package net.kpw.slackboat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import net.kpw.slackboat.core.Blacklist;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuSBlacklist;
import net.kpw.slackboat.resources.SlackBoatResource;
import net.kpw.slackboat.resources.SlackOAuthResource;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
public class SlackBoatApplication extends Application<SlackBoatConfiguration> {
    private static final Log LOG = LogFactory.getLog(SlackBoatApplication.class);

    public static void main(final String[] args) throws Exception {
        new SlackBoatApplication().run(args);
    }

    @Override
    public String getName() {
        return "slackboat";
    }

    @Override
    public void run(final SlackBoatConfiguration configuration, final Environment environment) {
        // HTTP client
        final HttpClient httpClient = new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration()).build(getName());

        /*
         * Using HTTP requests to load resources is something to do for a production version of this application.
         * For now, we will just load local files.
        Blacklist blacklist = null;
        HttpGet httpGet = new HttpGet("https://cdn.rawgit.com/martenson/disposable-email-domains/ed360890/disposable_email_blacklist.conf");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            blacklist = new Blacklist(parseTextFile(entity.getContent()));
            EntityUtils.consume(entity);
        } catch (Exception e) {
            LOG.error(e);
            System.exit(0);
        }
        */

        final Blacklist blacklist = new Blacklist(parseTextFile(getClass().getResourceAsStream("/disposable_email_blacklist.conf")));
        final PhishTank phishTank = new PhishTank(parseCSVFile(getClass().getResourceAsStream("/verified_online.csv")));
        final OpenPhish openPhish = new OpenPhish(parseTextFile(getClass().getResourceAsStream("/openphish.txt")));
        final ZeuSBlacklist zeusBlacklist = new ZeuSBlacklist(parseTextFile(getClass().getResourceAsStream("/ZeuS_bad_domains.txt")), 
                parseTextFile(getClass().getResourceAsStream("/ZeuS_ipv4_addresses.txt")));

        // Resources
        final SlackBoatResource slackBoatResource = new SlackBoatResource(blacklist, phishTank, openPhish, zeusBlacklist, 
                configuration.getSlackClientAppConfiguration().getVerificationToken());
        final SlackOAuthResource slackOAuthResource = new SlackOAuthResource(configuration.getSlackClientAppConfiguration(), httpClient);
        environment.jersey().register(slackBoatResource);
        environment.jersey().register(slackOAuthResource);
    }

    private Set<String> parseTextFile(InputStream inputStream) {
        try {
            Set<String> domains = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.toCollection(() -> new TreeSet<>()));
            LOG.debug("Loaded " + domains.size() + " bad email domains");
            return domains;
        } catch (Exception e) {
            LOG.error(e);
        }
        return new TreeSet<>();
    }

    private Set<String> parseCSVFile(InputStream inputStream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            Set<String> urls = br.lines().map(line -> {
                String[] split = line.split(",");
                if (split.length >= 2) {
                    return split[1]; // url column
                }
                return "";
            }).collect(Collectors.toCollection(() -> new TreeSet<>()));
            LOG.debug("Loaded " + urls.size() + " phishy urls");
            return urls;
        } catch (Exception e) {
            LOG.error(e);
        }
        return new TreeSet<>();
    }
}
