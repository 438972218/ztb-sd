package com.xdcplus.scheduling.common.swagger;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * SwaggerHeaderFilter过滤器
 *
 * @author Rong.Jia
 * @date 2021/07/28 08:53:37
 */
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    private final SwaggerGatewayProperties swaggerGatewayProperties;

    public SwaggerHeaderFilter(SwaggerGatewayProperties swaggerGatewayProperties) {
        this.swaggerGatewayProperties = swaggerGatewayProperties;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, swaggerGatewayProperties.getSwaggerApiDocs())) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(swaggerGatewayProperties.getSwaggerApiDocs()));
            String[] basePathArr = {basePath};
            ServerHttpRequest newRequest = request.mutate().header(swaggerGatewayProperties.getHeaderName(),
                    basePathArr).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }
}