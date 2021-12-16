package si.fri.rso.badmintonapporganizations.api.v1.resources;

import si.fri.rso.badmintonapporganizations.lib.Organization;
import si.fri.rso.badmintonapporganizations.services.beans.OrganizationBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    private Logger log = Logger.getLogger(OrganizationResource.class.getName());

    @Inject
    private OrganizationBean organizationBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getOrganizations() {
        List<Organization> organization = organizationBean.getOrganizations(uriInfo);

        return Response.status(Response.Status.OK).entity(organization).build();
    }

    @GET
    @Path("/{organizationId}")
    public Response getOrganization(@PathParam("organizationId") Integer organizationId) {

        Organization organ = organizationBean.getOrganization(organizationId);

        if (organ == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(organ).build();
    }

    @POST
    public Response createOrganization(Organization org) {

        //check for profanity with external api
        if (org.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            org = organizationBean.createOrganization(org);
        }

        return Response.status(Response.Status.CREATED).entity(org).build();

    }

    @DELETE
    @Path("{organizationId}")
    public Response deleteOrganization(@PathParam("organizationId") Integer orgId){

        boolean deleted = organizationBean.deleteOrganization(orgId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{organizationId}")
    public Response putOrganization(@PathParam("organizationId") Integer orgId,
                               Organization org){

        org = organizationBean.putOrganization(orgId, org);

        if (org == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

}
