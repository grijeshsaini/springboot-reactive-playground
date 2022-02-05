package com.grijesh.playground.client;

import com.grijesh.playground.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GithubClientUsingMockTest {

    private GithubClient githubClient;

    @Mock
    private ExchangeFunction exchangeFunction;

    @BeforeEach
    public void setup() {
        final var webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();
        githubClient = new GithubClient(webClient, "http://localhost");
    }

    @Test
    @DisplayName("Get users should return user details successfully")
    public void getUserShouldReturnUserSuccessfully() {
        final var userResponse = """
                 {
                   "login" : "test",
                   "avatar_url" : "http://test"
                 }
                """;
        final var okResponse = Mono.just(ClientResponse.create(HttpStatus.OK)
                .header("content-type", "application/json")
                .body(userResponse)
                .build());

        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(okResponse);

        final var user = githubClient.getUser("test");

        StepVerifier.create(user)
                .expectNext(new User("test", "http://test"))
                .verifyComplete();

        // OR another way to verify
        final var testUser = githubClient.getUser("test").block();
        assertThat(testUser.login()).isEqualTo("test");
        assertThat(testUser.avatarUrl()).isEqualTo("http://test");

        verify(exchangeFunction, times(2)).exchange(any());
    }

    @Test
    @DisplayName("Get users should throw an exception when get error from server")
    public void getUserShouldThrowExceptionOnErrorFromServer() {
        final var notFoundResponse = Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND)
                .header("content-type", "application/json")
                .build());
        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(notFoundResponse);

        final var user = githubClient.getUser("not found");

        StepVerifier.create(user)
                .expectError(WebClientResponseException.class)
                .verify();

        // OR another way to verify
        assertThatThrownBy(() -> githubClient.getUser("test").block())
                .isInstanceOf(WebClientResponseException.class);

        verify(exchangeFunction, times(2)).exchange(any());
    }

}
