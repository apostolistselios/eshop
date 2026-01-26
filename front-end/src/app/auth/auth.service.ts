import { HttpHeaders, HttpParams } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { ApiService } from '../api/api.service';
import { CurrentUser } from './current-user.interface';
import { Role } from './role.enum';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiService = inject(ApiService);
  private router = inject(Router);

  private _currentUser = signal<CurrentUser | null>(null);

  readonly currentUser = computed(() => this._currentUser());
  readonly isAuthenticated = computed(() => !!this._currentUser());

  constructor() {
    this.restoreCurrentUser();
  }

  async login(email: string, password: string): Promise<void> {
    const body = new HttpParams().set('email', email).set('password', password);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
    });

    try {
      await firstValueFrom(this.apiService.post<void>('/api/login', body, headers));
      const user = await this.fetchCurrentUser();
      this.setCurrentUser(user);
    } catch (error) {
      this.clearAuthState();
      throw error;
    }
  }

  logout(): void {
    this.apiService.post<void>('/api/logout', {}).subscribe({
      next: () => this.clearAuthState(),
      error: () => this.clearAuthState(),
    });
  }

  private async fetchCurrentUser(): Promise<CurrentUser> {
    return firstValueFrom(this.apiService.get<CurrentUser>('/api/users/current'));
  }

  private setCurrentUser(user: CurrentUser): void {
    this._currentUser.set(user);
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  clearAuthState(): void {
    this._currentUser.set(null);
    localStorage.removeItem('currentUser');
  }

  private restoreCurrentUser(): void {
    const stored = localStorage.getItem('currentUser');
    if (stored) {
      this._currentUser.set(JSON.parse(stored));
      void this.validateStoredUser();
    }
  }

  private async validateStoredUser(): Promise<void> {
    try {
      const user = await this.fetchCurrentUser();
      this.setCurrentUser(user);
    } catch (error) {
      this.clearAuthState();
      this.router.navigate(['/']);
    }
  }

  hasRole(role: Role): boolean {
    return this.currentUser()?.role.name === role;
  }

  hasAnyRole(roles: Role[]): boolean {
    const userRole = this.currentUser()?.role;
    return roles.some((r) => userRole && userRole.name === r);
  }
}
