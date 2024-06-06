import { LOCALE_ID, NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';

import { PrimengModule } from './primeng/primeng.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { OperacaoRendaVariavelService } from './services/operacao-renda-variavel.service';
import { ListarCarteiraAcoesComponent } from './components/renda-variavel/listar-carteira-acoes/listar-carteira-acoes.component';
import { registerLocaleData } from '@angular/common';
// Import the locale data for Brazilian Portuguese
import localePt from '@angular/common/locales/pt';
import localePtExtra from '@angular/common/locales/extra/pt';


registerLocaleData(localePt, 'pt-BR', localePtExtra);

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ListarCarteiraAcoesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    PrimengModule,
    BrowserAnimationsModule,
    FormsModule
  ],  
  providers: [
    provideHttpClient(),
    MessageService,
    OperacaoRendaVariavelService,
    { provide: LOCALE_ID, useValue: 'pt-BR' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
