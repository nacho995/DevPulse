import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, AsyncPipe],
  template: `
    <nav class="navbar">
      <div class="navbar-brand">
        <a routerLink="/" class="logo">DevPulse</a>
      </div>
      <div class="navbar-links">
        <a routerLink="/dashboard" class="nav-link">Dashboard</a>
        @if (auth.isLoggedIn$ | async) {
          <button (click)="auth.logout()" class="btn-logout">Logout</button>
        } @else {
          <a routerLink="/login" class="nav-link">Login</a>
        }
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 2rem;
      height: 60px;
      background: #1a1a2e;
      border-bottom: 1px solid #16213e;
    }
    .logo {
      font-size: 1.5rem;
      font-weight: 700;
      color: #00d4aa;
      text-decoration: none;
    }
    .navbar-links { display: flex; gap: 1rem; align-items: center; }
    .nav-link {
      color: #a0a0b8;
      text-decoration: none;
      transition: color 0.2s;
    }
    .nav-link:hover { color: #fff; }
    .btn-logout {
      background: transparent;
      border: 1px solid #e74c3c;
      color: #e74c3c;
      padding: 0.4rem 1rem;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.2s;
    }
    .btn-logout:hover { background: #e74c3c; color: #fff; }
  `]
})
export class NavbarComponent {
  constructor(public auth: AuthService) {}
}
