package com.bbende.project.starter.web.api.resource;

import com.bbende.project.starter.dto.PersonDTO;
import com.bbende.project.starter.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * NOTE: JAX-RS resources must have an @Path annotation at the class level, otherwise they
 * will not be linked to the Jersey configuration, even with explicit calls to register in ResourceConfig.
 */
@Component
@Path("/people")
public class PersonResource {

    private final PersonService personService;

    @Autowired
    public PersonResource(final PersonService personService) {
        this.personService = personService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPeople() {
        final List<PersonDTO> people = personService.getAll();
        return Response.ok(people).build();
    }

}
