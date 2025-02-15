import { Component, Input } from '@angular/core';
import { MessageService } from 'primeng/api';
import { DynamicDialogConfig, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Observable, startWith, tap } from 'rxjs';
import { AtivoCarteira } from 'src/app/models/ativo-carteira.model';
import { Ativo } from 'src/app/models/ativo.model';
import { AtivoCarteiraService } from 'src/app/services/ativo-carteira.service';
import { AtivoService } from 'src/app/services/ativo.service';
import { LoadingService } from 'src/app/services/loading.service';
import { FuncoesUtil } from 'src/app/shared/util/funcoes-util';

@Component({
  selector: 'app-cadastrar-ativo-carteira',
  templateUrl: './cadastrar-ativo-carteira.component.html',
  styleUrl: './cadastrar-ativo-carteira.component.css',
})
export class CadastrarAtivoCarteiraComponent {
  ativoCarteira: AtivoCarteira = new AtivoCarteira();
  carteira: AtivoCarteira[] = [];
  ativos: Ativo[] = [];
  ativo: Ativo = new Ativo();
  dataInicio: Date = new Date();
  subclasseAtivo: number = 0;

  constructor(
    private ativoService: AtivoService,
    private ativoCarteiraService: AtivoCarteiraService,
    private loading: LoadingService,
    private dialogRef: DynamicDialogRef,
    private messageService: MessageService,
    private configDialog: DynamicDialogConfig
  ) {}

  ngOnInit(): void {

    this.subclasseAtivo = this.configDialog.data.subclasse;    
    let isEditarAtivoCarteira = this.configDialog.data.isEdit;
    this.buscarAtivosPorSubclasse();

    if (isEditarAtivoCarteira) {
      this.ativoCarteiraService
        .findById(this.configDialog.data.rowData)
        .subscribe((res: any) => {
          this.ativoCarteira = res;   
          this.ativo.id = this.ativoCarteira.ativo.id;       
        });
    }
  }

  

  private buscarAtivosPorSubclasse(): void {
    this.ativoService.getAtivosPorSubClasse(this.subclasseAtivo).subscribe({
      next: (res) => (
        this.ativos = res      
      ),
      error: (err) => console.error('erro: ', err.message),
    });
  }

  private buscarAtivos(): void {
    this.ativoService.getAll().subscribe({
      next: (res) => (this.ativos = res),
      error: (err) => console.error('erro: ', err.message),
    });
  }

  private buscarCarteira(): void {
    this.ativoCarteiraService.getAtivosCarteira().subscribe({
      next: (res) => (this.carteira = res),
      error: (err) => console.error('erro: ', err.message),
    });
  }

  save(ativoCarteira: any) {
    console.log('log => ', this.ativoCarteira);
    console.log('log2 => ', ativoCarteira);
    let novoAtivo = FuncoesUtil.filtrarValoresNulos(this.ativoCarteira);

    novoAtivo = {
      ...novoAtivo,
    };
    console.log('log 3 => ', novoAtivo);

    this.loading
      .showLoaderUntilCompleted(
        novoAtivo['id'] > 0
          ? this.atualizarAtivo(novoAtivo, novoAtivo['id'])
          : this.salvarAtivo(novoAtivo)
      )
      .subscribe({
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
    return this.ativoCarteiraService.save(novoAtivo).pipe(
      tap(() => {
        this.dialogRef.close(novoAtivo);
      })
    );
  }

  private atualizarAtivo(novoAtivo: any, id: number) {
    return this.ativoCarteiraService.update(novoAtivo, id).pipe(
      tap(() => {
        this.dialogRef.close(novoAtivo);
      })
    );
  }

  close() {
    this.dialogRef.close();
  }
}
