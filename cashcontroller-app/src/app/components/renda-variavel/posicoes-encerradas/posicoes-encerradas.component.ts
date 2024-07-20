import { Component, OnInit } from '@angular/core';
import { OperacaoRendaVariavelService } from 'src/app/services/operacao-renda-variavel.service';

@Component({
  selector: 'app-posicoes-encerradas',    
  templateUrl: './posicoes-encerradas.component.html',
  styleUrl: './posicoes-encerradas.component.css'
})
export class PosicoesEncerradasComponent implements OnInit {

  constructor(
    private operacaoService: OperacaoRendaVariavelService
  ) {}

  resultadoAcumulado: number = 0;
  posicoes : any[] = [];

  ngOnInit(): void {    
    this.operacaoService.posicoesEncerradas().subscribe(
        res => {this.posicoes = res,

          console.log(this.posicoes)
        }
  )}

  getResultadoPosicoesEncerradas(): number {
    return this.posicoes.reduce((total, posicao ) => total + posicao.resultadoAtivo, 0)
  }

  


}
