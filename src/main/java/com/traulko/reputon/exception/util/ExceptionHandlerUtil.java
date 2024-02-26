package com.traulko.reputon.exception.util;

import com.traulko.reputon.exception.ClientInternalException;
import com.traulko.reputon.exception.ServerInternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class ExceptionHandlerUtil {
    public static Mono<Throwable> processServerException(String domain, ClientResponse response) {
        log.error(String.format("Status code from server is - %d", response.statusCode().value()));
        return Mono.error(new ServerInternalException(String.format(
                "Domain %s cannot be processed, status code is %d", domain, response.statusCode().value())));
    }

    public static Mono<Throwable> processClientException(String domain, ClientResponse response) {
        log.error(String.format("Status code from server is - %d", response.statusCode().value()));
        return Mono.error(new ClientInternalException(String.format(
                "Domain %s cannot be processed, status code is %d", domain, response.statusCode().value())));
    }
}
