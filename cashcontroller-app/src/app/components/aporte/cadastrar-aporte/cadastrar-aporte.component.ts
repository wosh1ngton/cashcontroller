import { Component, OnInit } from '@angular/core';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { tap } from 'rxjs';
import { Aporte } from 'src/app/models/aporte.model';
import { AporteService } from 'src/app/services/aporte.service';
import { DateUtil } from 'src/app/shared/util/date-util';

@Component({
  selector: 'app-cadastrar-aporte',   
  templateUrl: './cadastrar-aporte.component.html',
  styleUrl: './cadastrar-aporte.component.css'
})
export class CadastrarAporteComponent implements OnInit {

  
  aporte: Aporte = new Aporte();
  
  constructor(private dialogRef: DynamicDialogRef,
    private aporteService: AporteService,
    private configDialog: DynamicDialogConfig
  ) {}

  get isEdicao(): boolean {
    return this.configDialog.data.isEdit;
  }

  ngOnInit(): void {
    
    if(this.isEdicao) {
      let aporteEmEdicao = this.configDialog.data.rowData;
      this.aporte = {
        id: aporteEmEdicao.id,
        valorAporte : aporteEmEdicao.valorAporte,
        dataAporte : DateUtil.dateConstructor(aporteEmEdicao.dataAporte) 
      }
    }    
  }

  save(formValue: any) {      
    
    Object.assign(formValue, this.aporte);    
    this.aporteService.save(formValue)
      .pipe(tap(() => this.dialogRef.close(formValue)))  
      .subscribe();      
  }

  close() {
    this.dialogRef.close();
  }

}
