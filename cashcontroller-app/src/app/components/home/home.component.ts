import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  items: MenuItem[] | undefined;
  selectedMenu: any;
  ngOnInit(): void {
    this.items = [
      {
        label: 'Renda Fixa',
        icon: PrimeIcons.CHART_BAR,
        routerLink: 'renda-fixa',
        id: '1',
        command: (event) => this.getMenuSelecionado(event)
      },
      {
        label: 'Renda Variável',
        icon: PrimeIcons.CHART_LINE,
        routerLink: 'renda-variavel',
        id: '2',
        command: (event) => this.getMenuSelecionado(event)
      },
      {
        label: 'Carteira de Ações',
        icon: PrimeIcons.WALLET,
        routerLink: 'carteira-acoes',
        id: '3',
        command: (event) => this.getMenuSelecionado(event)
      },
    ];
  }

  getMenuSelecionado($event:any) {    
    const clickedItem = $event.item;    
    this.selectedMenu = clickedItem;    
  }

}
