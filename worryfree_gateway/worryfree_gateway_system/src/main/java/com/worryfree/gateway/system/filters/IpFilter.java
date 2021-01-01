package com.worryfree.gateway.system.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Component
public class IpFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取 request
        ServerHttpRequest request = exchange.getRequest();

        //获取 ip 地址
        InetSocketAddress remoteAddress = request.getRemoteAddress();

        //输出
//        System.out.println("经过第一个过滤器 ip 地址：" + remoteAddress.getHostName());

        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
