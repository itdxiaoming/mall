package com.worryfree.gateway.system.filters;

import com.worryfree.gateway.system.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        获取 request
        ServerHttpRequest request = exchange.getRequest();
//        获取 response
        ServerHttpResponse response = exchange.getResponse();
//        获取 url
        String path = request.getURI().getPath();
//        判断 login 放行
        String loginPath = "/admin/login";
        if (path.contains(loginPath)){
            return chain.filter(exchange);
        }
//        获取 headers
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
//        判断header 中是否带有 token
        try {
            JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
//        判断 token 是否合法




        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
