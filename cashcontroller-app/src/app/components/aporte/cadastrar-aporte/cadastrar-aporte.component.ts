import { Component } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { tap } from 'rxjs';
import { Aporte } from 'src/app/models/aporte.model';
import { AporteService } from 'src/app/services/aporte.service';

@Component({
  selector: 'app-cadastrar-aporte',   
  templateUrl: './cadastrar-aporte.component.html',
  styleUrl: './cadastrar-aporte.component.css'
})
export class CadastrarAporteComponent {

  
  aporte: Aporte = new Aporte();

  constructor(private dialogRef: DynamicDialogRef,
    private aporteService: AporteService
  ) {}

  save(formValue: any) {    
    this.aporteService.save(formValue)
      .pipe(tap(() => this.dialogRef.close(formValue)))  
      .subscribe();      
  }

  close() {
    this.dialogRef.close();
  }

}
