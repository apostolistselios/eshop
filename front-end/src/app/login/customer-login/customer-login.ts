import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

@Component({
  selector: 'app-customer-login',
  imports: [ReactiveFormsModule, Card, InputText, Password, Button],
  templateUrl: './customer-login.html',
  styleUrl: './customer-login.scss',
})
export class CustomerLogin {
  private formBuilder = inject(FormBuilder);

  protected loginForm = this.formBuilder.group({
    username: [''],
    password: [''],
  });

  login() {
    console.log('login as customer called', this.loginForm.getRawValue());

    /**
     * @todo call backend
     */
  }
}
