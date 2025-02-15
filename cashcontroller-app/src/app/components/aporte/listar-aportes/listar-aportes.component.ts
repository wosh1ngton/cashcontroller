import { Component, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { CadastrarAporteComponent } from '../cadastrar-aporte/cadastrar-aporte.component';
import { Aporte } from 'src/app/models/aporte.model';
import { AporteService } from 'src/app/services/aporte.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-listar-aportes', 
  templateUrl: './listar-aportes.component.html',
  styleUrl: './listar-aportes.component.css',
  providers: [DialogService, ConfirmationService]

})
export class ListarAportesComponent implements OnInit {

  dialogRef : DynamicDialogRef | undefined;  
  aportes: Aporte[] = [];
  cadastrarAporte : CadastrarAporteComponent | undefined;
  CadastrarAporteComponent = CadastrarAporteComponent;
  ngOnInit(): void {
    console.log('oi')
    this.listarAportes();
  }

  constructor(public dialogService: DialogService,
              public confirmationService: ConfirmationService,
              public aporteService: AporteService,
              private messageService: MessageService
  ) {}
  
  showDialogCadastrarAporte() {
      this.dialogRef = this.dialogService.open(CadastrarAporteComponent, {
        header: 'Cadastro de Aporte',
        width: '50vw',
        modal: true,

      });

      this.dialogRef.onClose.subscribe((val) => {
        this.listarAportes();
      })
  }

  listarAportes() {
    this.aporteService.getAll()
      .subscribe((res: any) => {
        this.aportes = res;
      })
  }

  editar(id: number) {

  }

  excluirAtivo(id: number) {
    return this.aporteService.excluir(id);   
  }
  validarExclusaoAtivo(ativo: any, event: Event, key: string) {
    console.log(ativo)
    this.confirmationService.confirm({
      key: key,
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja excluir esta operação?',
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',      
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
      //  this.excluirAtivo(ativo.id).subscribe(() => this.listarAtivosRendaFixa());
      //   this.messageService.add({
      //     severity: 'info',
      //     summary: 'Confirmado',
      //     detail: 'Confirmada a exclusão',
      //   });
      },
      reject: () => {
      //  this.filterData();
        this.messageService.add({
          severity: 'error',
          summary: 'Rejeitado',
          detail: 'Cancelada a exclusão',
          life: 3000,
        });
        
      },
    });
  }
}
