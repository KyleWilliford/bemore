package net.kpw.bemore.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bemore")
@Produces(MediaType.APPLICATION_JSON)
public class BeMoreResource {

    public BeMoreResource() {
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData() {
        return Response.ok().entity("Test successful!").build();
    }
}
