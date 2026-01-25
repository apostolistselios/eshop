import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';

import { AuthService } from '../auth/auth.service';
import { Role } from '../auth/role.enum';

@Component({
  selector: 'app-menubar',
  imports: [MenubarModule, Menu, ButtonModule, RouterLink],
  templateUrl: './menubar.html',
  styleUrl: './menubar.scss',
})
export class Menubar {
  private router = inject(Router);
  protected authService = inject(AuthService);

  items = signal<MenuItem[] | undefined>(undefined);
  signupMenuItems = signal<MenuItem[] | undefined>(undefined);

  constructor() {
    effect(() => {
      this.buildMenu();
    });
  }

  private buildMenu() {
    const user = this.authService.currentUser();
    this.items.set([
      {
        label: 'Home',
        icon: 'pi pi-home',
        routerLink: '/',
      },
      {
        label: 'Products',
        icon: 'pi pi-shopping-bag',
        routerLink: '/customer/products',
        visible: user ? this.authService.hasAnyRole([Role.CUSTOMER]) : false,
      },
      {
        label: 'Manage Products',
        icon: 'pi pi-briefcase',
        routerLink: '/shop/manage-products',
        visible: user ? this.authService.hasAnyRole([Role.SHOP]) : false,
      },
    ]);

    this.signupMenuItems.set([
      {
        label: 'Customer',
        icon: 'pi pi-user',
        routerLink: '/customer/signup',
        visible: !user,
      },
      {
        label: 'Shop',
        icon: 'pi pi-shop',
        routerLink: '/shop/signup',
        visible: !user,
      },
    ]);
  }

  async logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
