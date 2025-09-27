import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { PrimengModule } from './primeng/primeng.module';
import { SharedExtendedModule } from './shared-modules/shared.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MessageService, SharedModule } from 'primeng/api';
import { OperacaoRendaVariavelService } from './services/operacao-renda-variavel.service';
import { ListarCarteiraAcoesComponent } from './components/renda-variavel/listar-carteira-acoes/listar-carteira-acoes.component';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import localePtExtra from '@angular/common/locales/extra/pt';
import { PosicoesEncerradasComponent } from './components/renda-variavel/posicoes-encerradas/posicoes-encerradas.component';
import { DetalharAtivoComponent } from './components/renda-variavel/detalhar-ativo/detalhar-ativo.component';
import { ListarFiisComponent } from './components/renda-variavel/fiis/listar-fiis/listar-fiis.component';
import { LoadingComponent } from './components/shared/loading/loading.component';
import { LoadingService } from './services/loading.service';
import { ConfiguracaoModule } from './components/configuracao/configuracao.module';
import { ListarCarteiraRendaFixaComponent } from './components/renda-fixa/listar-carteira-renda-fixa/listar-carteira-renda-fixa.component';
import { ListarAtivosCarteiraPrincipalComponent } from './components/carteira-principal/listar-ativos-carteira-principal/listar-ativos-carteira-principal.component';
import { CadastrarAtivoCarteiraComponent } from './components/carteira-principal/cadastrar-ativo-carteira/cadastrar-ativo-carteira.component';
import { CalendarModule } from 'primeng/calendar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TableCarteiraPrincipalComponent } from './components/carteira-principal/table-carteira-principal/table-carteira-principal.component';
import { TabViewModule } from 'primeng/tabview';
import { ToastModule } from 'primeng/toast';
import { ListarPatrimonioComponent } from './components/relatorios/listar-patrimonio/listar-patrimonio.component';
import { TreeMapChartComponent } from './components/charts/tree-map-chart/tree-map-chart.component';
import { CadastrarAporteComponent } from './components/aporte/cadastrar-aporte/cadastrar-aporte.component';
import { ListarAportesComponent } from './components/aporte/listar-aportes/listar-aportes.component';

import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { DefaultComponent } from './components/default/default.component';
import { LoginComponent } from './components/login/login.component';
import { CardComponent } from './components/shared/card/card.component';
import { ListarUsuarioComponent } from './components/configuracao/usuario/listar-usuario/listar-usuario.component';


registerLocaleData(localePt, 'pt-BR', localePtExtra);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ListarCarteiraAcoesComponent,
    ListarCarteiraRendaFixaComponent,
    ListarAtivosCarteiraPrincipalComponent,
    CadastrarAtivoCarteiraComponent,
    PosicoesEncerradasComponent,
    ListarFiisComponent,    
    LoadingComponent,    
    TableCarteiraPrincipalComponent,
    ListarPatrimonioComponent,
    TreeMapChartComponent,
    CadastrarAporteComponent,
    ListarAportesComponent   ,
    DefaultComponent,
    LoginComponent      
    
  ],
  imports: [    
    BrowserModule,    
    AppRoutingModule,
    ConfirmDialogModule,
    PrimengModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    DetalharAtivoComponent,  
    SharedModule,
    SharedExtendedModule,
    CalendarModule,  
    ConfiguracaoModule,
    KeycloakAngularModule,
    ToastModule,
    HttpClientModule
  ],  
  exports: [
    ConfirmDialogModule
  ],
  providers: [
    MessageService,    
    LoadingService,
    OperacaoRendaVariavelService,
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },  
     
  ],
  bootstrap: [AppComponent],
  
})
export class AppModule { }
