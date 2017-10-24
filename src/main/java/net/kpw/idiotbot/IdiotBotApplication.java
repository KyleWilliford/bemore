package net.kpw.idiotbot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Environment;
import net.kpw.idiotbot.core.Blacklist;
import net.kpw.idiotbot.resources.IdiotBotResource;

public class IdiotBotApplication extends Application<IdiotBotConfiguration> {
    private static final Log LOG = LogFactory.getLog(IdiotBotApplication.class);

    public static void main(final String[] args) throws Exception {
        new IdiotBotApplication().run(args);
    }

    @Override
    public String getName() {
        return "idiot bot";
    }

    @Override
    public void run(final IdiotBotConfiguration configuration, final Environment environment) {
        // HTTP client
        final HttpClient httpClient = new HttpClientBuilder(environment).using(configuration.getHttpClientConfiguration()).build(getName());

        Blacklist blacklist = null;
        HttpGet httpGet = new HttpGet("https://cdn.rawgit.com/martenson/disposable-email-domains/ed360890/disposable_email_blacklist.conf");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            blacklist = new Blacklist(parseBlacklist(entity.getContent()));
            EntityUtils.consume(entity);
        } catch (Exception e) {
            LOG.error(e);
            System.exit(0);
        }

        // Resources
        final IdiotBotResource idiotBotResource = new IdiotBotResource(blacklist);
        environment.jersey().register(idiotBotResource);
    }

    private Set<String> parseBlacklist(InputStream inputStream) {
        try {
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.toCollection(() -> new TreeSet<>()));
        } catch (Exception e) {
            LOG.error(e);
        }
        return new TreeSet<>();
    }

}
