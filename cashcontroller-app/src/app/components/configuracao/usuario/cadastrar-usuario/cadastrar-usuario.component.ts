import { Component } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-cadastrar-usuario',
  templateUrl: './cadastrar-usuario.component.html',
  styleUrl: './cadastrar-usuario.component.css',
})
export class CadastrarUsuarioComponent {
  username: string = '';
  novaSenha: string = '';
  conferenciaSenha: string = '';

  constructor(
    private authService: AuthService,
    private messageService: MessageService,
    private dialogRef: DynamicDialogRef
  ) {}

  close() {
    this.dialogRef.close();
  }

  confereSenha() {
    return this.novaSenha === this.conferenciaSenha;
  }

  formularioValido() {
    return this.username.trim().length > 0
      && this.novaSenha.length > 0
      && this.confereSenha();
  }

  save(form: any) {
    if (form.invalid || !this.formularioValido()) return;

    this.authService.register(this.username, this.novaSenha).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Sucesso',
          detail: 'Usuário cadastrado',
        });
        this.dialogRef.close(true);
      },
      error: (e) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Erro',
          detail: e?.error?.message || e?.error || 'Falha ao cadastrar usuário',
        });
      },
    });
  }
}
