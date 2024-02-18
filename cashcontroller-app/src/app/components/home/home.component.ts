import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }

  items: MenuItem[] | undefined;

  ngOnInit(): void {
    this.items = [
      {
          label: 'Renda Fixa',
          icon: 'pi pi-fw pi-power-off'
      },
      {
        label: 'Renda Variável',
        icon: 'pi pi-fw pi-power-off',
        routerLink: 'renda-variavel'        
      },
    ];
    }
  

  

}
