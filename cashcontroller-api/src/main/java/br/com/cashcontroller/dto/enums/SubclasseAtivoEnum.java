package br.com.cashcontroller.dto.enums;

public enum SubclasseAtivoEnum {
    FII("FII",1),
    ACAO("ACAO",2);

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
