package net.kpw.slackboat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import net.kpw.slackboat.core.DomainBlacklist;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuSBlacklist;
import net.kpw.slackboat.resources.SlackBoatResource;
import net.kpw.slackboat.resources.SlackOAuthResource;
import net.kpw.slackboat.util.FileParser;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
public class SlackBoatApplication extends Application<SlackBoatConfiguration> {
    @SuppressWarnings("unused")
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
        
        final FileParser fileParser = FileParser.getInstance();

        final DomainBlacklist domainBlacklist = new DomainBlacklist(fileParser.parseLines(getClass().getResourceAsStream("/disposable_email_blacklist.conf")));
        final PhishTank phishTank = new PhishTank(fileParser.parseURLsSecondColumn(getClass().getResourceAsStream("/phishtank.csv")));
        final OpenPhish openPhish = new OpenPhish(fileParser.parseLines(getClass().getResourceAsStream("/openphish.txt")));
        final ZeuSBlacklist zeusBlacklist = new ZeuSBlacklist(fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_bad_domains.txt")), 
                fileParser.parseLines(getClass().getResourceAsStream("/ZeuS_ipv4_addresses.txt")));

        // Resources
        final SlackBoatResource slackBoatResource = new SlackBoatResource(domainBlacklist, phishTank, openPhish, zeusBlacklist, 
                configuration.getSlackClientAppConfiguration().getVerificationToken());
        final SlackOAuthResource slackOAuthResource = new SlackOAuthResource(configuration.getSlackClientAppConfiguration(), httpClient);
        environment.jersey().register(slackBoatResource);
        environment.jersey().register(slackOAuthResource);
    }
}
