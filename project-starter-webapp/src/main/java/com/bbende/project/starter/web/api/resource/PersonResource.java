package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.dto.ListDTO;
import com.bbende.project.starter.dto.PersonDTO;
import com.bbende.project.starter.service.PersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("people")
public class PersonResource extends ApplicationResource {

    private final PersonService personService;

    @Autowired
    public PersonResource(final PersonService personService) {
        this.personService = personService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all people", response = ListDTO.class)
    public Response getPeople() {
        final List<PersonDTO> people = personService.getAll();
        final ListDTO<PersonDTO> list = new ListDTO<>(people);
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get person", response = PersonDTO.class)
    public Response getPerson(@PathParam("id") final String id) {
        final PersonDTO person = personService.get(id);
        return Response.ok(person).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create person", response = PersonDTO.class)
    public Response createPerson(@Valid final PersonDTO person) {
        final PersonDTO createdPerson = personService.create(person);

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
    @ApiOperation(value = "Update person", response = PersonDTO.class)
    public Response updatePerson(@PathParam("id") final String id, final PersonDTO person) {
        if (person.getId() == null) {
            person.setId(id);
        }

        if (!person.getId().equals(id)) {
            throw new IllegalStateException("Id in request body must match id in url path");
        }

        final PersonDTO updatedPerson = personService.update(person);
        return Response.ok(updatedPerson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete person", response = PersonDTO.class)
    public Response deletePerson(@PathParam("id") final String id) {
        final PersonDTO deleted = personService.delete(id);
        return Response.ok(deleted).build();
    }

}
