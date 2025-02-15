import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { tap } from 'rxjs';
import { Ativo } from 'src/app/models/ativo.model';
import { Indexador } from 'src/app/models/indexador.model';
import { ParametroRendaFixa } from 'src/app/models/parametro-renda-fixa.model';
import { SubclasseAtivo } from 'src/app/models/subclasse-ativo.model';
import { AtivoService } from 'src/app/services/ativo.service';
import { IndexadorService } from 'src/app/services/indexador.service';
import { LoadingService } from 'src/app/services/loading.service';
import { DateUtil } from 'src/app/shared/util/date-util';
import { FuncoesUtil } from 'src/app/shared/util/funcoes-util';

@Component({
  selector: 'app-cadastro-ativo',  
  templateUrl: './cadastro-ativo.component.html',
  styleUrl: './cadastro-ativo.component.css'
})
export class CadastroAtivoComponent implements OnInit {

  subclasses: SubclasseAtivo[] = [];
  ativo: Ativo = new Ativo();
  indices: Indexador[] = [];
  indexador: Indexador = new Indexador();
  subclasseAtivo: SubclasseAtivo = new SubclasseAtivo();
  dataVencimento: Date = new Date();
  isIsento: boolean = false;

  constructor(
    private ativoService: AtivoService,
    private loading: LoadingService,
    private dialogRef: DynamicDialogRef,
    private messageService: MessageService,
    private indexadorService: IndexadorService,
    private configDialog: DynamicDialogConfig
  ) {}

  ngOnInit(): void {

    this.buscarIndexadores();
    this.buscarSubclasses();
    let isEditarAtivo = this.configDialog.data.isEdit;

    if(isEditarAtivo) {
      this.ativoService.findById(this.configDialog.data.rowData)
       .subscribe((res:any) => {                   
          this.ativo = res;    
          this.subclasseAtivo = res.subclasseAtivoDto;
          this.indexador = res.parametroRendaFixaDto.indexador;
          this.dataVencimento = DateUtil.dateConstructor(res.parametroRendaFixaDto.dataVencimento);           
        })        
    }
  }

  private buscarSubclasses() {
    this.ativoService.getSubclasseAtivos()
      .subscribe((res) => {
        this.subclasses = res;
      });
  }



  save(ativo: any) {    
    
    let parametroRendaFixa = new ParametroRendaFixa();
    this.ativo.parametroRendaFixaDto = parametroRendaFixa;
    this.ativo.parametroRendaFixaDto.dataVencimento = ativo.dataVencimento;
    this.ativo.parametroRendaFixaDto.isIsento = ativo.isIsento;
    this.ativo.parametroRendaFixaDto.indexador = ativo.indexador;       
    let novoAtivo = FuncoesUtil.filtrarValoresNulos(this.ativo); 

    novoAtivo = {
        ...novoAtivo,
        subclasseAtivo: ativo.subclasseAtivo

    }
    console.log('log => ', novoAtivo)
    
    this.loading
      .showLoaderUntilCompleted(        
        ativo.id > 0 ? this.atualizarAtivo(novoAtivo) : this.salvarAtivo(novoAtivo)
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

  private salvarAtivo(novoAtivo: any) {
    return this.ativoService.save(novoAtivo).pipe(
      tap(() => {
        this.dialogRef.close(novoAtivo);
      })
    );
  }

  private atualizarAtivo(novoAtivo: any) {
    return this.ativoService.update(novoAtivo).pipe(
      tap(() => {
        this.dialogRef.close(novoAtivo);        
      })
    );
  }

  close() {
    this.dialogRef.close();
  }

  private buscarIndexadores() {
    this.indexadorService.getIndexadores().subscribe(
      index => { this.indices = index }
    );
  }

}
