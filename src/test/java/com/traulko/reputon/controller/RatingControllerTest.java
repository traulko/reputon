package com.traulko.reputon.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @ParameterizedTest
    @MethodSource("correctDomainStream")
    public void testGetReviews_success(String domain) {
        webTestClient.get()
                .uri("/api/reviews/{domain}", domain)
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @MethodSource("incorrectDomainStream")
    public void testGetReviews_incorrectDomain(String domain) {
        webTestClient.get()
                .uri("/api/reviews/{domain}", domain)
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static Stream<Arguments> correctDomainStream() {
        return Stream.of(
                Arguments.of("monarchairgroup.com"),
                Arguments.of("gullwingmotor.com")
        );
    }

    private static Stream<Arguments> incorrectDomainStream() {
        return Stream.of(
                Arguments.of("1234234"),
                Arguments.of("invalid")
        );
    }
}