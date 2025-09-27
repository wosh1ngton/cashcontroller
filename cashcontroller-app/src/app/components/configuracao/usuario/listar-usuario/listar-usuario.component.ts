import { Component, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Usuario } from 'src/app/models/usuario.model';
import { UserService } from 'src/app/services/user.service';
import { FormUsuarioComponent } from '../form-usuario/form-usuario.component';

@Component({
  selector: 'app-listar-usuario', 
  templateUrl: './listar-usuario.component.html',
  styleUrl: './listar-usuario.component.css',
  providers: [DialogService]
})
export class ListarUsuarioComponent implements OnInit {

  usuarios: Usuario[] = [];
  userDialog: DynamicDialogRef | undefined;

  constructor(
    public userService: UserService,    
    private dialogService: DialogService
  ) {}
    
  ngOnInit(): void {
    this.getUsuarios();  
  }

  getUsuarios() {
    this.userService.getAll().subscribe((res) => {
      this.usuarios = res.map(user => new Usuario(user.id, user.username, user.password));
      console.log(this.usuarios)
    });
  }

  editarUsuario(id: number) {
    this.userDialog = this.dialogService.open(FormUsuarioComponent, {
      header: 'Alterar Senha',
      width: '50%',
      contentStyle: { overflow: 'auto' },            
      data: { id: id },
    });
  }
}
