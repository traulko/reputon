package com.traulko.reputon.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebClientConfig {

    @Value("${trustpilot.api.base-url}")
    String baseURL;

    @Bean
    public WebClient trustpilotWebClient(WebClient.Builder webClientBuilder) {
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
        return webClientBuilder.baseUrl(baseURL)
                .exchangeStrategies(strategies)
                .build();
    }
}
