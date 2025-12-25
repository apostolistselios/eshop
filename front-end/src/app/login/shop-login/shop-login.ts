import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

@Component({
  selector: 'app-shop-login',
  imports: [ReactiveFormsModule, Card, InputText, Password, Button],
  templateUrl: './shop-login.html',
  styleUrl: './shop-login.scss',
})
export class ShopLogin {
  private formBuilder = inject(FormBuilder);

  protected loginForm = this.formBuilder.group({
    username: [''],
    password: [''],
  });

  login() {
    console.log('login as shop called', this.loginForm.getRawValue());

    /**
     * @todo call backend
     */
  }
}
