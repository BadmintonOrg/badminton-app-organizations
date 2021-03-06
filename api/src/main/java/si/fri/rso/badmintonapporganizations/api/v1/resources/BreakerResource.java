package si.fri.rso.badmintonapporganizations.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import si.fri.rso.badmintonapporganizations.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS")
public class BreakerResource {
    private Logger log = Logger.getLogger(BreakerResource.class.getName());

    @Inject
    private RestProperties restProperties;

    @POST
    @Path("break")
    public Response makeUnhealthy() {

        restProperties.setBroken(true);
        log.info("API is now broken.");
        return Response.status(Response.Status.OK).build();
    }
}
