package net.kpw.slackboat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
public class SlackBoatConfiguration extends Configuration {
    @Valid
    @NotNull
    private HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration();

    @Valid
    @NotNull
    private SlackClientAppConfiguration slackClientAppConfiguration = new SlackClientAppConfiguration();

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClientConfiguration;
    }

    @JsonProperty("slack")
    public SlackClientAppConfiguration getSlackClientAppConfiguration() {
        return slackClientAppConfiguration;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
        this.httpClientConfiguration = httpClient;
    }

    @JsonProperty("slack")
    public void setSlackClientAppConfiguration(SlackClientAppConfiguration slackClientAppConfiguration) {
        this.slackClientAppConfiguration = slackClientAppConfiguration;
    }
}
