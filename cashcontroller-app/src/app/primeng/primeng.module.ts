import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { MenubarModule } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';
import { DropdownModule } from 'primeng/dropdown';
import { TabViewModule } from 'primeng/tabview';


@NgModule({
  declarations: [],
  exports: [
    CommonModule,
    TableModule,
    MenubarModule,
    CardModule,
    ChipModule,
    DropdownModule,
    TabViewModule
  ]
})
export class PrimengModule { }
