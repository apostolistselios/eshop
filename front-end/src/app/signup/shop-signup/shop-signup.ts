import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

@Component({
  selector: 'app-shop-signup',
  imports: [ReactiveFormsModule, Card, InputText, Button, Password],
  templateUrl: './shop-signup.html',
  styleUrl: './shop-signup.scss',
})
export class ShopSignup {
  private formBuilder = inject(FormBuilder);

  protected signupForm = this.formBuilder.group({
    tin: [''],
    brandName: [''],
    owner: [''],
    password: [''],
  });

  signup() {
    console.log('signup as customer called', this.signupForm.getRawValue());
  }
}
