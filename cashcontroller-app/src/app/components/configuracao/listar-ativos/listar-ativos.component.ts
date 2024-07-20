import { Component } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastroAtivoComponent } from '../cadastro-ativo/cadastro-ativo.component';

@Component({
  selector: 'app-listar-ativos', 
  templateUrl: './listar-ativos.component.html',
  styleUrl: './listar-ativos.component.css',
  providers: [DialogService]
})
export class ListarAtivosComponent {

  constructor(public dialogService: DialogService) {

  }
  ref: DynamicDialogRef | undefined;

  showAtivoDialog() {
    this.ref = this.dialogService.open(CadastroAtivoComponent, {
      header: 'Cadastro de Ativo',
      width: '50vw',
      modal: true,
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw',
      },
      data: {
       
      },
    });
  }

}
