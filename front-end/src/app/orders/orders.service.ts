import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiService } from '../api/api.service';
import { OrderResponseDto } from './orders.interfaces';

@Injectable({ providedIn: 'root' })
export class OrdersService {
  private apiService = inject(ApiService);

  getOrders(): Observable<OrderResponseDto[]> {
    return this.apiService.get<OrderResponseDto[]>('/api/order');
  }
}
