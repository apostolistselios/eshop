import { Component, inject, OnInit, signal } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Card } from 'primeng/card';
import { TableModule } from 'primeng/table';

import { OrdersService } from './orders.service';
import { OrderResponseDto } from './orders.interfaces';

@Component({
  selector: 'app-orders',
  imports: [Card, TableModule],
  templateUrl: './orders.html',
  styleUrl: './orders.scss',
})
export class Orders implements OnInit {
  private ordersService = inject(OrdersService);
  private messageService = inject(MessageService);

  protected orders = signal<OrderResponseDto[]>([]);
  protected isLoading = signal(false);

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.isLoading.set(true);
    this.ordersService.getOrders().subscribe({
      next: (res) => {
        this.orders.set(res ?? []);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to load orders.',
        });
        this.isLoading.set(false);
      },
    });
  }
}
