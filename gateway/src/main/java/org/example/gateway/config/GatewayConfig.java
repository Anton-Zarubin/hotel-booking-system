package org.example.gateway.config;

import lombok.RequiredArgsConstructor;
import org.example.gateway.security.AuthenticationFilter;
import org.example.gateway.security.AuthorizationGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    private final AuthorizationGatewayFilterFactory authorizationGatewayFilterFactory;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "auth_route", r -> r.path("/auth-service/**")
                                .filters(f -> f.filter(authenticationFilter))
                                .uri("lb://AUTH-SERVICE")
                )
                .route(
                        "booking_route", r -> r.path("/booking-service/**")
                                .filters(f -> f.filter(authenticationFilter))
                                .uri("lb://BOOKING-SERVICE")
                )
                .route(
                        "hotel_route", r -> r.path("/hotel-service/v3/api-docs",
                                        "/hotel-service/hotels/rate/**", "/hotel-service/hotels/view/**",
                                        "/hotel-service/rooms/view/**")
                                .filters(f -> f.filter(authenticationFilter))
                                .uri("lb://HOTEL-SERVICE")
                )
                .route(
                        "hotel_route", r -> r.path("/hotel-service/**")
                                .filters(f -> f.filter(authenticationFilter)
                                        .filter(authorizationGatewayFilterFactory.apply(new AuthorizationGatewayFilterFactory.Config("ROLE_ADMIN"))))
                                .uri("lb://HOTEL-SERVICE")
                )
                .build();
    }
}
