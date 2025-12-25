import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Menubar } from './menubar/menubar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Menubar],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('Eshop');
}
