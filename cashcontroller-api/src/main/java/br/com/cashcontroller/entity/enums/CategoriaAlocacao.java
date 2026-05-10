package br.com.cashcontroller.entity.enums;

public enum CategoriaAlocacao {
    ACOES("Ações", 30.0),
    FIIS("Fiis", 30.0),
    RENDA_FIXA("Renda Fixa", 30.0),
    RENDA_INTERNACIONAL("Renda Internacional", 10.0);

    private final String descricao;
    private final double percentualPadrao;

    CategoriaAlocacao(String descricao, double percentualPadrao) {
        this.descricao = descricao;
        this.percentualPadrao = percentualPadrao;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPercentualPadrao() {
        return percentualPadrao;
    }
}
