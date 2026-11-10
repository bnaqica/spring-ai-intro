package com.bnaqica.spring_ai_intro.models;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record CapitalWithInfoResponse(
        @JsonPropertyDescription("This is the city name") String city,
        @JsonPropertyDescription("This is the population of the city") String population,
        @JsonPropertyDescription("This is the region of the city") String region,
        @JsonPropertyDescription("This is the language of the city") String language,
        @JsonPropertyDescription("This is the currency of the city") String currency) {
}
