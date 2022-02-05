package com.grijesh.playground.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(String login, @JsonProperty("avatar_url") String avatarUrl) {
}
