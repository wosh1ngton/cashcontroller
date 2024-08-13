package br.com.cashcontroller.external.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean(name = "brasilApiClient")
    public WebClient webClientBrasilApi() {
        return WebClient.builder().baseUrl("https://brasilapi.com.br").build();
    }

    @Bean("tesouroApiClient")
    public WebClient webClientTesouro() {
        return WebClient.builder().baseUrl("https://www.tesourodireto.com.br").build();
    }

    @Bean("bcApiClient")
    public WebClient webClientBacen() {
        return WebClient.builder().baseUrl("https://api.bcb.gov.br").build();
    }
}
