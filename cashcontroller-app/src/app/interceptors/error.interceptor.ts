import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { MessageService } from 'primeng/api';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private messageService: MessageService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        switch (error.status) {
          case 401:
            this.authService.logout();
            break;
          case 403:
            this.messageService.add({
              severity: 'error',
              summary: 'Acesso negado',
              detail: 'Você não tem permissão para acessar este recurso.'
            });
            break;
          case 400:
            const validationErrors = error.error?.errors;
            if (validationErrors && Array.isArray(validationErrors)) {
              validationErrors.forEach((err: string) => {
                this.messageService.add({
                  severity: 'warn',
                  summary: 'Validação',
                  detail: err,
                  life: 5000
                });
              });
            } else {
              this.messageService.add({
                severity: 'error',
                summary: 'Erro',
                detail: error.error?.message || error.error || 'Requisição inválida.'
              });
            }
            break;
          case 0:
            this.messageService.add({
              severity: 'error',
              summary: 'Erro de conexão',
              detail: 'Servidor indisponível. Verifique sua conexão.'
            });
            break;
          case 500:
            this.messageService.add({
              severity: 'error',
              summary: 'Erro interno',
              detail: 'Ocorreu um erro no servidor. Tente novamente mais tarde.'
            });
            break;
        }
        return throwError(() => error);
      })
    );
  }
}
