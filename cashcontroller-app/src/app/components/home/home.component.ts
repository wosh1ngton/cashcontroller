import { Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem, MenuItemCommandEvent } from 'primeng/api';
import { PrimeIcons } from 'primeng/api';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],  
  
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  items: MenuItem[] | undefined;
  selectedMenu: any;
  
  
  

  ngOnInit(): void {
    this.items = [    
      {
        label: 'Operações',
        icon: PrimeIcons.HISTORY,        
        items: [
          {
            label: 'Renda Variável',
            icon: PrimeIcons.CHART_LINE,
            routerLink: 'renda-variavel',
            id: '2',
            command: (event) => this.getMenuSelecionado(event)
          },
          {
            label: 'Renda Fixa',
            icon: PrimeIcons.CHART_BAR,
            routerLink: 'renda-fixa',
            id: '1',
            command: (event) => this.getMenuSelecionado(event)
          }
        ]
      },
      {
        label: 'Carteiras',
        icon: PrimeIcons.WALLET,
        items: [
          {
            label: 'Ações',
            icon: PrimeIcons.CALENDAR_PLUS,
            routerLink: 'carteira-acoes',
            id: '3',
            command: (event) => this.getMenuSelecionado(event)
          },
          {
            label: 'Fundos Imobiliários',
            icon: PrimeIcons.BUILDING,
            routerLink: 'listar-fiis',
            id: '4',
            command: (event) => this.getMenuSelecionado(event)
          }

        ]
        
      },
      
      {
        label: 'Posições Encerradas',
        icon: PrimeIcons.ANGLE_DOWN,
        routerLink: 'posicoes-encerradas',
        id: '5',
        command: (event) => this.getMenuSelecionado(event)
      },
      {
        label: '',
        icon: PrimeIcons.COG,        
        routerLink: 'configuracoes',
        id: '6',
        command: (event) => this.getMenuSelecionado(event)
      },
    ];
  }

  getMenuSelecionado($event: MenuItemCommandEvent) {    
    this.selectedMenu = $event.item?.id;
  }

}
