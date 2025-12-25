import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';
import { InputText } from 'primeng/inputtext';
import { Password } from 'primeng/password';

@Component({
  selector: 'app-customer-signup',
  imports: [ReactiveFormsModule, Card, InputText, Button, Password],
  templateUrl: './customer-signup.html',
  styleUrl: './customer-signup.scss',
})
export class CustomerSignup {
  private formBuilder = inject(FormBuilder);

  protected signupForm = this.formBuilder.group({
    tin: [''],
    firstname: [''],
    lastname: [''],
    email: [''],
    password: [''],
  });

  signup() {
    console.log('signup as customer called', this.signupForm.getRawValue());
  }
}
