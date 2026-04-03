import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>{{ isRegister ? 'Register' : 'Login' }}</h2>
        <form (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Username</label>
            <input [(ngModel)]="username" name="username" type="text" required />
          </div>
          <div class="form-group">
            <label>Password</label>
            <input [(ngModel)]="password" name="password" type="password" required />
          </div>
          @if (error) {
            <p class="error">{{ error }}</p>
          }
          <button type="submit" class="btn-primary">{{ isRegister ? 'Register' : 'Login' }}</button>
        </form>
        <p class="toggle" (click)="isRegister = !isRegister">
          {{ isRegister ? 'Already have an account? Login' : 'No account? Register' }}
        </p>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: calc(100vh - 60px);
    }
    .login-card {
      background: #16213e;
      padding: 2.5rem;
      border-radius: 12px;
      width: 380px;
      box-shadow: 0 8px 32px rgba(0,0,0,0.3);
    }
    h2 { color: #fff; margin-bottom: 1.5rem; text-align: center; }
    .form-group { margin-bottom: 1.2rem; }
    label { display: block; color: #a0a0b8; margin-bottom: 0.4rem; font-size: 0.9rem; }
    input {
      width: 100%;
      padding: 0.7rem;
      border: 1px solid #2a2a4a;
      border-radius: 8px;
      background: #0f0f23;
      color: #fff;
      font-size: 1rem;
      box-sizing: border-box;
    }
    input:focus { outline: none; border-color: #00d4aa; }
    .btn-primary {
      width: 100%;
      padding: 0.8rem;
      background: #00d4aa;
      color: #0f0f23;
      border: none;
      border-radius: 8px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .btn-primary:hover { background: #00b894; }
    .error { color: #e74c3c; font-size: 0.85rem; margin-bottom: 0.8rem; }
    .toggle {
      color: #00d4aa;
      text-align: center;
      margin-top: 1rem;
      cursor: pointer;
      font-size: 0.9rem;
    }
    .toggle:hover { text-decoration: underline; }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  isRegister = false;

  constructor(private auth: AuthService, private router: Router) {}

  onSubmit() {
    this.error = '';
    const action = this.isRegister
      ? this.auth.register(this.username, this.password)
      : this.auth.login(this.username, this.password);

    action.subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => this.error = this.isRegister ? 'Registration failed' : 'Invalid credentials'
    });
  }
}
