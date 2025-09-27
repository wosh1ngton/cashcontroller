import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { AlterarSenha } from 'src/app/models/novo-usuario.model';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-form-usuario',  
  templateUrl: './form-usuario.component.html',
  styleUrl: './form-usuario.component.css',
  
})
export class FormUsuarioComponent implements OnInit {


    user: AlterarSenha = new AlterarSenha();
    conferenciaSenha: string = "";
    idUsuario: number | undefined;
    constructor(
      private configDialog: DynamicDialogConfig,
      private userService: UserService,
      private messageService: MessageService,
      private dialogRef: DynamicDialogRef
    ) {}

    ngOnInit(): void {
      if(this.configDialog.data) {
        this.idUsuario = this.configDialog.data.id;      
        
      }
    }

    close() {
      this.dialogRef.close();
    }

    save(form: any) {

      if(form.invalid) return;      
      this.userService.alterarSenha(this.user).subscribe({
        next: (result: any) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Successo',
          detail: result,
        });
        this.close();
      },
      error: (e) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: e.error,
        });
       
      }
    });
    }

    confereSenha() {
      return this.user.novaSenha === this.conferenciaSenha;
    }
}
