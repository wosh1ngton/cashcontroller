import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { MenubarModule } from 'primeng/menubar';
import { CardModule } from 'primeng/card';
import { ChipModule } from 'primeng/chip';

@NgModule({
  declarations: [],
  exports: [
    CommonModule,
    TableModule,
    MenubarModule,
    CardModule,
    ChipModule
  ]
})
export class PrimengModule { }
