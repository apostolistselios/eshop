import { Component, OnChanges, SimpleChanges, inject, input, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { Button } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumber } from 'primeng/inputnumber';
import { InputText } from 'primeng/inputtext';

import { ApiService } from '../../api/api.service';
import { Product } from '../../products/product.interface';

@Component({
  selector: 'app-product-editor',
  imports: [ReactiveFormsModule, InputText, InputNumber, Button, DialogModule],
  templateUrl: './product-editor.html',
  styleUrl: './product-editor.scss',
})
export class ProductEditor implements OnChanges {
  private formBuilder = inject(FormBuilder);
  private messageService = inject(MessageService);
  private apiService = inject(ApiService);

  readonly visible = input.required<boolean>();
  readonly productId = input<number | null>(null);
  readonly closed = output<void>();
  readonly saved = output<void>();

  protected isEditMode = signal(false);
  protected isLoading = signal(false);
  protected fieldErrors = signal<Record<string, string[]>>({});

  protected productForm = this.formBuilder.nonNullable.group({
    type: ['', Validators.required],
    brand: ['', Validators.required],
    description: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
    quantity: [0, [Validators.required, Validators.min(0)]],
  });

  protected fieldErrorFor(field: string): string | null {
    const errors = this.fieldErrors()[field];
    return errors && errors.length > 0 ? errors[0] : null;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['visible'] && this.visible()) {
      const productId = this.productId();
      if (productId !== null && productId !== undefined) {
        this.isEditMode.set(true);
        this.loadProduct(productId);
      } else {
        this.isEditMode.set(false);
        this.fieldErrors.set({});
        this.productForm.reset({
          type: '',
          brand: '',
          description: '',
          price: 0,
          quantity: 0,
        });
      }
    }
  }

  save() {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const payload = this.productForm.getRawValue();
    const productId = this.productId();
    const request = this.isEditMode() && productId !== null
      ? this.apiService.patch<void, Product>(`/api/shop/products/${productId}`, payload)
      : this.apiService.post<void, Product>('/api/shop/products', payload);

    this.isLoading.set(true);
    this.fieldErrors.set({});
    request.subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: this.isEditMode() ? 'Product updated.' : 'Product added.',
        });
        this.isLoading.set(false);
        this.saved.emit();
      },
      error: (err) => {
        console.error(err);
        const errors = err?.error?.errors;
        if (errors && typeof errors === 'object') {
          this.fieldErrors.set(errors);
        }
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to save product.',
        });
        this.isLoading.set(false);
      },
    });
  }

  cancel() {
    this.closed.emit();
  }

  private loadProduct(id: number) {
    this.isLoading.set(true);
    this.fieldErrors.set({});
    this.apiService.get<Product>(`/api/shop/products/${id}`).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          type: product.type,
          brand: product.brand,
          description: product.description,
          price: product.price,
          quantity: product.quantity,
        });
      },
      error: (err) => {
        console.error(err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Failed to load product.',
        });
        this.isLoading.set(false);
      },
      complete: () => {
        this.isLoading.set(false);
      },
    });
  }
}
