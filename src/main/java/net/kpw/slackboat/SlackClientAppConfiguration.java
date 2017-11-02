package net.kpw.slackboat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * @author kwilliford
 * @created Nov 2, 2017
 *
 */
public class SlackClientAppConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("clientID")
    private String clientID;

    @Valid
    @NotNull
    @JsonProperty("clientSecret")
    private String clientSecret;

    @Valid
    @NotNull
    @JsonProperty("verificationToken")
    private String verificationToken;

    /**
     * @return the clientID
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * @return the clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @return the verificationToken
     */
    public String getVerificationToken() {
        return verificationToken;
    }

    /**
     * @param clientID
     *            the clientID to set
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    /**
     * @param clientSecret
     *            the clientSecret to set
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @param verificationToken
     *            the verificationToken to set
     */
    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}
