import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputNumber } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';

import { CartService } from './cart.service';
import { CartItemResponseDto, CartResponseDto } from './cart.interfaces';

@Component({
  selector: 'app-cart',
  imports: [TableModule, InputNumber, Button, Card, FormsModule],
  templateUrl: './cart.html',
  styleUrl: './cart.scss',
})
export class Cart implements OnInit {
  private cartService = inject(CartService);
  private messageService = inject(MessageService);

  protected cartId = signal<number | null>(null);
  protected items = signal<CartItemResponseDto[]>([]);
  protected totalAmount = signal(0);
  protected isLoading = signal(false);
  protected isCheckingOut = signal(false);
  protected updatingIds = signal<Set<number>>(new Set());

  protected quantities: Record<number, number> = {};

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    this.isLoading.set(true);
    this.cartService.getCart().subscribe({
      next: (res) => this.applyCart(res),
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to load cart.',
        });
        this.isLoading.set(false);
      },
    });
  }

  updateItem(item: CartItemResponseDto) {
    const nextQuantity = this.quantities[item.productId];
    if (nextQuantity === undefined || nextQuantity === item.quantity) {
      return;
    }

    this.setUpdating(item.productId, true);
    this.cartService.updateItem(item.productId, nextQuantity).subscribe({
      next: (res) => {
        this.applyCart(res);
        this.messageService.add({
          severity: 'success',
          summary: 'Updated',
          detail: 'Cart item updated.',
        });
      },
      error: (err) => {
        console.error(err);
        this.quantities[item.productId] = item.quantity;
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to update cart item.',
        });
        this.setUpdating(item.productId, false);
      },
    });
  }

  removeItem(item: CartItemResponseDto) {
    this.setUpdating(item.productId, true);
    this.cartService.deleteItem(item.productId).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Removed',
          detail: `${item.productBrand} ${item.productType} removed from cart.`,
        });
        this.loadCart();
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to remove cart item.',
        });
        this.setUpdating(item.productId, false);
      },
    });
  }

  checkout() {
    if (this.items().length === 0) {
      return;
    }

    this.isCheckingOut.set(true);
    this.cartService.checkout().subscribe({
      next: (order) => {
        this.messageService.add({
          severity: 'success',
          summary: 'Order created',
          detail: `Order #${order.id} created successfully.`,
        });
        this.loadCart();
        this.isCheckingOut.set(false);
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to checkout.',
        });
        this.isCheckingOut.set(false);
      },
    });
  }

  isUpdating(productId: number): boolean {
    return this.updatingIds().has(productId);
  }

  private setUpdating(productId: number, isUpdating: boolean) {
    const next = new Set(this.updatingIds());
    if (isUpdating) {
      next.add(productId);
    } else {
      next.delete(productId);
    }
    this.updatingIds.set(next);
  }

  private applyCart(res: CartResponseDto) {
    this.cartId.set(res.cartId);
    this.items.set(res.items ?? []);
    this.totalAmount.set(res.totalAmount ?? 0);
    this.quantities = (res.items ?? []).reduce<Record<number, number>>((acc, item) => {
      acc[item.productId] = item.quantity;
      return acc;
    }, {});
    this.isLoading.set(false);
    this.setUpdatingForAll(false);
  }

  private setUpdatingForAll(isUpdating: boolean) {
    const next = new Set(this.updatingIds());
    if (!isUpdating) {
      next.clear();
    }
    this.updatingIds.set(next);
  }
}
