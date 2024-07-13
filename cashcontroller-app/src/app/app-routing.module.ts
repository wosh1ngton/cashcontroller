import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';

import { ListarRendaFixaComponent } from './components/renda-fixa/listar-renda-fixa/listar-renda-fixa.component';
import { ListarOperacoesComponent } from './components/renda-variavel/listar-operacoes/listar-operacoes.component';
import { ListarCarteiraAcoesComponent } from './components/renda-variavel/listar-carteira-acoes/listar-carteira-acoes.component';
import { PosicoesEncerradasComponent } from './components/renda-variavel/posicoes-encerradas/posicoes-encerradas.component';
import { ListarFiisComponent } from './components/renda-variavel/fiis/listar-fiis/listar-fiis.component';

const routes: Routes = [
  { path: '', 
    children: [
      {
        path: 'renda-variavel', 
        component: ListarOperacoesComponent
      },
      {
        path: 'renda-fixa', 
        component: ListarRendaFixaComponent
      },
      {
        path: 'carteira-acoes', 
        component: ListarCarteiraAcoesComponent
      },
      {
        path: 'listar-fiis', 
        component: ListarFiisComponent
      },
      {
        path: 'posicoes-encerradas', 
        component: PosicoesEncerradasComponent
      },
    ],
    component: HomeComponent },
  
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
