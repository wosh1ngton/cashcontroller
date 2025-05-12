import { Component, OnInit } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { IndexadorService } from 'src/app/services/indexador.service';

@Component({
  selector: 'app-cadastrar-indice',  
  templateUrl: './cadastrar-indice.component.html',
  styleUrl: './cadastrar-indice.component.css'
})
export class CadastrarIndiceComponent implements OnInit {

  indice: any = {};
  indices: SelectItem[] = [{label: 'IPCA', value: 'IPCA'},{label:'SELIC', value: 'SELIC'}];
  
  constructor(
    private cadastrarIndiceRef: DynamicDialogRef,
    private indiceDialogConfig: DynamicDialogConfig,
    private indexadorService: IndexadorService) { }
  
  ngOnInit(): void {
    if(this.indiceDialogConfig.data.isEdicao) {
      this.indice = this.indiceDialogConfig.data.indice;
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
    if(this.isEditar) {
      this.indexadorService.editar(form).subscribe((result: any) => {
        this.cadastrarIndiceRef.close(result);
      });
    } else {
      this.indexadorService.save(form).subscribe((result: any) => {
        this.cadastrarIndiceRef.close(result);
      });
    }
    
    console.log(form);
  }

}
