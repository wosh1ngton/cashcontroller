import { Component, OnInit } from "@angular/core";
import { filter, forkJoin, map } from "rxjs";
import { AtivoBrapi } from "src/app/models/ativo-brapi.model";
import { AtivoCarteira } from "src/app/models/ativo-carteira.model";
import { AtivoService } from "src/app/services/ativo.service";
import { OperacaoRendaVariavelService } from "src/app/services/operacao-renda-variavel.service";

@Component({
    selector: 'app-carteira-acoes',
    templateUrl: './listar-carteira-acoes.component.html'
})
export class ListarCarteiraAcoesComponent implements OnInit {

    constructor(
        private operacaoRendaVariavelService: OperacaoRendaVariavelService,
        private ativoService: AtivoService
    ) { }

    carteira: AtivoCarteira[] = [];

    ngOnInit(): void {
        this.listarCarteira();
    }

    listarCarteira() {

        const ativosBrapi$ = this.ativoService.getAtivosBrapi();
        const carteira$ = this.operacaoRendaVariavelService.carteiraAcoes();

        forkJoin([ativosBrapi$, carteira$]).pipe(
            map(([ativosBrapi, carteira]) => {
                return carteira.map(c => {
                    let ativo = ativosBrapi.stocks.find((a: any) => a.stock === c.ativo.sigla);
                    return {
                        ...c,
                        cotacao: ativo.close,
                        oscilacaoDia: ativo.change,
                        valorMercado: ativo.close * c.custodia,
                        custo: c.custodia * c.precoMedio
                    }
                });
            }
            )).subscribe({
                next: (data) => this.carteira = data,
                complete: () => console.log('complete'),
                error: (err: Error) => console.log('Erro: ', err.message)
            });


    }
}