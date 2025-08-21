package com.training.moneybags.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient usersWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:9090/api/users/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // Relay incoming Bearer token to downstream call
                .filter((request, next) -> {
                    String token = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                            .filter(JwtAuthenticationToken.class::isInstance)
                            .map(JwtAuthenticationToken.class::cast)
                            .map(t -> t.getToken().getTokenValue())
                            .orElse(null);

                    ClientRequest withAuth = ClientRequest.from(request)
                            .headers(h -> { if (token != null) h.setBearerAuth(token); })
                            .build();

                    return next.exchange(withAuth);
                })
                .build();
    }
}
