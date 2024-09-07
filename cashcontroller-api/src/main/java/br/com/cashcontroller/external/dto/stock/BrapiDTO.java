package br.com.cashcontroller.external.dto.stock;

import lombok.Data;

@Data
public class BrapiDTO {

    private IndexDTO[] indexes;
    private StocksDTO[] stocks;
    private String[] availableSectors;
    private String[] availableStockTypes;

}
