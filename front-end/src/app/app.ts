import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Toast } from 'primeng/toast';

import { Menubar } from './menubar/menubar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Menubar, Toast],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('Eshop');
}
