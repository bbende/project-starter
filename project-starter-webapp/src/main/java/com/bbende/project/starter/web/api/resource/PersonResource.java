package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.core.commons.dto.ListDto;
import com.bbende.project.starter.core.modules.person.PersonDto;
import com.bbende.project.starter.core.modules.person.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * NOTE: JAX-RS resources must have an @Path annotation at the class level, otherwise they
 * will not be linked to the Jersey configuration, even with explicit calls to register in ResourceConfig.
 */
@Component
@Path("/people")
public class PersonResource extends ApplicationResource {

    private final PersonService personService;

    @Autowired
    public PersonResource(final PersonService personService) {
        this.personService = personService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get People",
            description = "Retrieves all the people",
            tags = {"people"},
            responses = {
                @ApiResponse(description = "The list of people", content = @Content(
                        schema = @Schema(implementation = ListDto.class)
                ))
            }
    )
    public Response getPeople() {
        final List<PersonDto> people = personService.getAll();
        final ListDto<PersonDto> list = new ListDto<>(people);
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get Person",
            description = "Retrieves a person by id",
            tags = {"people"},
            responses = {
                    @ApiResponse(description = "The person with the given id", content = @Content(
                            schema = @Schema(implementation = PersonDto.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Person not found")
            }
    )
    public Response getPerson(
            @PathParam("id")
            @Parameter(description = "The id of the person to retrieve", required = true)
            final String id) {
        final PersonDto person = personService.get(id);
        return Response.ok(person).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create person",
            description = "Creates a new person",
            tags = {"people"},
            responses = {
                    @ApiResponse(description = "The created person", content = @Content(
                            schema = @Schema(implementation = PersonDto.class)
                    )),
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            }
    )
    public Response createPerson(
            @Valid
            @Parameter(description = "The person to create", required = true,
                    schema = @Schema(implementation = PersonDto.class))
            final PersonDto person) {
        final PersonDto createdPerson = personService.create(person);

        final URI uri = getBaseUriBuilder()
                .path(PersonResource.class, "getPerson")
                .resolveTemplate("id", createdPerson.getId())
                .build();

        return Response.created(uri).entity(createdPerson).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Update person",
            description = "Updates a new person",
            tags = {"people"},
            responses = {
                    @ApiResponse(description = "The updated person", content = @Content(
                            schema = @Schema(implementation = PersonDto.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Person not found"),
                    @ApiResponse(responseCode = "405", description = "Invalid input")
            }
    )
    public Response updatePerson(
            @PathParam("id")
            @Parameter(description = "The id of the person to update", required = true)
            final String id,
            @Parameter(description = "The person to update", required = true,
                    schema = @Schema(implementation = PersonDto.class))
            final PersonDto person) {

        if (person.getId() == null) {
            person.setId(id);
        }

        if (!person.getId().equals(id)) {
            throw new IllegalStateException("Id in request body must match id in url path");
        }

        final PersonDto updatedPerson = personService.update(person);
        return Response.ok(updatedPerson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Delete Person",
        description = "Delete a person by ID",
        tags = {"people"},
        responses = {
            @ApiResponse(description = "The deleted person", content = @Content(
                schema = @Schema(implementation = PersonDto.class)
            )),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Person not found")
        }
    )
    public Response deletePerson(
            @PathParam("id")
            @Parameter(description = "The id of the person to delete", required = true)
            final String id) {
        final PersonDto deleted = personService.delete(id);
        return Response.ok(deleted).build();
    }

}
