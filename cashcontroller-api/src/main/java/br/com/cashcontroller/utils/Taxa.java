package br.com.cashcontroller.utils;

public final class Taxa {

    //taxa antiga, pré 02/02/2021
    public static double TAXA_LIQUIDACAO_ANTIGA = 0.0275;

    //alterado a partir de 02/02/2021
    public static double TAXA_LIQUIDACAO_B3 = 0.0250;
    public static double TAXA_NEGOCIACAO_B3 = 0.005;
    public static double TAXA_EMOLUMENTOS = 0.005;
    public static double IMPOSTOS = 0.9035;
    public static double TAXA_OPERACIONAL_XP = 0.059;

    public static double LIMITE_IR = 20000;
    public static double ALIQUOTA_IR = 0.15;
    public static double ALIQUOTA_JSCP = 0.15;


    public static double convertAnnualToMonthlyInterestRate(double annualRate) {
        annualRate = annualRate / 100;
        return Math.pow(1 + annualRate, 1.0 / 12.0) - 1;
    }

}
