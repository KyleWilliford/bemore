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
import net.kpw.slackboat.core.ZeuSDomainList;
import net.kpw.slackboat.core.constant.Constants;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
@Path("/api")
public class SlackBoatResource {
    private static final Log LOG = LogFactory.getLog(SlackBoatResource.class);

    private final DisposableMalwareDomainList disposableMalwareDomainList;
    private final PhishTank phishTank;
    private final OpenPhish openPhish;
    private final ZeuSDomainList zeus;

    private final String verificationToken;

    public SlackBoatResource(final DisposableMalwareDomainList disposableMalwareDomainList, final PhishTank phishTank,
            final OpenPhish openPhish, final ZeuSDomainList zeus, final String verificationToken) {
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
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        final boolean matchDisposableEmailDomain = disposableMalwareDomainList.isDomainBlacklisted(text);
        final boolean matchPhishTank = phishTank.isURLBlacklisted(text);
        final boolean matchOpenPhish = openPhish.isURLBlacklisted(text);
        final boolean matchZeuSDomains = zeus.isDomainBlacklisted(text);
        final boolean matchZeuSIPv4 = zeus.isIPBlacklisted(text);
        StringBuilder sb = new StringBuilder("The input was found in these databases:");
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
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_spam_domain")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (disposableMalwareDomainList.isDomainBlacklisted(text)) {
            responseText = Constants.DISPOSABLE_DOMAIN_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.DISPOSABLE_DOMAIN_BLACKLISTED_FALSE;
        }
        LOG.info(responseText);
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/is_in_phishtank")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isURLPhishTankDetected(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
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
    @Path("/is_in_openphish")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isURLOpenPhishDetected(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
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
    @Path("/is_zeus_domain")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isZeuSDomainBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
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
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isZeuSIPBlacklisted(@FormParam("text") String text, @FormParam("token") final String token) {
        LOG.debug(text);
        if (!verifyToken(token)) {
            return Response.serverError().entity(Constants.VERIFICATION_TOKEN_INVALID).build();
        }
        String responseText;
        if (zeus.isIPBlacklisted(text)) {
            responseText = Constants.ZEUS_IP_BLACKLISTED_TRUE;
        } else {
            responseText = Constants.ZEUS_IP_BLACKLISTED_FALSE;
        }
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
