import { Routes } from '@angular/router';

import { CustomerLogin } from './login/customer-login/customer-login';
import { ShopLogin } from './login/shop-login/shop-login';
import { ShopSignup } from './signup/shop-signup/shop-signup';
import { CustomerSignup } from './signup/customer-signup/customer-signup';

export const routes: Routes = [
  {
    path: 'customer',
    children: [
      {
        path: 'login',
        component: CustomerLogin,
      },
      {
        path: 'signup',
        component: CustomerSignup,
      },
    ],
  },
  {
    path: 'shop',
    children: [
      {
        path: 'login',
        component: ShopLogin,
      },
      {
        path: 'signup',
        component: ShopSignup,
      },
    ],
  },
];
