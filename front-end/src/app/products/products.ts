import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { DataViewModule, DataViewPageEvent } from 'primeng/dataview';
import { MessageService } from 'primeng/api';
import { Card } from 'primeng/card';
import { Button } from 'primeng/button';

import { ApiService } from '../api/api.service';
import { Product } from './product.interface';
import { PageableResponse } from '../api/pageable-response.interface';
import { ProductSearch } from './product-search/product-search';
import { ProductSearchParamsService } from './product-search/product-search-params.service';

@Component({
  selector: 'app-products',
  imports: [DataViewModule, ProductSearch, ReactiveFormsModule, Card, Button],
  templateUrl: './products.html',
  styleUrl: './products.scss',
})
export class Products implements OnInit {
  private formBuilder = inject(FormBuilder);
  private messageService = inject(MessageService);
  private apiService = inject(ApiService);
  private productSearchParams = inject(ProductSearchParamsService);

  protected products = signal<Product[]>([]);
  protected totalRecords = signal(1);
  protected itemsPerPage = 12;

  protected filterForm = this.formBuilder.group({
    type: [''],
    brand: [''],
    description: [''],
    minPrice: [undefined],
    maxPrice: [undefined],
    minQuantity: [undefined],
    maxQuantity: [undefined],
  });

  ngOnInit(): void {
    this.loadProducts(0);
  }

  loadProducts(page: number) {
    const params = this.productSearchParams.buildSearchParams(
      this.filterForm,
      page,
      this.itemsPerPage,
    );

    this.apiService.get<PageableResponse<Product>>('/api/customer/products', params).subscribe({
      next: (res) => {
        this.products.set(res.content);
        this.totalRecords.set(res.totalElements);
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error.message,
        });
      },
    });
  }

  onSearch() {
    this.loadProducts(0);
  }

  onReset() {
    this.filterForm.reset();
    this.loadProducts(0);
  }

  onPageChange(event: DataViewPageEvent) {
    let page = 0;
    if (event.first !== 0) {
      page = event.first / event.rows;
    }

    this.loadProducts(page);
  }
}
