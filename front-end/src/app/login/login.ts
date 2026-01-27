import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';
import { MessageService } from 'primeng/api';

import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, Card, InputText, Password, Button],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private router = inject(Router);
  private formBuilder = inject(FormBuilder);
  private messageService = inject(MessageService);
  private authService = inject(AuthService);

  protected loginForm = this.formBuilder.nonNullable.group({
    email: ['', Validators.required],
    password: ['', Validators.required],
  });

  login() {
    const values = this.loginForm.getRawValue();
    this.authService.login(values.email, values.password).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Successfully logged in: ' + values.email,
        });

        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error(error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Invalid credentials',
        });
      },
    });
  }
}
