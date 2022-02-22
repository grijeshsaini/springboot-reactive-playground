package com.grijesh.playground.service;

import com.grijesh.playground.client.GithubClient;
import com.grijesh.playground.dto.UserDetails;
import com.grijesh.playground.repository.RedisDataRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final GithubClient githubClient;
    private final RedisDataRepository repository;

    public UserService(GithubClient githubClient, RedisDataRepository repository) {
        this.githubClient = githubClient;
        this.repository = repository;
    }

    public Mono<UserDetails> getUser(String shortName) {
        return repository.find(shortName)
                .switchIfEmpty(
                        Mono.defer(() -> githubClient.getUser(shortName)
                                .map((user) -> new UserDetails(user.login(), user.avatarUrl()))
                                .flatMap(userDetails -> repository.save(userDetails)
                                        .map(isSaved -> {
                                            if (!isSaved) {
                                                throw new RuntimeException("Value already exist");
                                            }
                                            return userDetails;
                                        })))
                );
    }

}
