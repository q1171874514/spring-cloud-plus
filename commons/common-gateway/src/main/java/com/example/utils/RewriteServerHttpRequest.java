package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.util.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 重写HttpServletRequestWrapper
 *
 * @SuppressWarnings("unchecked")（ 抑制没有进行类型检查操作的警告）(https://www.cnblogs.com/perfei456/p/8962167.html)
 */
@SuppressWarnings("unchecked")
public class RewriteServerHttpRequest extends ServerHttpRequestDecorator {

    @Setter
    @Getter
    private Map<String, String> params = new HashMap<>();

    @Setter
    @Getter
    private HttpHeaders headers;

    @Setter
    @Getter
    private JSONObject bodyJson;

    private CachedBodyOutputMessage outputMessage;

    public RewriteServerHttpRequest(ServerWebExchange exchange) {
        super(exchange.getRequest());
        initParams();
        initHeader();
        initBodyJson();

    }

    private RewriteServerHttpRequest(RewriteServerHttpRequest rewriteServerHttpRequest, ServerHttpRequest request) {
        super(request);
        this.params = rewriteServerHttpRequest.params;
        this.headers = rewriteServerHttpRequest.headers;
        this.bodyJson = rewriteServerHttpRequest.bodyJson;
        this.outputMessage = rewriteServerHttpRequest.outputMessage;
    }

    /**
     * 初始化params
     */
    private void initParams(){
        params.clear();
        URI uri = getURI();
        String originalQuery = uri.getRawQuery();
        int startIndex = 0, endIndex = -1;
        String key;
        String value;
        if (StringUtils.hasText(originalQuery)) {
            while (endIndex < originalQuery.length()) {
                startIndex = endIndex + 1;
                endIndex = originalQuery.indexOf("=",startIndex);
                key = originalQuery.substring(startIndex, endIndex);
                startIndex = endIndex + 1;
                endIndex = originalQuery.indexOf("&",startIndex);
                if(endIndex == -1)
                    endIndex = originalQuery.length();
                value = originalQuery.substring(startIndex, endIndex);
                params.put(key,value);

            }
        }
    }



    /**
     * 载入params
     */
    public ServerHttpRequest loadParams() {
        URI uri = getURI();
        StringBuffer query = new StringBuffer("");
        params.entrySet().stream().forEach(entry -> {
            if(query.length() > 0) {
                query.append("&");
            }
            query.append(entry.getKey() + "=" + entry.getValue());
        });
        try {
            URI newUri = UriComponentsBuilder.fromUri(uri)
                    .replaceQuery(query.toString())
                    .build(true)
                    .toUri();

            ServerHttpRequest request = this.getDelegate().mutate().uri(newUri).build();
            return request;
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Invalid URI query: \"" + query.toString() + "\"");
        }
    }


    /**
     * 初始化body
     */
    public void initBodyJson() {
        // mediaType
        AtomicReference<String> requestBody = new AtomicReference<>("");
        Flux<DataBuffer> body =  super.getBody();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            requestBody.set(charBuffer.toString());
        });
        this.bodyJson = JSONObject.parseObject(requestBody.get());
    }

    /**
     * 载入body
     */
    public Mono<String> loadBodyJson(ServerRequest serverRequest) {
        // mediaType
        MediaType mediaType = this.headers.getContentType();
        Mono<String> modifiedBody = serverRequest.bodyToMono(JSONObject.class)
                .flatMap(body -> {
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                        return Mono.just(this.bodyJson.toJSONString());
                    }
                    return Mono.empty();
                });
        return modifiedBody;

    }

    public Mono<Void> build(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = this.loadParams();
//        exchange = exchange.mutate().request(request).build();
        Mono<String> modifiedBody = this.loadBodyJson(new DefaultServerRequest(exchange));
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        this.outputMessage = new CachedBodyOutputMessage(exchange, this.headers);
        RewriteServerHttpRequest rewRequest = new RewriteServerHttpRequest(this, request);
        return bodyInserter.insert(rewRequest.outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    return chain.filter(exchange.mutate().request(rewRequest).build());
                }));

    }    /**
     * 初始化header
     */
    private void initHeader(){
        this.headers = new HttpHeaders();
        headers.putAll(super.getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
    }

    /**
     * 载入header
     */
    private void loadHeader() {

    }

    @Override
    public HttpHeaders getHeaders() {
        long contentLength = this.headers.getContentLength();
        if (contentLength > 0) {
            this.headers.setContentLength(contentLength);
        } else {
            this.headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
        }
        return this.headers;
    }

    @Override
    public Flux<DataBuffer> getBody() {

        return outputMessage.getBody();
    }
}
