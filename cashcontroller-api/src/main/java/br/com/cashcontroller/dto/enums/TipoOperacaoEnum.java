package br.com.cashcontroller.dto.enums;

public enum TipoOperacaoEnum {
    COMPRA("compra",1),
    VENDA("venda",2),
    DESDOBRAMENTO("desdobramento", 3),
    BONIFICACAO("bonificacao", 4),
    GRUPAMENTO("grupamento", 5);
    TipoOperacaoEnum(String descricao, int id) {
        this.descricao = descricao;
        this.id = id;
    }
    private final String descricao;
    private final int id;

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }
}
