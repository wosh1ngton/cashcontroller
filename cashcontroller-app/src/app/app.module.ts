import { CUSTOM_ELEMENTS_SCHEMA, LOCALE_ID, NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { PrimengModule } from './primeng/primeng.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
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



registerLocaleData(localePt, 'pt-BR', localePtExtra);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ListarCarteiraAcoesComponent,
    ListarCarteiraRendaFixaComponent,
    PosicoesEncerradasComponent,
    ListarFiisComponent,    
    LoadingComponent,    
  ],
  imports: [    
    BrowserModule,    
    AppRoutingModule,
    PrimengModule,
    BrowserAnimationsModule,
    FormsModule,    
    DetalharAtivoComponent,  
    SharedModule,
    ConfiguracaoModule    
  ],  
  exports: [
    
  ],
  providers: [
    provideHttpClient(),
    MessageService,    
    LoadingService,
    OperacaoRendaVariavelService,
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    
  ],
  bootstrap: [AppComponent],
  
})
export class AppModule { }
