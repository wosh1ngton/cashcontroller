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

  posicoes : any[] = [];

  ngOnInit(): void {
    console.log('oi')
    this.operacaoService.posicoesEncerradas().subscribe(
        res => this.posicoes = res
  )}


}
