import { inject, Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';

import { AuthService } from './auth.service';
import { Role } from './role.enum';

@Injectable({ providedIn: 'root' })
export class RolesGuard implements CanActivate {
  private router = inject(Router);
  private authService = inject(AuthService);

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRoles = route.data['roles'] as Role[];

    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }

    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }

    if (this.authService.hasAnyRole(requiredRoles)) {
      return true;
    }

    this.router.navigate(['/']);

    return false;
  }
}
