package com.traulko.reputon.service;

import com.traulko.reputon.record.RatingResponse;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<RatingResponse> getRating(@NotNull String domain);
}
