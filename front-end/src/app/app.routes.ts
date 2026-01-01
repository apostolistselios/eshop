import { Routes } from '@angular/router';

import { Login } from './login/login';
import { ShopSignup } from './signup/shop-signup/shop-signup';
import { CustomerSignup } from './signup/customer-signup/customer-signup';
import { AuthGuard } from './auth/auth.guard';
import { RolesGuard } from './auth/role.guard';
import { Role } from './auth/role.enum';

export const routes: Routes = [
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'customer',
    children: [
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
        path: 'signup',
        component: ShopSignup,
      },
    ],
  },
  {
    path: 'products',
    canActivate: [AuthGuard, RolesGuard],
    data: { roles: [Role.CUSTOMER, Role.SHOP] },
    loadComponent: () => import('./products/products').then((m) => m.Products),
  },
];
