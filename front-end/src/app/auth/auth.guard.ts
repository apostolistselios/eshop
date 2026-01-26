import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard {
  private router = inject(Router);
  private authService = inject(AuthService);

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.authService.clearAuthState();
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }
}
