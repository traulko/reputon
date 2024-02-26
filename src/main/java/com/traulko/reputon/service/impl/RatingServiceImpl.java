package com.traulko.reputon.service.impl;

import com.traulko.reputon.exception.HtmlParsingException;
import com.traulko.reputon.record.RatingResponse;
import com.traulko.reputon.service.RatingService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.traulko.reputon.exception.util.ExceptionHandlerUtil.processClientException;
import static com.traulko.reputon.exception.util.ExceptionHandlerUtil.processServerException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class RatingServiceImpl implements RatingService {

    @Qualifier("trustpilotWebClient")
    final WebClient webClient;

    @Value("${trustpilot.api.domain-endpoint}")
    String domainEndpoint;

    @Value("${trustpilot.api.rating-element-name}")
    String ratingElementName;

    public RatingServiceImpl(@Qualifier("trustpilotWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @Cacheable(value = "trustpilotReviews", key = "#domain")
    public Mono<RatingResponse> getRating(@NotNull String domain) {
        return webClient.get()
                .uri(domainEndpoint, domain)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> processClientException(domain, response))
                .onStatus(HttpStatusCode::is5xxServerError, response -> processServerException(domain, response))
                .bodyToMono(String.class)
                .flatMap(this::buildResponseFromHTML);
    }

    private Mono<RatingResponse> buildResponseFromHTML(String html) {
        try {
            Element businessUnit = Jsoup.parse(html).getElementById(ratingElementName);
            return Mono.just(new RatingResponse(parseReviewAmountFromElement(businessUnit), parseRatingFromElement(businessUnit)));
        } catch (Exception e) {
            log.error(String.format("HTML cannot be processed, cause: %s", e.getMessage()));
            return Mono.error(new HtmlParsingException("HTML cannot be processed"));
        }
    }

    /**
     * an example of the element structure is presented in the file business-util-example.xml
     * @param businessUnit element
     * @return review amount
     */
    private Double parseRatingFromElement(Element businessUnit) {
        return Double.valueOf(businessUnit.child(2).firstElementChild().lastElementChild().text());
    }

    /**
     * @param reviewAmount string value ex. 1,492
     * @return long value ex. 1492
     */
    private Long reviewAmountToLong(String reviewAmount) {
        return Long.valueOf(reviewAmount.replace(",", ""));
    }

    /**
     * an example of the element structure is presented in the file business-util-example.xml
     * @param businessUnit element
     * @return review rating
     */
    private Long parseReviewAmountFromElement(Element businessUnit) {
        String rawReviewCount = businessUnit.child(1).child(0).textNodes().get(0).text();
        return reviewAmountToLong(rawReviewCount);
    }
}
