package com.example.userscrud.controller;

import com.example.userscrud.Utils;
import com.example.userscrud.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    private static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(20);

    private final String url = "users";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserTest() throws Exception {
        var user = Utils.getDefaultUser();

        given(userService.createUser(user)).willReturn(user);

        mvc.perform(post("/{url}", url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(user)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void getAllUsersAndExpectSomeTest() throws Exception {
        var user = Utils.getDefaultUser();

        given(userService.getAllByBirthDateBetween(LocalDate.MIN, LocalDate.MAX, DEFAULT_PAGEABLE)).willReturn(List.of(user));

        mvc.perform(get("/{url}", url)
                        .queryParam("from", LocalDate.MIN.toString())
                        .queryParam("to", LocalDate.MAX.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId().intValue())));
    }

    @Test
    void getAllUsersAndExpectNoneTest() throws Exception {
        var user = Utils.getDefaultUser();

        given(userService.getAllByBirthDateBetween(user.getBirthDate().plusYears(1), LocalDate.MAX, DEFAULT_PAGEABLE)).willReturn(List.of());

        mvc.perform(get("/{url}", url)
                        .queryParam("from", user.getBirthDate().plusYears(1).toString())
                        .queryParam("to", LocalDate.MAX.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void fullUpdateUserTest() throws Exception {
        var user = Utils.getDefaultUser();

        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        given(userService.updateUser(user)).willReturn(user);

        mvc.perform(patch("/{url}/{id}", url, user.getId())
                        .contentType("application/json-patch+json")
                        .content("""
                                [
                                    {"op": "replace", "path": "/email", "value": "%s"},
                                    {"op": "replace", "path": "/firstName", "value": "%s"},
                                    {"op": "replace", "path": "/lastName", "value": "%s"},
                                    {"op": "replace", "path": "/birthDate", "value": "%s"},
                                    {"op": "replace", "path": "/address", "value": "%s"},
                                    {"op": "replace", "path": "/phoneNumber", "value": "%s"}
                                ]
                                """.formatted(user.getEmail(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getAddress(), user.getPhoneNumber())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(
                        jsonPath("$.id", is(user.getId().intValue())),
                        jsonPath("$.email", is(user.getEmail())),
                        jsonPath("$.firstName", is(user.getFirstName())),
                        jsonPath("$.lastName", is(user.getLastName())),
                        jsonPath("$.birthDate", is(user.getBirthDate().toString())),
                        jsonPath("$.address", is(user.getAddress())),
                        jsonPath("$.phoneNumber", is(user.getPhoneNumber()))
                );
    }

    @Test
    void partialUpdateUserTest() throws Exception {
        var user = Utils.getDefaultUser();

        given(userService.getUserById(user.getId())).willReturn(Optional.of(user));
        given(userService.updateUser(user)).willReturn(user);

        mvc.perform(patch("/{url}/{id}", url, user.getId())
                        .contentType("application/json-patch+json")
                        .content("""
                                [
                                    {"op": "replace", "path": "/email", "value": "%s"},
                                    {"op": "replace", "path": "/firstName", "value": "%s"}
                                ]
                                """.formatted(user.getEmail(), user.getFirstName())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(
                        jsonPath("$.id", is(user.getId().intValue())),
                        jsonPath("$.email", is(user.getEmail())),
                        jsonPath("$.firstName", is(user.getFirstName()))
                );
    }

    @Test
    void deleteUserTest() throws Exception {
        var user = Utils.getDefaultUser();

        mvc.perform(delete("/{url}/{id}", url, user.getId()))
                .andExpect(status().is2xxSuccessful());
    }

}