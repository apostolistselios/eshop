import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';
import { firstValueFrom } from 'rxjs';
import { MessageService } from 'primeng/api';

import { ApiService } from '../../api/api.service';

@Component({
  selector: 'app-shop-signup',
  imports: [ReactiveFormsModule, Card, InputText, Button, Password],
  templateUrl: './shop-signup.html',
  styleUrl: './shop-signup.scss',
})
export class ShopSignup {
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  private messageService = inject(MessageService);
  private apiService = inject(ApiService);

  protected signupForm = this.formBuilder.nonNullable.group({
    tin: ['', Validators.required],
    brandName: ['', Validators.required],
    owner: ['', Validators.required],
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  async signup() {
    try {
      const values = this.signupForm.getRawValue();
      await firstValueFrom(this.apiService.post('/api/signup/shop', values));

      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Successfully signed up as shop with TIN: ' + values.tin,
      });

      this.router.navigate(['/login']);
    } catch (error: any) {
      console.error(error);
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: error.error.message,
      });
    }
  }
}
