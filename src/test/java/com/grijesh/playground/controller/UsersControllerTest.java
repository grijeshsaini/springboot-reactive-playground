package com.grijesh.playground.controller;

import com.grijesh.playground.dto.UserDetails;
import com.grijesh.playground.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebFluxTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Get User API should be able to fetch the user details using short name")
    public void getUserShouldReturnUserDetails() {
        when(userService.getUser("test"))
                .thenReturn(Mono.just(new UserDetails("test", "http://test")));

        webTestClient
                .get()
                .uri("/users/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.short_name").isEqualTo("test")
                .jsonPath("$.avatar_url").isEqualTo("http://test");

    }

    @Test
    @DisplayName("Get User API should return not found when user is not present")
    public void getUserShouldThrowNotFoundExceptionWhenUserIsNotPresent() {
        when(userService.getUser("test"))
                .thenReturn(Mono.error(WebClientResponseException.create(404,
                        "Not Found", null, null, Charset.defaultCharset())));


        webTestClient
                .get()
                .uri("/users/test")
                .exchange()
                .expectStatus().isNotFound();

    }

}