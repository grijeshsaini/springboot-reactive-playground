package com.grijesh.playground.service;

import com.grijesh.playground.client.GithubClient;
import com.grijesh.playground.dto.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final GithubClient githubClient;

    public UserService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Mono<UserDetails> getUser(String shortName) {
        return githubClient.getUser(shortName)
                .map((user) -> new UserDetails(user.login(), user.avatarUrl()));
    }

}
