import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="auth-page">
      <div class="auth-container">
        <div class="auth-header">
          <div class="auth-icon">&#9670;</div>
          <h1>{{ isRegister ? 'Create Account' : 'Welcome Back' }}</h1>
          <p class="auth-subtitle">{{ isRegister ? 'Join the observatory' : 'Access your dashboard' }}</p>
        </div>

        <form (ngSubmit)="onSubmit()" class="auth-form">
          <div class="field">
            <label>Username</label>
            <div class="input-wrapper">
              <input
                [(ngModel)]="username"
                name="username"
                type="text"
                placeholder="Enter your username"
                required
                autocomplete="username" />
            </div>
          </div>

          <div class="field">
            <label>Password</label>
            <div class="input-wrapper">
              <input
                [(ngModel)]="password"
                name="password"
                type="password"
                placeholder="Enter your password"
                required
                autocomplete="current-password" />
            </div>
          </div>

          @if (error) {
            <div class="error-msg">
              <span class="error-icon">!</span>
              {{ error }}
            </div>
          }

          <button type="submit" class="submit-btn" [class.loading]="loading" [disabled]="loading">
            @if (loading) {
              <span class="spinner"></span>
            } @else {
              {{ isRegister ? 'Create Account' : 'Sign In' }}
            }
          </button>
        </form>

        <div class="auth-footer">
          <span class="toggle" (click)="isRegister = !isRegister; error = ''">
            {{ isRegister ? 'Already have an account? Sign in' : 'Need an account? Create one' }}
          </span>
        </div>
      </div>

      <div class="auth-decoration">
        <div class="deco-line" style="--delay: 0s"></div>
        <div class="deco-line" style="--delay: 0.5s"></div>
        <div class="deco-line" style="--delay: 1s"></div>
      </div>
    </div>
  `,
  styles: [`
    .auth-page {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: calc(100vh - 64px);
      padding: 2rem;
      position: relative;
      overflow: hidden;
    }
    .auth-container {
      width: 100%;
      max-width: 420px;
      animation: fadeInUp 0.6s ease-out;
    }
    .auth-header { text-align: center; margin-bottom: 2.5rem; }
    .auth-icon {
      font-size: 2rem;
      color: var(--accent);
      filter: drop-shadow(0 0 20px var(--accent-glow));
      margin-bottom: 1rem;
    }
    h1 {
      font-family: var(--font-display);
      font-size: 1.8rem;
      font-weight: 700;
      color: var(--text-primary);
      letter-spacing: -0.03em;
    }
    .auth-subtitle {
      color: var(--text-secondary);
      font-size: 0.95rem;
      margin-top: 0.4rem;
    }
    .auth-form { display: flex; flex-direction: column; gap: 1.4rem; }
    .field label {
      display: block;
      font-size: 0.8rem;
      font-weight: 600;
      color: var(--text-secondary);
      text-transform: uppercase;
      letter-spacing: 0.08em;
      margin-bottom: 0.5rem;
    }
    .input-wrapper {
      position: relative;
    }
    input {
      width: 100%;
      padding: 0.85rem 1rem;
      background: var(--bg-surface);
      border: 1px solid var(--border);
      border-radius: var(--radius-md);
      color: var(--text-primary);
      font-family: var(--font-display);
      font-size: 0.95rem;
      transition: all 0.25s;
    }
    input::placeholder { color: var(--text-muted); }
    input:focus {
      outline: none;
      border-color: var(--accent);
      box-shadow: 0 0 0 3px var(--accent-glow), inset 0 0 0 1px var(--accent);
    }
    .error-msg {
      display: flex;
      align-items: center;
      gap: 0.6rem;
      padding: 0.75rem 1rem;
      background: rgba(255, 68, 102, 0.08);
      border: 1px solid rgba(255, 68, 102, 0.2);
      border-radius: var(--radius-sm);
      color: var(--danger);
      font-size: 0.85rem;
    }
    .error-icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 20px; height: 20px;
      background: var(--danger);
      color: white;
      border-radius: 50%;
      font-size: 0.7rem;
      font-weight: 700;
      flex-shrink: 0;
    }
    .submit-btn {
      width: 100%;
      padding: 0.9rem;
      background: var(--accent);
      color: var(--bg-void);
      border: none;
      border-radius: var(--radius-md);
      font-family: var(--font-display);
      font-size: 0.95rem;
      font-weight: 700;
      cursor: pointer;
      transition: all 0.25s;
      position: relative;
      margin-top: 0.5rem;
    }
    .submit-btn:hover:not(:disabled) {
      background: var(--accent-dim);
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 255, 200, 0.2);
    }
    .submit-btn:disabled { opacity: 0.7; cursor: not-allowed; }
    .spinner {
      display: inline-block;
      width: 18px; height: 18px;
      border: 2px solid transparent;
      border-top-color: var(--bg-void);
      border-radius: 50%;
      animation: spin 0.6s linear infinite;
    }
    @keyframes spin { to { transform: rotate(360deg); } }
    .auth-footer {
      text-align: center;
      margin-top: 1.5rem;
    }
    .toggle {
      color: var(--text-secondary);
      font-size: 0.88rem;
      cursor: pointer;
      transition: color 0.2s;
    }
    .toggle:hover { color: var(--accent); }
    .auth-decoration {
      position: absolute;
      top: 0; right: -200px; bottom: 0;
      width: 400px;
      pointer-events: none;
    }
    .deco-line {
      position: absolute;
      width: 1px;
      height: 100%;
      background: linear-gradient(to bottom, transparent, var(--accent-glow), transparent);
      opacity: 0.3;
      animation: shimmer 4s ease-in-out infinite;
      animation-delay: var(--delay);
    }
    .deco-line:nth-child(1) { left: 40%; }
    .deco-line:nth-child(2) { left: 55%; }
    .deco-line:nth-child(3) { left: 70%; }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  isRegister = false;
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    this.error = '';
    this.loading = true;

    const action = this.isRegister
      ? this.auth.register(this.username, this.password)
      : this.auth.login(this.username, this.password);

    action.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.loading = false;
        this.error = this.isRegister ? 'Registration failed. Try another username.' : 'Invalid credentials. Please try again.';
      }
    });
  }
}
