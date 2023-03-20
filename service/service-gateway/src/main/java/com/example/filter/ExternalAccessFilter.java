package com.example.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.utils.RewriteServerHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 *外部访问拦截并且配置
 */
//@Order(-1)
//@Component注解，将类注入到Spring容器中，作为Bean
@Component
@Slf4j
public class ExternalAccessFilter implements GlobalFilter, Ordered {
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

////        // mediaType
//        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
////        // read & modify body
//        ServerRequest serverRequest = new DefaultServerRequest(exchange);
//        Mono<String> modifiedBody = serverRequest.bodyToMono(JSONObject.class)
//                .flatMap(body -> {
//                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
//                        System.out.println("------------------------------------");
//                        System.out.println(body);
//                        JSONObject jsonObject = body;
//                        jsonObject.put("test123456", "5");
//                        // 对原先的body进行修改操作
//                        String newBody = jsonObject.toJSONString();
//                        return Mono.just(newBody);
//                    } else {
//                        System.out.println("------------------------------------");
//                        System.out.println("tttttttttt");
//                    }
//                    return Mono.empty();
//                });
//        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
//        HttpHeaders headers = new HttpHeaders();
//        headers.putAll(exchange.getRequest().getHeaders());
//        headers.remove(HttpHeaders.CONTENT_LENGTH);
//        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
//        return bodyInserter.insert(outputMessage, new BodyInserterContext())
//                .then(Mono.defer(() -> {
//                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
//                            exchange.getRequest()) {
//                        @Override
//                        public HttpHeaders getHeaders() {
//                            long contentLength = headers.getContentLength();
//                            HttpHeaders httpHeaders = new HttpHeaders();
//                            httpHeaders.putAll(super.getHeaders());
//                            if (contentLength > 0) {
//                                httpHeaders.setContentLength(contentLength);
//                            } else {
//                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
//                            }
//                            return httpHeaders;
//                        }
//
//                        @Override
//                        public Flux<DataBuffer> getBody() {
//                            return outputMessage.getBody();
//                        }
//                    };
//                    return chain.filter(exchange.mutate().request(decorator).build());
//                }));


        RewriteServerHttpRequest rewriteServerHttpRequest = new RewriteServerHttpRequest(exchange);
        rewriteServerHttpRequest.getBodyJson().put("bodyaa","1");
        rewriteServerHttpRequest.getParams().put("paramsaa","1");
        rewriteServerHttpRequest.getHeaders().put("Headersaa", new ArrayList<String>(){{add("1");}});
        return rewriteServerHttpRequest.build(exchange,chain);
//        return chain.filter(exchange);

    }

    //表示过滤器的顺序，return 的值越小，优先级越高
    //也可以用@Order(-1)代替
    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {

            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }


}

