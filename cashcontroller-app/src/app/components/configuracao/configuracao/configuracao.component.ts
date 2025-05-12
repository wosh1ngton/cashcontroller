import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-configuracao',
  templateUrl: './configuracao.component.html',
  styleUrl: './configuracao.component.css',
})
export class ConfiguracaoComponent implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Ativos',
        icon: 'pi pi-globe',
        routerLink: 'listar-ativo',
      },
      {
        label: 'IPCA',
        routerLink: ['listar-indices', 'IPCA'],
       
      },
      {
        label: 'SELIC',
        routerLink: ['listar-indices', 'SELIC'],
       
      },
    ];
    
  }
}
