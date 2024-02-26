package com.traulko.reputon.record;

import jakarta.validation.constraints.NotNull;

public record RatingResponse(@NotNull Long reviewsCount, @NotNull Double rating) {}
