package com.grijesh.playground.client;

import com.grijesh.playground.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GithubClient {

    private final WebClient webClient;

    private final String githubBaseUrl;

    @Autowired
    public GithubClient(WebClient webClient, @Value("${github.base_url:https://api.github.com}") String githubBaseUrl) {
        this.webClient = webClient;
        this.githubBaseUrl = githubBaseUrl;
    }

    public Mono<User> getUser(String login) {
        return webClient.get()
                .uri(githubBaseUrl + "/users/{login}", login)
                .retrieve()
                .onStatus(HttpStatus::isError, ClientResponse::createException)
                .bodyToMono(User.class);
    }

}
