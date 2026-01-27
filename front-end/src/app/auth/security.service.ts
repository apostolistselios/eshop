import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class SecurityService {
  private http = inject(HttpClient);

  refreshCsrf() {
    return this.http.get('/api/csrf');
  }
}
