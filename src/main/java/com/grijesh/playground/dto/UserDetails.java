package com.grijesh.playground.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDetails(@JsonProperty("short_name") String shortName, @JsonProperty("avatar_url") String avatarUrl) {
}
