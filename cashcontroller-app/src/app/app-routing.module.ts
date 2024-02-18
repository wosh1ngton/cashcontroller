import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ListarRendaVariavelComponent } from './components/renda-variavel/listar-renda-variavel/listar-renda-variavel.component';

const routes: Routes = [
  { path: '', 
    children: [
      {
        path: 'renda-variavel', 
        component: ListarRendaVariavelComponent        
      }
    ],
    component: HomeComponent },
  
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
