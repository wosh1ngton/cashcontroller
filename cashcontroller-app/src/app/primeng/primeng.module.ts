import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { MenubarModule } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';
import { DropdownModule } from 'primeng/dropdown';
import { TabViewModule } from 'primeng/tabview';
import { CheckboxModule } from 'primeng/checkbox';
import { InputNumberModule } from 'primeng/inputnumber';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MenuModule } from 'primeng/menu';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FieldsetModule } from 'primeng/fieldset';
import { ChartModule } from 'primeng/chart';
import { DataViewModule } from 'primeng/dataview';
import { PasswordModule } from 'primeng/password';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';

@NgModule({
  declarations: [],
  exports: [
    CommonModule,
    TableModule,
    MenuModule,
    MenubarModule,
    ButtonModule,
    CardModule,
    ChipModule,
    DropdownModule,
    TabViewModule,
    CheckboxModule,
    InputNumberModule,
    ProgressSpinnerModule,
    InputTextModule,
    FieldsetModule,
    ChartModule,
    DataViewModule,
    PasswordModule,
    MessageModule,
    ToastModule

  ]
})
export class PrimengModule { }
