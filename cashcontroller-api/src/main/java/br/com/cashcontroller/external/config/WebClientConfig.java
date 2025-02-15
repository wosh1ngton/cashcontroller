package br.com.cashcontroller.external.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.net.SocketException;
import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean(name = "brasilApiClient")
    public WebClient webClientBrasilApi() {
        return WebClient.builder().baseUrl("https://brasilapi.com.br").build();
    }

    @Retryable(
            value = { SocketException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Bean("tesouroApiClient")
    public WebClient webClientTesouro() {
        return WebClient.builder()
                .baseUrl("https://www.tesourodireto.com.br")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                        .responseTimeout(Duration.ofSeconds(30))
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(30))
                                .addHandlerLast(new WriteTimeoutHandler(30)))))
                .build();
    }

    @Bean("bcApiClient")
    public WebClient webClientBacen() {
        return WebClient.builder().baseUrl("https://api.bcb.gov.br").build();
    }

    @Bean("brapiClient")
    public WebClient webClientBrapi() {
        return WebClient.builder().baseUrl("https://brapi.dev/api").build();
    }
}
