import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, AsyncPipe],
  template: `
    <nav class="nav">
      <div class="nav-inner">
        <a routerLink="/" class="brand">
          <span class="brand-icon">&#9670;</span>
          <span class="brand-text">Dev<span class="brand-accent">Pulse</span></span>
        </a>
        <div class="nav-links">
          <a routerLink="/dashboard" routerLinkActive="active" class="nav-link">
            <span class="link-dot"></span>
            Dashboard
          </a>
          @if (auth.isLoggedIn$ | async) {
            <button (click)="auth.logout()" class="nav-btn logout">Sign Out</button>
          } @else {
            <a routerLink="/login" routerLinkActive="active" class="nav-btn login">Sign In</a>
          }
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .nav {
      position: sticky;
      top: 0;
      z-index: 100;
      backdrop-filter: blur(20px) saturate(180%);
      background: rgba(6, 6, 14, 0.85);
      border-bottom: 1px solid var(--border);
    }
    .nav-inner {
      display: flex;
      justify-content: space-between;
      align-items: center;
      max-width: 1400px;
      margin: 0 auto;
      padding: 0 2rem;
      height: 64px;
    }
    .brand {
      display: flex;
      align-items: center;
      gap: 0.6rem;
      text-decoration: none;
      transition: transform 0.2s;
    }
    .brand:hover { transform: scale(1.02); }
    .brand-icon {
      font-size: 1.4rem;
      color: var(--accent);
      filter: drop-shadow(0 0 8px var(--accent-glow));
    }
    .brand-text {
      font-family: var(--font-display);
      font-size: 1.3rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.02em;
    }
    .brand-accent { color: var(--accent); }
    .nav-links { display: flex; align-items: center; gap: 0.5rem; }
    .nav-link {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.5rem 1rem;
      color: var(--text-secondary);
      text-decoration: none;
      font-size: 0.9rem;
      font-weight: 500;
      border-radius: var(--radius-sm);
      transition: all 0.2s;
    }
    .nav-link:hover { color: var(--text-primary); background: rgba(255,255,255,0.04); }
    .nav-link.active { color: var(--accent); }
    .link-dot {
      width: 5px; height: 5px;
      border-radius: 50%;
      background: var(--text-muted);
      transition: background 0.2s;
    }
    .nav-link.active .link-dot { background: var(--accent); box-shadow: 0 0 6px var(--accent); }
    .nav-btn {
      padding: 0.45rem 1.2rem;
      font-size: 0.85rem;
      font-weight: 600;
      font-family: var(--font-display);
      border-radius: var(--radius-sm);
      cursor: pointer;
      transition: all 0.2s;
      text-decoration: none;
    }
    .login {
      background: var(--accent);
      color: var(--bg-void);
      border: none;
    }
    .login:hover { background: var(--accent-dim); transform: translateY(-1px); }
    .logout {
      background: transparent;
      color: var(--text-secondary);
      border: 1px solid var(--border);
    }
    .logout:hover { border-color: var(--danger); color: var(--danger); }
  `]
})
export class NavbarComponent {
  constructor(public auth: AuthService) {}
}
