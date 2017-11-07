package net.kpw.slackboat.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.kpw.slackboat.core.DisposableMalwareDomainList;
import net.kpw.slackboat.core.OpenPhish;
import net.kpw.slackboat.core.PhishTank;
import net.kpw.slackboat.core.ZeuS;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
@Path("/api")
public class SlackBoatResource {
    private static final Log LOG = LogFactory.getLog(SlackBoatResource.class);

    private static final String VERIFICATION_TOKEN_INVALID = "Verification token invalid.";
    private final DisposableMalwareDomainList disposableMalwareDomainList;
    private final PhishTank phishTank;
    private final OpenPhish openPhish;
    private final ZeuS zeus;

    private final String verificationToken;

    public SlackBoatResource(final DisposableMalwareDomainList disposableMalwareDomainList, final PhishTank phishTank, final OpenPhish openPhish,
            final ZeuS zeus, final String verificationToken) {
        this.disposableMalwareDomainList = disposableMalwareDomainList;
        this.phishTank = phishTank;
        this.openPhish = openPhish;
        this.zeus = zeus;
        this.verificationToken = verificationToken;
    }

    @POST
    @Path("/find_any_match")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response finaAnyMatch(@FormParam("text") final String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(VERIFICATION_TOKEN_INVALID).build();
        }
        final boolean matchDisposableEmailDomain = disposableMalwareDomainList.isDomainBlacklisted(text);
        final boolean matchPhishTank = phishTank.isURLBlacklisted(text);
        final boolean matchOpenPhish = openPhish.isURLBlacklisted(text);
        final boolean matchZeuSDomains = zeus.isDomainBlacklisted(text);
        final boolean matchZeuSIPv4 = zeus.isIPBlacklisted(text);
        StringBuilder sb = new StringBuilder("That input matches:");
        if (matchDisposableEmailDomain) {
            sb.append("\nThe Disposable Email Blacklist (Spam domains)");
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
        LOG.debug(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_blacklisted")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (disposableMalwareDomainList.isDomainBlacklisted(text)) {
            responseText = "Found it! Don't trust that domain! It's bad, m'kay.";
        } else {
            responseText = "I couldn't find that domain in the blacklist of domains.";
        }
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_phishy")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isURLPhishy(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (phishTank.isURLBlacklisted(text) || openPhish.isURLBlacklisted(text)) {
            responseText = "Found it! Don't trust that url! It's bad, m'kay.";
        } else {
            responseText = "I couldn't find that url in my database of known phishing urls.";
        }
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_zeus_domain")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isZeuSDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (zeus.isDomainBlacklisted(text)) {
            responseText = "Found it! Don't trust that domain! It's bad, m'kay.";
        } else {
            responseText = "I couldn't find that domain in the blacklist of ZeuS domains.";
        }
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_zeus_ipv4")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isZeuSIPBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (zeus.isIPBlacklisted(text)) {
            responseText = "Found it! Don't trust that ip address! It's bad, m'kay.";
        } else {
            responseText = "I couldn't find that ip address in the blacklist of ZeuS domains.";
        }
        return Response.ok().entity(responseText).build();
    }

    /**
     * Verify that the request is coming from Slack.
     * @param token The verification token from Slack.
     * @return True if the token is verified, otherwise false.
     */
    private boolean verifyToken(final String token) {
        LOG.debug(token);
        if (!this.verificationToken.equals(token)) {
            LOG.error(VERIFICATION_TOKEN_INVALID);
            return false;
        }
        return true;
    }
}
