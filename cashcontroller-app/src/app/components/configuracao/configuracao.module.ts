import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfiguracaoRoutingModule } from './configuracao-routing.module';
import { ConfiguracaoComponent } from './configuracao/configuracao.component';
import { ListarAtivosComponent } from './listar-ativos/listar-ativos.component';
import { CadastroAtivoComponent } from './cadastro-ativo/cadastro-ativo.component';
import { PrimengModule } from 'src/app/primeng/primeng.module';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CalendarModule } from 'primeng/calendar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { SharedExtendedModule } from "../../shared-modules/shared.module";
import { IndicesComponent } from './indices/indices.component';
import { CadastrarIndiceComponent } from './indices/cadastrar-indice/cadastrar-indice.component';
import { DataView } from 'primeng/dataview';

@NgModule({
  declarations: [
    ConfiguracaoComponent,
    ListarAtivosComponent,
    CadastroAtivoComponent,
    IndicesComponent,
    CadastrarIndiceComponent
  ],
  imports: [
    CommonModule,
    ConfirmDialogModule,
    PrimengModule,
    FormsModule,
    CalendarModule,
    ConfiguracaoRoutingModule,
    SharedExtendedModule,    
]
})
export class ConfiguracaoModule { }
