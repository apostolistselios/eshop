import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

import { LoginService } from '../login.service';

@Component({
  selector: 'app-shop-login',
  imports: [ReactiveFormsModule, Card, InputText, Password, Button],
  providers: [LoginService],
  templateUrl: './shop-login.html',
  styleUrl: './shop-login.scss',
})
export class ShopLogin {
  protected loginService = inject(LoginService);

  login() {
    this.loginService.loginAsShop();
  }
}
