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

@Path("/api")
public class IdiotBotResource {
    private static final Log LOG = LogFactory.getLog(IdiotBotResource.class);
    
    private Blacklist blacklist;
    
    public IdiotBotResource(final Blacklist blacklist) {
        this.blacklist = blacklist;
    }
    
    @POST
    @Path("/is_blacklisted")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postData(@FormParam("text") String text) {
        LOG.debug(text);
        String responseText = "Hmm, I'm not sure.";
        if (blacklist.isDomainBlacklisted(text)) {
            responseText = "Yeah... don't trust " + text + "; It's bad, m'kay.";
        } else {
            responseText = "Nope. " + text + " was not found in the blacklist of domains.";
        }
        return Response.ok().entity(responseText).build();
    }
}
