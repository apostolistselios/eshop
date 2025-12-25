import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { Menu } from 'primeng/menu';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'app-menubar',
  imports: [MenubarModule, Menu, ButtonModule],
  templateUrl: './menubar.html',
  styleUrl: './menubar.scss',
})
export class Menubar implements OnInit {
  items: MenuItem[] | undefined;
  loginMenuItems: MenuItem[] | undefined;
  signupMenuItems: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-home',
        routerLink: '/',
      },
    ];

    this.loginMenuItems = [
      {
        label: 'Customer',
        icon: 'pi pi-user',
        routerLink: '/customer/login',
      },
      {
        label: 'Shop',
        icon: 'pi pi-shop',
        routerLink: '/shop/login',
      },
    ];

    this.signupMenuItems = [
      {
        label: 'Customer',
        icon: 'pi pi-user',
        routerLink: '/customer/signup',
      },
      {
        label: 'Shop',
        icon: 'pi pi-shop',
        routerLink: '/shop/signup',
      },
    ];
  }
}
