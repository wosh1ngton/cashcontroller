import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { tap } from 'rxjs';
import { Ativo } from 'src/app/models/ativo.model';
import { SubclasseAtivo } from 'src/app/models/subclasse-ativo.model';
import { AtivoService } from 'src/app/services/ativo.service';
import { LoadingService } from 'src/app/services/loading.service';

@Component({
  selector: 'app-cadastro-ativo',  
  templateUrl: './cadastro-ativo.component.html',
  styleUrl: './cadastro-ativo.component.css'
})
export class CadastroAtivoComponent implements OnInit {

  subclasses: SubclasseAtivo[] = [];
  ativo: Ativo = new Ativo();
  

  constructor(
    private ativoService: AtivoService,
    private loading: LoadingService,
    private dialogRef: DynamicDialogRef,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.ativoService.getSubclasseAtivos()
      .subscribe((res) => {
        this.subclasses = res;
      })
  }

  save(ativo: any) {
   
    this.loading
      .showLoaderUntilCompleted(
        this.ativoService.save(ativo).pipe(
          tap(() => {
            this.dialogRef.close(ativo);
          })
        )
      ).subscribe({
        next: (val) => console.log(val),
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erro',
            detail: 'Tente novamente',
          });
        },
      });
  }

  close() {
    this.dialogRef.close();
  }

}
