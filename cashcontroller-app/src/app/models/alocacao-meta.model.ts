export type CategoriaAlocacao = 'ACOES' | 'FIIS' | 'RENDA_FIXA' | 'RENDA_INTERNACIONAL';

export interface AlocacaoMeta {
    categoria: CategoriaAlocacao;
    descricao: string;
    percentual: number;
}
