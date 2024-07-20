import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfiguracaoRoutingModule } from './configuracao-routing.module';
import { ConfiguracaoComponent } from './configuracao/configuracao.component';
import { ListarAtivosComponent } from './listar-ativos/listar-ativos.component';
import { CadastroAtivoComponent } from './cadastro-ativo/cadastro-ativo.component';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    ConfiguracaoComponent,
    ListarAtivosComponent,
    CadastroAtivoComponent
  ],
  imports: [
    CommonModule,
    PrimengModule,    
    FormsModule,    
    ConfiguracaoRoutingModule
  ]
})
export class ConfiguracaoModule { }
