package br.com.cashcontroller.external.dto.selic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.YearMonth;

@Data
public class IPCAMesDTO {
    String data;
    Double valor;

    @JsonIgnoreProperties
    YearMonth dataYearMonth;
}
