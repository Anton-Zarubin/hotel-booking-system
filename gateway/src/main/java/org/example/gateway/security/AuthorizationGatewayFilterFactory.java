package org.example.gateway.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizationGatewayFilterFactory.Config> {

    public AuthorizationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var roles = getRolesHeader(request);
            if(!((roles != null) && Arrays.asList(roles.split(", ")).contains(config.getRole()))) {
                var response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    private String getRolesHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("roles").get(0);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Config {
        private String role;
    }
}
