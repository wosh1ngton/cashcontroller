import { Component, Input, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastrarAtivoCarteiraComponent } from '../cadastrar-ativo-carteira/cadastrar-ativo-carteira.component';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-listar-ativos-carteira-principal',    
  templateUrl: './listar-ativos-carteira-principal.component.html',
  styleUrl: './listar-ativos-carteira-principal.component.css',
  providers: [DialogService, ConfirmationService, MessageService]
})
export class ListarAtivosCarteiraPrincipalComponent implements OnInit {

  dialogRef: DynamicDialogRef | undefined;
  @Input('classeAtivo') classeAtivo = new Input();
  activeIndex: number = 0;  

  constructor(
    private dialogService: DialogService, 
    private ativoCarteiraService: AtivoCarteiraService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService) {

    }
  
  carteira: AtivoCarteira[] = [];
  carteiraAcoes: AtivoCarteira[] = [];
  carteiraFiis: AtivoCarteira[] = [];
  carteiraRendaFixa: AtivoCarteira[] = [];

  ngOnInit(): void {    
    this.buscarCarteiraAcoes();
    this.buscarCarteiraFiis();  
    this.buscarCarteiraRendaFixa();
  }

  colsCarteiraAtivo = [
    { field: 'ativo', header: 'Ativo', type: 'object'},
    { field: 'custodia', header: 'Custódia', type: 'number'},
    { field: 'custo', header: 'Custo', type: 'number'},
    { field: 'valorMercado', header: 'Valor de Mercado', type: 'number'},
    { field: 'percentual', header: 'Percentual', type: 'number'},
    { field: 'precoMedio', header: 'Preço Médio', type: 'number'},
  ]

  public buscarCarteiraPrincipal() {
    this.ativoCarteiraService.getAtivosCarteira().subscribe(
      (res) => this.carteira = res
    );
  }


  public buscarCarteiraAcoes() {
    this.ativoCarteiraService.getAtivosCarteira().subscribe(
      (res) => {        
        this.carteiraAcoes = res;
       // this.calcularRentabilidade();
      }
    );
  }


  public buscarCarteiraFiis() {
    this.ativoCarteiraService.getAtivosCarteiraFiis().subscribe(
      (res) => {        
        this.carteiraFiis = res;
        //this.calcularRentabilidade();
      }
    );
  }

  public buscarCarteiraRendaFixa() {
    this.ativoCarteiraService.getAtivosCarteiraRendaFixa().subscribe(
      (res) => {        
        this.carteiraRendaFixa = res;
        //this.calcularRentabilidade();
      }
    );
  }  


}
