package br.com.cashcontroller.external.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.SocketException;

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
        return WebClient.builder().baseUrl("https://www.tesourodireto.com.br").build();
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
