// src/app/core/interceptors/error.interceptor.ts
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { ToastrService } from 'ngx-toastr';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toastr = inject(ToastrService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        toastr.error('Session expirée. Veuillez vous reconnecter.');
        authService.logout();
      } else if (error.status === 403) {
        toastr.error('Accès non autorisé.');
      } else if (error.status === 500) {
        toastr.error('Erreur serveur. Veuillez réessayer.');
      }

      return throwError(() => error);
    })
  );
};