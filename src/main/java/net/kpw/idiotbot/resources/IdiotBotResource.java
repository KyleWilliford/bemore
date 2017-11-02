package net.kpw.idiotbot.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.kpw.idiotbot.core.Blacklist;
import net.kpw.idiotbot.core.OpenPhish;
import net.kpw.idiotbot.core.PhishTank;
import net.kpw.idiotbot.core.ZeuSBlacklist;

/**
 * @author kwilliford
 * @created Oct 24, 2017
 *
 */
@Path("/api")
public class IdiotBotResource {
    private static final Log LOG = LogFactory.getLog(IdiotBotResource.class);

    private final Blacklist blacklist;
    private final PhishTank phishTank;
    private final OpenPhish openPhish;
    private final ZeuSBlacklist zeusBlacklist;

    public IdiotBotResource(final Blacklist blacklist, final PhishTank phishTank, final OpenPhish openPhish,
            final ZeuSBlacklist zeusBlacklist) {
        this.blacklist = blacklist;
        this.phishTank = phishTank;
        this.openPhish = openPhish;
        this.zeusBlacklist = zeusBlacklist;
    }

    @POST
    @Path("/is_blacklisted")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isDomainBlacklisted(@FormParam("text") String text) {
        LOG.debug(text);
        String responseText;
        if (blacklist.isDomainBlacklisted(text)) {
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
    public Response isURLPhishy(@FormParam("text") String text) {
        LOG.debug(text);
        String responseText;
        if (phishTank.isURLAPhishery(text) || openPhish.isURLAPhishery(text)) {
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
    public Response isZeuSDomainBlacklisted(@FormParam("text") String text) {
        LOG.debug(text);
        String responseText;
        if (zeusBlacklist.isDomainBlacklisted(text)) {
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
    public Response isZeuSIPBlacklisted(@FormParam("text") String text) {
        LOG.debug(text);
        String responseText;
        if (zeusBlacklist.isIPBlacklisted(text)) {
            responseText = "Found it! Don't trust that ip address! It's bad, m'kay.";
        } else {
            responseText = "I couldn't find that ip address in the blacklist of ZeuS domains.";
        }
        return Response.ok().entity(responseText).build();
    }

    @POST
    @Path("/find_any_match")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response isAnythingMatched(@FormParam("text") String text) {
        LOG.debug(text);
        final boolean matchDisposableEmailDomain = blacklist.isDomainBlacklisted(text);
        final boolean matchPhishTank = phishTank.isURLAPhishery(text);
        final boolean matchOpenPhish = openPhish.isURLAPhishery(text);
        final boolean matchZeuSDomains = zeusBlacklist.isDomainBlacklisted(text);
        final boolean matchZeuSIPv4 = zeusBlacklist.isIPBlacklisted(text);
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
        }  else {
            sb.append(" no databases.");
        }
        final String responseText = sb.toString();
        LOG.debug(responseText);
        return Response.ok().entity(responseText).build();
    }
}
