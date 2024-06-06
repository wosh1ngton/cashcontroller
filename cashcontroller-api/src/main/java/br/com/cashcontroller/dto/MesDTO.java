package br.com.cashcontroller.dto;

import lombok.*;

import java.text.DateFormatSymbols;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MesDTO {

    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    private Integer mesInteiro;
    private String mesString;
    private Integer ano;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String mesComAno;

    public MesDTO(Integer mesInteiro, Integer ano) {
        this.mesInteiro = mesInteiro;
        this.ano = ano;
        this.mesString = getMonthName(mesInteiro);
    }

    private String getMonthName(int month) {
        return new DateFormatSymbols(LOCALE_PT_BR).getMonths()[month - 1];
    }

    public String getMesComAno() {
        if (mesInteiro != null && ano != null) {
            mesComAno = mesInteiro + "/" + ano;
            return mesComAno;
        } else {
            return "dados incompletos";
        }
    }


    public void setMesComAno(String mesComAno) {
        this.mesComAno = mesComAno;
    }
}
