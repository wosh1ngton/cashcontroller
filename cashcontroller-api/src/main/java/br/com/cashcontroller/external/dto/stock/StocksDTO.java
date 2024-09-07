package br.com.cashcontroller.external.dto.stock;

import lombok.Data;

@Data
public class StocksDTO {

    private String stock;
    private String name;
    private Double close;
    private Double change;
    private Double volume;
    private double market_cap;
    private String logo;
    private String sector;
    private String type;
}