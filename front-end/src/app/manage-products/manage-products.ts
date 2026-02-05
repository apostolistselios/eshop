import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Table, TableLazyLoadEvent, TableModule } from 'primeng/table';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Button } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

import { ApiService } from '../api/api.service';
import { PageableResponse } from '../api/pageable-response.interface';
import { Product } from '../products/product.interface';
import { ProductSearch } from '../products/product-search/product-search';
import { ProductSearchParamsService } from '../products/product-search/product-search-params.service';
import { ProductEditor } from './product-editor/product-editor';

@Component({
  selector: 'app-manage-products',
  imports: [
    TableModule,
    ProductSearch,
    ReactiveFormsModule,
    Button,
    ProductEditor,
    TooltipModule,
    ConfirmDialogModule,
  ],
  templateUrl: './manage-products.html',
  styleUrl: './manage-products.scss',
})
export class ManageProducts implements OnInit {
  @ViewChild('productsTable') private table?: Table;

  private formBuilder = inject(FormBuilder);
  private messageService = inject(MessageService);
  private confirmationService = inject(ConfirmationService);
  private apiService = inject(ApiService);
  private productSearchParams = inject(ProductSearchParamsService);

  protected products = signal<Product[]>([]);
  protected totalRecords = signal(0);
  protected rows = 10;
  protected currentPage = signal(0);
  protected editorVisible = signal(false);
  protected editorProductId = signal<number | null>(null);

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
    this.currentPage.set(page);
    const params = this.productSearchParams.buildSearchParams(this.filterForm, page, rows);

    const sortParams = this.buildSortParams(event);
    if (sortParams.length > 0) {
      params['sort'] = sortParams;
    }

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

  openAddProduct() {
    this.editorProductId.set(null);
    this.editorVisible.set(true);
  }

  openEditProduct(product: Product) {
    if (!product.id) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Missing id',
        detail: 'This product cannot be edited because it has no id.',
      });
      return;
    }

    this.editorProductId.set(product.id);
    this.editorVisible.set(true);
  }

  deleteProduct(product: Product) {
    if (!product.id) {
      this.messageService.add({
        severity: 'warn',
        summary: 'Missing id',
        detail: 'This product cannot be deleted because it has no id.',
      });
      return;
    }

    this.confirmationService.confirm({
      header: 'Delete product',
      message: `Delete product "${product.brand} ${product.type}"? This cannot be undone.`,
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Delete',
      rejectLabel: 'Cancel',
      acceptButtonStyleClass: 'p-button-danger',
      rejectButtonStyleClass: 'p-button-secondary',
      accept: () => {
        this.apiService.delete<void>(`/api/products/${product.id}`).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Deleted',
              detail: 'Product deleted.',
            });
            this.reloadCurrentPage();
          },
          error: (err) => {
            console.error(err);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: err.error?.message ?? 'Failed to delete product.',
            });
          },
        });
      },
    });
  }

  onEditorClosed() {
    this.editorVisible.set(false);
  }

  onEditorSaved() {
    this.editorVisible.set(false);
    this.reloadCurrentPage();
  }

  private loadProducts(params: Record<string, string | number | boolean | Array<string | number>>) {
    this.apiService.get<PageableResponse<Product>>('/api/products/manage', params).subscribe({
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
    this.currentPage.set(0);
  }

  private reloadCurrentPage() {
    const params = this.productSearchParams.buildSearchParams(
      this.filterForm,
      this.currentPage(),
      this.rows,
    );
    this.loadProducts(params);
  }

  private buildSortParams(event: TableLazyLoadEvent): string[] {
    if (event.multiSortMeta && event.multiSortMeta.length > 0) {
      return event.multiSortMeta
        .filter((meta) => !!meta.field && !!meta.order)
        .map((meta) => `${meta.field},${meta.order === 1 ? 'asc' : 'desc'}`);
    }

    if (event.sortField && event.sortOrder) {
      return [`${event.sortField},${event.sortOrder === 1 ? 'asc' : 'desc'}`];
    }

    return [];
  }
}
