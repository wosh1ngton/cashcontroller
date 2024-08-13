package br.com.cashcontroller.external.dto.tesouro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TituloTesouroDTO {
    private int cd;
    private String nm;
    private String featrs;
    private String mtrtyDt;
    private double minInvstmtAmt;
    private double untrInvstmtVal;
    private String invstmtStbl;
    private boolean semiAnulIntrstInd;
    private String rcvgIncm;
    private double anulInvstmtRate;
    private double anulRedRate;
    private double minRedQty;
    private double untrRedVal;
    private double minRedVal;
    private String isinCd;
    private FinIndxsDTO FinIndxs;
    private String wdwlDt;
    private String convDt;
    private String BusSegmt;
    private int amortQuotQty;

}
