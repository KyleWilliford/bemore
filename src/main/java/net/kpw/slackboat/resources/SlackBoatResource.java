package net.kpw.slackboat.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.kpw.slackboat.core.DisposableEmailDomainList;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuSDomainList;
import net.kpw.slackboat.core.constant.Constants;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class SlackBoatResource {
    static final Log LOG = LogFactory.getLog(SlackBoatResource.class);

    private final DisposableEmailDomainList disposableEmailDomainList;
    private final PhishTank phishTank;
    private final OpenPhish openPhish;
    private final ZeuSDomainList zeus;

    private final String verificationToken;

    public SlackBoatResource(final DisposableEmailDomainList disposableEmailDomainList, final PhishTank phishTank,
            final OpenPhish openPhish, final ZeuSDomainList zeus, final String verificationToken) {
        this.disposableEmailDomainList = disposableEmailDomainList;
        this.phishTank = phishTank;
        this.openPhish = openPhish;
        this.zeus = zeus;
        this.verificationToken = verificationToken;
    }

    @POST
    @Path("/find_any_match")
    public Response finaAnyMatch(@FormParam("text") final String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity("The input was not found in any database.").build();
        }
        LOG.debug(text);
        final boolean matchDisposableEmailDomain = disposableEmailDomainList.isDomainBlacklisted(text);
        final boolean matchPhishTank = phishTank.isURLBlacklisted(text);
        final boolean matchOpenPhish = openPhish.isURLBlacklisted(text);
        final boolean matchZeuSDomains = zeus.isDomainBlacklisted(text);
        final boolean matchZeuSIPv4 = zeus.isIPBlacklisted(text);
        StringBuilder sb = new StringBuilder("The input was found in these databases:");
        if (matchDisposableEmailDomain) {
            sb.append("\nThe Disposable Email/Spam Blacklist");
        } else if (matchPhishTank) {
            sb.append("\nThe PhishTank phishing url list.");
        } else if (matchOpenPhish) {
            sb.append("\nThe OpenPhish phishing url list.");
        } else if (matchZeuSDomains) {
            sb.append("\nThe ZeuS trojan domain blacklist.");
        } else if (matchZeuSIPv4) {
            sb.append("\nThe ZeuS trojan ipv4 blacklist.");
        } else {
            sb.append(" no databases.");
        }
        final String responseText = sb.toString();
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_spam_domain")
    public Response isDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity(Constants.DISPOSABLE_DOMAIN_BLACKLISTED_FALSE).build();
        }
        LOG.debug(text);
        String responseText;
        if (disposableEmailDomainList.isDomainBlacklisted(text)) {
            responseText = Constants.DISPOSABLE_DOMAIN_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.DISPOSABLE_DOMAIN_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_in_openphish")
    public Response isURLOpenPhishDetected(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity(Constants.OPENPHISH_URL_BLACKLISTED_FALSE).build();
        }
        LOG.debug(text);
        String responseText;
        if (openPhish.isURLBlacklisted(text)) {
            responseText = Constants.OPENPHISH_URL_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.OPENPHISH_URL_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_in_phishtank")
    public Response isURLPhishTankDetected(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity(Constants.PHISHTANK_URL_BLACKLISTED_FALSE).build();
        }
        LOG.debug(text);
        String responseText;
        if (phishTank.isURLBlacklisted(text)) {
            responseText = Constants.PHISHTANK_URL_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.PHISHTANK_URL_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_zeus_domain")
    public Response isZeuSDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity(Constants.ZEUS_DOMAIN_BLACKLISTED_FALSE).build();
        }
        LOG.debug(text);
        String responseText;
        if (zeus.isDomainBlacklisted(text)) {
            responseText = Constants.ZEUS_DOMAIN_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.ZEUS_DOMAIN_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_zeus_ipv4")
    public Response isZeuSIPBlacklisted(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity(Constants.ZEUS_IP_BLACKLISTED_FALSE).build();
        }
        LOG.debug(text);
        String responseText;
        if (zeus.isIPBlacklisted(text)) {
            responseText = Constants.ZEUS_IP_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.ZEUS_IP_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/search")
    public Response search(@FormParam("text") String text, @FormParam("token") final String token,
            @FormParam("ssl_check") final String sslCheck) {
        if (!verifyToken(token)) {
            return Response.status(Status.UNAUTHORIZED).entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        if (StringUtils.isNotBlank(sslCheck)) {
            return Response.ok().build();
        }
        if (StringUtils.isBlank(text)) {
            return Response.ok().entity("The input was not found in any database.").build();
        }
        LOG.debug(text);
        StringBuilder sb = new StringBuilder();
        sb.append("You searched for: ").append(text.replaceAll("\\.", "[dot]")).append("\n")
        .append("Note: Some characters are escaped so that Slack will not create hyperlinks.\n");
        final String preamble = sb.toString();
        sb = new StringBuilder();
        
        // Malware Domains
        List<String> results = this.disposableEmailDomainList.searchDomainBlacklist(text);
        if (!results.isEmpty()) {
            sb.append("Found some results in the Disposable Email/Spam Blacklist:\n");
            for (int i = 0; i < results.size(); i++) {
                sb.append(">").append(results.get(i));
                if (i + 1 < results.size()) {
                    sb.append("\n");
                }
            }
        }
        
        // PhishTank
        results = this.phishTank.searchURLBlacklist(text);
        if (!results.isEmpty()) {
            sb.append("\nFound some results in the PhishTank database:\n");
            for (int i = 0; i < results.size(); i++) {
                sb.append(">").append(results.get(i));
                if (i + 1 < results.size()) {
                    sb.append("\n");
                }
            }
        }
        // OpenPhish
        results = this.openPhish.searchURLBlacklist(text);
        if (!results.isEmpty()) {
            sb.append("\nFound some results in the OpenPhish database:\n");
            for (int i = 0; i < results.size(); i++) {
                sb.append(">").append(results.get(i));
                if (i + 1 < results.size()) {
                    sb.append("\n");
                }
            }
        }
        
        // Zeus Domains
        results = this.zeus.searchDomainBlacklist(text);
        if (!results.isEmpty()) {
            sb.append("\nFound some results in the Zeus domain database:\n");
            for (int i = 0; i < results.size(); i++) {
                sb.append(">").append(results.get(i));
                if (i + 1 < results.size()) {
                    sb.append("\n");
                }
            }
        }
        
        // Zeus IPs
        results = this.zeus.searchIPBlacklist(text);
        if (!results.isEmpty()) {
            sb.append("\nFound some results in the Zeus ip database:\n");
            for (int i = 0; i < results.size(); i++) {
                sb.append(">").append(results.get(i));
                if (i + 1 < results.size()) {
                    sb.append("\n");
                }
            }
        }
        String resultsText = sb.toString();
        if ("".equals(resultsText)) {
            resultsText = "\nNo results found.";
        }
        final String responseText = preamble + resultsText;
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    /**
     * Verify that the request is coming from Slack.
     * 
     * @param token
     *            The verification token from Slack.
     * @return True if the token is verified, otherwise false.
     */
    private boolean verifyToken(final String token) {
        LOG.debug(token);
        if (!this.verificationToken.equals(token)) {
            LOG.error(Constants.VERIFICATION_TOKEN_INVALID);
            return false;
        }
        return true;
    }
}
