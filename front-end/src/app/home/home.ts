import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Button } from 'primeng/button';
import { Card } from 'primeng/card';

import { AuthService } from '../auth/auth.service';
import { Role } from '../auth/role.enum';

@Component({
  selector: 'app-home',
  imports: [Card, Button, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.scss',
})
export class Home {
  protected authService = inject(AuthService);
  protected roles = Role;
}
