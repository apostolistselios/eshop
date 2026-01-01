import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';
import { firstValueFrom } from 'rxjs';

import { ApiService } from '../../api/api.service';

@Component({
  selector: 'app-customer-signup',
  imports: [ReactiveFormsModule, Card, InputText, Button, Password],
  templateUrl: './customer-signup.html',
  styleUrl: './customer-signup.scss',
})
export class CustomerSignup {
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  private messageService = inject(MessageService);
  private apiService = inject(ApiService);

  protected signupForm = this.formBuilder.group({
    tin: [''],
    firstname: [''],
    lastname: [''],
    email: [''],
    password: [''],
  });

  async signup() {
    try {
      const values = this.signupForm.getRawValue();
      await firstValueFrom(this.apiService.post('/api/signup/customer', values));

      this.messageService.add({
        severity: 'success',
        summary: 'Success',
        detail: 'Successfully signed up as customer with TIN: ' + values.tin,
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
