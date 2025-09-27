import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { ListarRendaFixaComponent } from './components/renda-fixa/listar-renda-fixa/listar-renda-fixa.component';
import { ListarCarteiraAcoesComponent } from './components/renda-variavel/listar-carteira-acoes/listar-carteira-acoes.component';
import { PosicoesEncerradasComponent } from './components/renda-variavel/posicoes-encerradas/posicoes-encerradas.component';
import { ListarFiisComponent } from './components/renda-variavel/fiis/listar-fiis/listar-fiis.component';
import { ConfiguracaoComponent } from './components/configuracao/configuracao/configuracao.component';
import { ListarOperacoesComponent } from './components/renda-variavel/listar-operacoes/listar-operacoes.component';
import { ListarCarteiraRendaFixaComponent } from './components/renda-fixa/listar-carteira-renda-fixa/listar-carteira-renda-fixa.component';
import { ListarAtivosCarteiraPrincipalComponent } from './components/carteira-principal/listar-ativos-carteira-principal/listar-ativos-carteira-principal.component';
import { ListarPatrimonioComponent } from './components/relatorios/listar-patrimonio/listar-patrimonio.component';
import { ListarAportesComponent } from './components/aporte/listar-aportes/listar-aportes.component';


const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',   
    title: 'Home Page',
    component: HomeComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        redirectTo: 'renda-variavel',
        pathMatch: 'full'
      },
      {
        path: 'renda-variavel',
        component: ListarOperacoesComponent,
      },
      {
        path: 'renda-fixa',
        component: ListarRendaFixaComponent,
      },
      {
        path: 'carteira-acoes',
        component: ListarCarteiraAcoesComponent,
      },
      {
        path: 'carteira-principal',
        component: ListarAtivosCarteiraPrincipalComponent,
      },
      {
        path: 'listar-patrimonio',
        component: ListarPatrimonioComponent,
      },
      {
        path: 'listar-aportes',
        component: ListarAportesComponent,
      },
      {
        path: 'listar-fiis',
        component: ListarFiisComponent,
      },
      {
        path: 'carteira-renda-fixa',
        component: ListarCarteiraRendaFixaComponent,
      },
      {
        path: 'posicoes-encerradas',
        component: PosicoesEncerradasComponent,
      },
      {
        path: 'configuracoes',
        component: ConfiguracaoComponent,
        loadChildren: () =>
          import('./components/configuracao/configuracao.module').then(
            (m) => m.ConfiguracaoModule
          ),
      },
    ],
    
  },
 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
