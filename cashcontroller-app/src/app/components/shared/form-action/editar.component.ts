import { Component, EventEmitter, Input, Output } from "@angular/core";
import { ConfirmationService, MessageService } from "primeng/api";
import { DialogService, DynamicDialogRef } from "primeng/dynamicdialog";

@Component({
    selector: 'editar',
    templateUrl: './editar.component.html',
    providers: [ConfirmationService, MessageService, DialogService]
})
export class EditarComponent {

    constructor(
        private confirmationService: ConfirmationService,
        private messageService: MessageService,    
        private dialogService: DialogService,    
      ) {}  

    ref : DynamicDialogRef | undefined;
    @Input('entity') entity: any;
    @Input('componente') componente: any;
    @Input('service') service: any;
    @Output() entityDeleted: EventEmitter<void> = new EventEmitter();
    
    
    editarEntidade(event: any) {
        console.log(event)
        this.ref = this.dialogService.open(this.componente, {
          header: 'Editar ',
          width: '50vw',
          modal: true,
          breakpoints: {
            '960px': '75vw',
            '640px': '90vw',
          },
          data: {
            isEdit: true,
            rowData: this.entity,
          },
        });
    
        this.ref.onClose.subscribe((res) => this.service.getAll())
      }
}