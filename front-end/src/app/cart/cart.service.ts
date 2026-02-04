import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ApiService } from '../api/api.service';
import {
  AddCartItemDto,
  CartResponseDto,
  OrderResponseDto,
  UpdateCartItemDto,
} from './cart.interfaces';

@Injectable({ providedIn: 'root' })
export class CartService {
  private apiService = inject(ApiService);

  getCart(): Observable<CartResponseDto> {
    return this.apiService.get<CartResponseDto>('/api/cart');
  }

  addItem(productId: number, quantity: number): Observable<CartResponseDto> {
    const payload: AddCartItemDto = { productId, quantity };
    return this.apiService.post<CartResponseDto, AddCartItemDto>('/api/cart/item', payload);
  }

  updateItem(productId: number, quantity: number): Observable<CartResponseDto> {
    const payload: UpdateCartItemDto = { quantity };
    return this.apiService.patch<CartResponseDto, UpdateCartItemDto>(
      `/api/cart/item/${productId}`,
      payload,
    );
  }

  deleteItem(productId: number): Observable<void> {
    return this.apiService.delete<void>(`/api/cart/item/${productId}`);
  }

  checkout(): Observable<OrderResponseDto> {
    return this.apiService.post<OrderResponseDto, Record<string, never>>('/api/cart/checkout', {});
  }
}
