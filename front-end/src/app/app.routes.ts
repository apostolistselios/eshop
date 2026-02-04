import { Routes } from '@angular/router';

import { Login } from './login/login';
import { ShopSignup } from './signup/shop-signup/shop-signup';
import { CustomerSignup } from './signup/customer-signup/customer-signup';
import { AuthGuard } from './auth/auth.guard';
import { RolesGuard } from './auth/role.guard';
import { Role } from './auth/role.enum';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () => import('./home/home').then((m) => m.Home),
  },
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
      {
        path: 'products',
        canActivate: [AuthGuard, RolesGuard],
        data: { roles: [Role.CUSTOMER] },
        loadComponent: () => import('./products/products').then((m) => m.Products),
      },
      {
        path: 'cart',
        canActivate: [AuthGuard, RolesGuard],
        data: { roles: [Role.CUSTOMER] },
        loadComponent: () => import('./cart/cart').then((m) => m.Cart),
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
      {
        path: 'manage-products',
        canActivate: [AuthGuard, RolesGuard],
        data: { roles: [Role.SHOP] },
        loadComponent: () =>
          import('./manage-products/manage-products').then((m) => m.ManageProducts),
      },
    ],
  },
];
