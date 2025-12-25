import { Component, inject } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

import { LoginService } from '../login.service';

@Component({
  selector: 'app-customer-login',
  imports: [ReactiveFormsModule, Card, InputText, Password, Button],
  providers: [LoginService],
  templateUrl: './customer-login.html',
  styleUrl: './customer-login.scss',
})
export class CustomerLogin {
  protected loginService = inject(LoginService);

  login() {
    this.loginService.loginAsCustomer();
  }
}
