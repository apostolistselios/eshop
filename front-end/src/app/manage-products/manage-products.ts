import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Table, TableLazyLoadEvent, TableModule } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { Button } from 'primeng/button';

import { ApiService } from '../api/api.service';
import { PageableResponse } from '../api/pageable-response.interface';
import { Product } from '../products/product.interface';
import { ProductSearch } from '../products/product-search/product-search';
import { ProductSearchParamsService } from '../products/product-search/product-search-params.service';

@Component({
  selector: 'app-manage-products',
  imports: [TableModule, ProductSearch, ReactiveFormsModule, Button],
  templateUrl: './manage-products.html',
  styleUrl: './manage-products.scss',
})
export class ManageProducts implements OnInit {
  @ViewChild('productsTable') private table?: Table;

  private formBuilder = inject(FormBuilder);
  private messageService = inject(MessageService);
  private apiService = inject(ApiService);
  private productSearchParams = inject(ProductSearchParamsService);

  protected products = signal<Product[]>([]);
  protected totalRecords = signal(0);
  protected rows = 3;

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
    this.loadProducts(this.productSearchParams.buildSearchParams(this.filterForm, 0, this.rows));
  }

  onLazyLoad(event: TableLazyLoadEvent) {
    const rows = event.rows ?? this.rows;
    const page = event.first && rows ? event.first / rows : 0;
    this.rows = rows;
    const params = this.productSearchParams.buildSearchParams(this.filterForm, page, rows);

    if (event.filters) {
      Object.entries(event.filters).forEach(([field, metadata]) => {
        const filterMeta = Array.isArray(metadata) ? metadata[0] : metadata;
        const value = filterMeta?.value;
        if (value !== null && value !== undefined && value !== '') {
          params[field] = value;
        }
      });
    }

    this.loadProducts(params);
  }

  onSearch() {
    this.resetToFirstPage();
    this.loadProducts(this.productSearchParams.buildSearchParams(this.filterForm, 0, this.rows));
  }

  onReset() {
    this.filterForm.reset();
    this.resetToFirstPage();
    this.loadProducts(this.productSearchParams.buildSearchParams(this.filterForm, 0, this.rows));
  }

  onClearColumnFilters() {
    this.table?.clear();
  }

  private loadProducts(params: Record<string, string | number | boolean>) {
    this.apiService.get<PageableResponse<Product>>('/api/shop/products', params).subscribe({
      next: (res) => {
        this.products.set(res.content);
        this.totalRecords.set(res.totalElements);
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to load products',
        });
      },
    });
  }

  private resetToFirstPage() {
    if (this.table) {
      this.table.first = 0;
    }
  }
}
