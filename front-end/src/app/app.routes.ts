import { Routes } from '@angular/router';

import { CustomerLogin } from './login/customer-login/customer-login';
import { ShopLogin } from './login/shop-login/shop-login';

export const routes: Routes = [
  {
    path: 'customer',
    children: [
      {
        path: 'login',
        component: CustomerLogin,
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
    ],
  },
];
