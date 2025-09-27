import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoadingComponent } from '../components/shared/loading/loading.component';
import { PrimengModule } from '../primeng/primeng.module';
import { ValidarExclusaoComponent } from '../components/shared/form-action/validar-exclusao.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { EditarComponent } from '../components/shared/form-action/editar.component';

@NgModule({
  declarations: [
    ValidarExclusaoComponent,
    EditarComponent,
  ],
  imports: [
    CommonModule,
    PrimengModule,
    ConfirmDialogModule
  ],
  exports: [
    ValidarExclusaoComponent,
    EditarComponent
  ]
})
export class SharedExtendedModule { }
