import { inject, Injectable } from '@angular/core';
import { FormBuilder } from '@angular/forms';

@Injectable()
export class LoginService {
  private formBuilder = inject(FormBuilder);

  private _loginForm = this.formBuilder.group({
    username: [''],
    password: [''],
  });

  get loginForm() {
    return this._loginForm;
  }

  loginAsCustomer() {
    /**
     * @todo call backend
     */
    console.log('login as customer called', this.loginForm.getRawValue());
  }

  loginAsShop() {
    /**
     * @todo call backend
     */
    console.log('login as shop called', this.loginForm.getRawValue());
  }
}
