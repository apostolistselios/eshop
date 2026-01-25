import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { InputNumber } from 'primeng/inputnumber';

@Component({
  selector: 'app-product-search',
  imports: [Card, Button, InputText, InputNumber, ReactiveFormsModule],
  templateUrl: './product-search.html',
  styleUrl: './product-search.scss',
})
export class ProductSearch {
  @Input({ required: true }) form!: FormGroup;
  @Output() search = new EventEmitter<void>();
  @Output() reset = new EventEmitter<void>();
}
