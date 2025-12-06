package br.com.cashcontroller.dto.enums;

public enum SubclasseAtivoEnum {
    FII("FII",1),
    ACAO("ACAO",2),
    TESOURO_DIRETO("TESOURO_DIRETO",3),
    CREDITO_BANCARIO("CREDITO_BANCARIO",4),
    CREDITO_PRIVADO("CREDITO_PRIVADO",5),
    PREVIDENCIA("PREVIDENCIA",6);

    SubclasseAtivoEnum(String descricao, int id) {
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
