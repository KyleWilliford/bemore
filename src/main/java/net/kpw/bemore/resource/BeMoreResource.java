package net.kpw.bemore.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.kpw.bemore.dao.DataDAO;
import net.kpw.bemore.dto.DataDTO;

@Path("/bemore")
@Produces(MediaType.APPLICATION_JSON)
public class BeMoreResource {

    private final DataDAO dataDao;
    
    public BeMoreResource(DataDAO dataDao) {
        this.dataDao = dataDao;
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData() {
        return Response.ok().build();
    }
    
    @POST
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postData(DataDTO dataDto) {
        return Response.ok().build();
    }
    
    @PUT
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putData(DataDTO dataDto) {
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteData(DataDTO dataDto) {
        return Response.ok().build();
    }
}
