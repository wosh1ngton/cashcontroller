import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConfiguracaoComponent } from './configuracao/configuracao.component';
import { ListarAtivosComponent } from './listar-ativos/listar-ativos.component';

const routes: Routes = [
  { path: 'configuracoes', component: ConfiguracaoComponent },
  { path: 'listar-ativo', component: ListarAtivosComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfiguracaoRoutingModule { 

}
