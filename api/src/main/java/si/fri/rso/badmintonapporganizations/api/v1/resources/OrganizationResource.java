package si.fri.rso.badmintonapporganizations.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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

    @Operation(description = "Get all organizations in a list", summary = "Get all organizations")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of organizations",
                    content = @Content(schema = @Schema(implementation = Organization.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getOrganizations() {
        List<Organization> organization = organizationBean.getOrganizations(uriInfo);

        return Response.status(Response.Status.OK).entity(organization).build();
    }

    @Operation(description = "Get data for an organization.", summary = "Get data for an organization")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Organization",
                    content = @Content(
                            schema = @Schema(implementation = Organization.class))
            )})
    @GET
    @Path("/{organizationId}")
    public Response getOrganization(@Parameter(description = "Organization ID.", required = true)
                                        @PathParam("organizationId") Integer organizationId) {

        Organization organ = organizationBean.getOrganization(organizationId);

        if (organ == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(organ).build();
    }

    @Operation(description = "Add organization.", summary = "Add organization")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Organization successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createOrganization(@RequestBody(
            description = "DTO object with reservation data.",
            required = true, content = @Content(
            schema = @Schema(implementation = Organization.class))) Organization org) {

        //check for profanity with external api
        if (org.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            org = organizationBean.createOrganization(org);
        }

        return Response.status(Response.Status.CREATED).entity(org).build();

    }

    @Operation(description = "Delete organization.", summary = "Delete organization")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Organization successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{organizationId}")
    public Response deleteOrganization(@Parameter(description = "Organization ID.", required = true)
                                           @PathParam("organizationId") Integer orgId){

        boolean deleted = organizationBean.deleteOrganization(orgId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a organization.", summary = "Update organization")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Organization successfully updated."
            )
    })
    @PUT
    @Path("{organizationId}")
    public Response putOrganization(@Parameter(description = "Organization ID.", required = true)
                                        @PathParam("organizationId") Integer orgId,
                                    @RequestBody(
                                            description = "DTO object with reservation data.",
                                            required = true, content = @Content(
                                            schema = @Schema(implementation = Organization.class))) Organization org){

        org = organizationBean.putOrganization(orgId, org);

        if (org == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

}
