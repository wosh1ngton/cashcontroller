package br.com.cashcontroller.external.client;

import br.com.cashcontroller.external.dto.tesouro.TesouroDiretoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TesouroDiretoApiClient {

    private final WebClient webClientTesouro;
    @Autowired
    public TesouroDiretoApiClient(@Qualifier("tesouroApiClient") WebClient webClientTesouro) {
        this.webClientTesouro = webClientTesouro;
    }

    public TesouroDiretoDTO getTitulosTesouroDireto() {
        return webClientTesouro.get()
                .uri("/json/br/com/b3/tesourodireto/service/api/treasurybondsinfo.json")
                .retrieve()
                .bodyToMono(TesouroDiretoDTO.class)
                .block();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return Mono.just(clientResponse);
        });
    }

//    public TesouroDiretoDTO getTitulosTesouroDireto() {
//        return webClientTesouro.get()
//                .uri("/json/br/com/b3/tesourodireto/service/api/treasurybondsinfo.json")
//                .retrieve()
//                .bodyToMono(String.class)
//                .doOnNext(System.out::println)  // Log the raw JSON response
//                .map(this::convertToDto)
//                .block();  // Blocking to get the response (only for demonstration)
//    }

//    private TesouroDiretoDTO convertToDto(String json) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            return objectMapper.readValue(json, TesouroDiretoDTO.class);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert JSON to DTO", e);
//        }
//    }
}
