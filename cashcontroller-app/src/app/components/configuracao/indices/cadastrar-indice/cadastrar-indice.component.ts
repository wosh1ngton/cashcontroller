import { Component, OnInit } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { IndexadorService } from 'src/app/services/indexador.service';
import { IndiceMes } from '../models/indice-mes.model';

@Component({
  selector: 'app-cadastrar-indice',
  templateUrl: './cadastrar-indice.component.html',
  styleUrl: './cadastrar-indice.component.css',
})
export class CadastrarIndiceComponent implements OnInit {
  indice: any = {};
  tipoIndice: SelectItem | undefined = { label: '', value: '' };
  indices: SelectItem[] = [
    { label: 'IPCA', value: 'IPCA' },
    { label: 'SELIC', value: 'SELIC' },
  ];

  constructor(
    private cadastrarIndiceRef: DynamicDialogRef,
    private indiceDialogConfig: DynamicDialogConfig,
    private indexadorService: IndexadorService
  ) {}

  ngOnInit(): void {
    if (this.indiceDialogConfig.data.isEdicao) {
      this.indice = this.indiceDialogConfig.data.indice;
      this.tipoIndice = this.indices.find(
        (tipo) => this.indiceDialogConfig.data.indice.tipo === tipo.value
      );
      if (!this.indice.data.includes('T') || !this.indice.data.includes(' ')) {
        this.indice.data += 'T00:00';
      }
      this.indice.data = new Date(this.indice.data);
    }
  }

  get isEditar(): boolean {
    return this.indiceDialogConfig.data?.isEdicao;
  }

  close() {
    this.cadastrarIndiceRef.close();
  }

  save(form: any) {
    const indiceMes = IndiceMes.fromDate(form.data, form.valor);
   
      if (this.isEditar) {
        this.indexadorService
          .editar(indiceMes, form.tipo.value, form.id)
          .subscribe((result: any) => {
            this.cadastrarIndiceRef.close(result);
          });
      } else {
        this.indexadorService
          .save(indiceMes, form.tipo.value)
          .subscribe((result: any) => {
            this.cadastrarIndiceRef.close(result);
          });
      }
  
  }
}
