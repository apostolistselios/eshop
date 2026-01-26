import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  get<T>(
    endpoint: string,
    params?: Record<string, string | number | boolean | Array<string | number>>,
    headers?: HttpHeaders,
  ): Observable<T> {
    return this.http.get<T>(endpoint, {
      params: this.buildParams(params),
      headers,
      withCredentials: true,
    });
  }

  post<T, B = unknown>(endpoint: string, body: B, headers?: HttpHeaders): Observable<T> {
    return this.http.post<T>(endpoint, body, { headers, withCredentials: true });
  }

  patch<T, B = unknown>(endpoint: string, body: B, headers?: HttpHeaders): Observable<T> {
    return this.http.patch<T>(endpoint, body, { headers, withCredentials: true });
  }

  delete<T>(endpoint: string, headers?: HttpHeaders): Observable<T> {
    return this.http.delete<T>(endpoint, { headers, withCredentials: true });
  }

  private buildParams(
    params?: Record<string, string | number | boolean | Array<string | number>>,
  ): HttpParams | undefined {
    if (!params) {
      return undefined;
    }

    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      if (Array.isArray(value)) {
        value.forEach((entry) => {
          httpParams = httpParams.append(key, String(entry));
        });
        return;
      }
      httpParams = httpParams.set(key, String(value));
    });

    return httpParams;
  }
}
