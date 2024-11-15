import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'validar-exclusao',
  templateUrl: './validar-exclusao.component.html',
  providers: [ConfirmationService, MessageService],
})
export class ValidarExclusaoComponent  {
  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService,    
  ) {}  

  @Input('entity') entity: any;
  @Input('key') key: any;
  @Input('service') service: any;
  @Output() entityDeleted: EventEmitter<void> = new EventEmitter();
  
  validarExclusao(event: Event) {
    
    this.confirmationService.confirm({
      key: this.key,
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
       this.service.excluir(this.entity.id).subscribe((res:any) => {
            this.service.getAll().subscribe((res: any) => {
                this.entityDeleted.emit();
            });
       });
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmado',
          detail: 'Confirmada a exclusão',
        });
      },
      reject: () => {     
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
