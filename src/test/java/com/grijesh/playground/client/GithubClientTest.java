package com.grijesh.playground.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grijesh.playground.model.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubClientTest {

    private static MockWebServer mockWebServer;

    private GithubClient githubClient;

    @BeforeAll
    public static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public void setup() {
        final var baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        githubClient = new GithubClient(WebClient.create(), baseUrl);
    }

    @Test
    @DisplayName("Get users should return user details successfully")
    public void getUserShouldReturnUserSuccessfully() throws JsonProcessingException, InterruptedException {
        final var expected = new User("test", "http://test");
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(expected))
                .addHeader("Content-Type", "application/json"));

        final var user = githubClient.getUser("user");

        StepVerifier.create(user)
                .expectNext(expected)
                .verifyComplete();
        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/users/user");
    }

    @Test
    @DisplayName("Get users should throw an exception when get error from server")
    public void getUserShouldThrowExceptionOnErrorFromServer() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .addHeader("Content-Type", "application/json"));

        final var user = githubClient.getUser("not_found");

        StepVerifier.create(user)
                .expectError(WebClientResponseException.class)
                .verify();

        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        assertThat(recordedRequest.getPath()).isEqualTo("/users/not_found");
    }

}
