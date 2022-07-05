package com.bbende.project.starter.web.mvc;

import com.bbende.project.starter.component.about.AboutInfoDto;
import com.bbende.project.starter.component.about.AboutService;
import com.bbende.project.starter.component.person.PersonDto;
import com.bbende.project.starter.component.person.PersonNotFoundException;
import com.bbende.project.starter.component.person.PersonService;
import com.bbende.project.starter.component.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PersonController.class)
public class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private AboutService aboutService;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        final AboutInfoDto aboutInfoDto = new AboutInfoDto();
        aboutInfoDto.setLabel("WebMvcTest");
        aboutInfoDto.setVersion("test-version");
        when(aboutService.getAboutInfo()).thenReturn(aboutInfoDto);
    }

    @Test
    public void testGetAllPeople() throws Exception {
        PersonDto p1 = createPerson("1", "John", "Smith", 35);
        PersonDto p2 = createPerson("2", "Alice", "Smith", 30);

        List<PersonDto> people = Arrays.asList(p1, p2);
        when(personService.getAll()).thenReturn(people);

        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/list"))
                .andExpect(model().attribute("people", people))
                .andExpect(model().attributeExists("projectDetails"));
    }

    @Test
    public void testNewPerson() throws Exception {
        mockMvc.perform(get("/people/new"))
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

        mockMvc.perform(get("/people/" + person.getId() + "/delete/confirm"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/delete-confirm"))
                .andExpect(model().attribute("person", person));
    }

    @Test
    public void testConfirmDeleteWhenPersonDoesNotExist() throws Exception {
        when(personService.get(any())).thenThrow(new PersonNotFoundException("1"));

        mockMvc.perform(get("/people/1/delete/confirm"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("status"));
    }

    @Test
    public void testDeleteWhenPersonExists() throws Exception {
        mockMvc.perform(delete("/people/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/people"));
    }

    @Test
    public void testDeleteWhenPersonDoesNotExist() throws Exception {
        when(personService.delete(any())).thenThrow(new PersonNotFoundException("1"));

        mockMvc.perform(delete("/people/1"))
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
