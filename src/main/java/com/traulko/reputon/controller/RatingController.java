package com.traulko.reputon.controller;

import com.traulko.reputon.record.RatingResponse;
import com.traulko.reputon.service.RatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

    RatingService ratingService;

    @GetMapping("/{domain}")
    public ResponseEntity<Mono<RatingResponse>> getReviews(@PathVariable final String domain) {
        return ResponseEntity.ok(ratingService.getRating(domain));
    }
}
