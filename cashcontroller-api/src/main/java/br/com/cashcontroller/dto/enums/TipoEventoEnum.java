package br.com.cashcontroller.dto.enums;

public enum TipoEventoEnum {
    DIVIDENDO("dividendo",1),
    JSCP("Juros sobre capital próprio",2),
    RENDIMENTO("Rendimentos", 3),
    RECEBIVEL("Juros certificado recebíveis", 4),
    TESOURO("Juros Tesouro Direto", 5);
    TipoEventoEnum(String descricao, int id) {
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
