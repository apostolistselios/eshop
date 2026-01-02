import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { DataViewModule, DataViewPageEvent } from 'primeng/dataview';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { InputNumber } from 'primeng/inputnumber';

import { ApiService } from '../api/api.service';
import { Product } from './product.interface';
import { PageableResponse } from '../api/pageable-response.interface';

@Component({
  selector: 'app-products',
  imports: [DataViewModule, Card, Button, InputText, InputNumber, ReactiveFormsModule],
  templateUrl: './products.html',
  styleUrl: './products.scss',
})
export class Products implements OnInit {
  private formBuilder = inject(FormBuilder);
  private apiService = inject(ApiService);

  protected products = signal<Product[]>([]);
  protected totalRecords = signal(1);
  protected itemsPerPage = 20;

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
    const params = this.buildSearchParams(page);

    this.apiService.get<PageableResponse<Product>>('/api/products', params).subscribe((res) => {
      this.products.set(res.content);
      this.totalRecords.set(res.totalElements);
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

  private buildSearchParams(page: number): Record<string, string | number | boolean> {
    const filters = this.filterForm.getRawValue();

    const params: Record<string, string | number | boolean> = {
      page: page,
      size: this.itemsPerPage,
    };

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        if (typeof value === 'number' || typeof value === 'string') {
          params[key] = value;
        }
      }
    });

    return params;
  }
}
