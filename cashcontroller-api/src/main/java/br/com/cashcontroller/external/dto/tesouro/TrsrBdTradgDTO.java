package br.com.cashcontroller.external.dto.tesouro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrsrBdTradgDTO {

    @JsonProperty("TrsrBd")
    private TituloTesouroDTO TrsrBd;
}
