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
    
    public IdiotBotResource(final Blacklist blacklist, final PhishTank phishTank, final OpenPhish openPhish) {
        this.blacklist = blacklist;
        this.phishTank = phishTank;
        this.openPhish = openPhish;
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
}
