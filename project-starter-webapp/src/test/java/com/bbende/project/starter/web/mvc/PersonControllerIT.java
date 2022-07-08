package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.component.person.PersonDto;
import com.bbende.project.starter.component.person.PersonNotFoundException;
import com.bbende.project.starter.component.person.PersonService;
import com.bbende.project.starter.security.cookie.CookieService;
import com.bbende.project.starter.security.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PersonController.class)
public class PersonControllerIT {

    private MockMvc mockMvc;

    // Web-app context required to create MockMvc
    @Autowired private WebApplicationContext context;

    // Not used directly, but required to load SecurityConfig
    @MockBean private TokenService tokenService;
    @MockBean private CookieService cookieService;
    @MockBean private UserDetailsService userDetailsService;

    // Dependency of the controller
    @MockBean private PersonService personService;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void testGetAllPeople() throws Exception {
        PersonDto p1 = createPerson("1", "John", "Smith", 35);
        PersonDto p2 = createPerson("2", "Alice", "Smith", 30);

        List<PersonDto> people = Arrays.asList(p1, p2);
        when(personService.getAll()).thenReturn(people);

        mockMvc.perform(
                get("/people")
                        .with(user("user"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("people/list"))
                .andExpect(model().attribute("people", people));
    }

    @Test
    public void testNewPerson() throws Exception {
        mockMvc.perform(
                get("/people/new")
                        .with(user("user"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("people/new"))
                .andExpect(model().attributeExists("person"));
    }

    @Test
    public void testCreatePersonSuccess() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Smith")
                        .param("age", "37")
                        .with(user("user"))
                        .with(csrf())
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/people"));

        verify(personService, new Times(1)).create(any());
    }

    @Test
    public void testCreatePersonValidationErrors() throws Exception {
        mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Smith")
                        .param("age", "NOT-A-NUMBER")
                        .with(user("user"))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("people/new"))
                .andExpect(model().hasErrors());

        verifyNoInteractions(personService);
    }

    @Test
    public void testConfirmDeleteWhenPersonExists() throws Exception{
        final PersonDto person = createPerson("1", "John", "Smith", 35);
        when(personService.get(person.getId())).thenReturn(person);

        mockMvc.perform(
                get("/people/" + person.getId() + "/delete/confirm")
                        .with(user("user"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("people/delete-confirm"))
                .andExpect(model().attribute("person", person));
    }

    @Test
    public void testConfirmDeleteWhenPersonDoesNotExist() throws Exception {
        when(personService.get(any())).thenThrow(new PersonNotFoundException("1"));

        mockMvc.perform(
                get("/people/1/delete/confirm")
                        .with(user("user"))
                )
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    public void testDeleteWhenPersonExists() throws Exception {
        mockMvc.perform(
                delete("/people/1")
                        .with(user("user"))
                        .with(csrf())
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/people"));
    }

    @Test
    public void testDeleteWhenPersonDoesNotExist() throws Exception {
        when(personService.delete(any())).thenThrow(new PersonNotFoundException("1"));

        mockMvc.perform(
                delete("/people/1")
                        .with(user("user"))
                        .with(csrf())
                )
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("status"));
    }

    private PersonDto createPerson(String id, String firstName, String lastName, int age) {
        PersonDto person = new PersonDto();
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAge(age);
        return person;
    }
}
