import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApiService, GithubRepo } from '../../services/api.service';

@Component({
  selector: 'app-repos',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="repos-page">
      <div class="repos-header">
        <a routerLink="/dashboard" class="back-link">&#8592; Back to Dashboard</a>
        <h1>{{ techName }} Repositories</h1>
        <p class="subtitle">Top {{ repos.length }} most starred repositories on GitHub</p>
      </div>

      @if (loading) {
        <div class="loading">Loading repositories...</div>
      }

      @if (error) {
        <div class="error-box">{{ error }}</div>
      }

      <div class="repos-list">
        @for (repo of repos; track repo.id; let i = $index) {
          <a [href]="repo.url" target="_blank" class="repo-card" [style.animation-delay]="i * 60 + 'ms'">
            <div class="repo-rank">{{ i + 1 }}</div>
            <div class="repo-content">
              <div class="repo-name-row">
                <h3>{{ repo.fullName || repo.name }}</h3>
                @if (repo.language) {
                  <span class="repo-lang">{{ repo.language }}</span>
                }
              </div>
              <p class="repo-desc">{{ repo.description || 'No description' }}</p>
              <div class="repo-stats">
                <span class="repo-stat">&#9733; {{ formatNumber(repo.stars) }}</span>
                <span class="repo-stat">&#9931; {{ formatNumber(repo.forks) }}</span>
              </div>
            </div>
            <div class="repo-arrow">&#8599;</div>
          </a>
        }
      </div>

      @if (repos.length === 0 && !loading && !error) {
        <div class="empty">
          <p>No repository data yet. Go back and click "Sync Data" first.</p>
        </div>
      }
    </div>
  `,
  styles: [`
    .repos-page { max-width: 900px; margin: 0 auto; padding: 2rem; }
    .repos-header { margin-bottom: 2rem; animation: fadeInUp 0.4s ease-out; }
    .back-link { display: inline-block; color: var(--accent); text-decoration: none; font-size: 0.85rem; font-weight: 600; margin-bottom: 1rem; transition: opacity 0.2s; }
    .back-link:hover { opacity: 0.7; }
    h1 { font-family: var(--font-display); font-size: 2rem; font-weight: 800; color: var(--text-primary); letter-spacing: -0.03em; }
    .subtitle { color: var(--text-secondary); font-size: 0.9rem; margin-top: 0.3rem; }
    .loading { text-align: center; color: var(--accent); padding: 3rem 0; font-size: 0.9rem; }
    .error-box { padding: 1rem; background: rgba(255,68,102,0.1); border: 1px solid rgba(255,68,102,0.3); border-radius: 8px; color: var(--danger); margin-bottom: 1rem; font-size: 0.85rem; }
    .repos-list { display: flex; flex-direction: column; gap: 8px; }
    .repo-card { display: flex; align-items: center; gap: 1rem; padding: 1.2rem; background: var(--bg-card); border: 1px solid var(--border); border-radius: var(--radius-md); text-decoration: none; transition: all 0.25s; animation: fadeInUp 0.4s ease-out both; }
    .repo-card:hover { border-color: var(--accent); transform: translateX(4px); box-shadow: 0 4px 20px rgba(0,0,0,0.3); }
    .repo-rank { font-family: var(--font-mono); font-size: 1.2rem; font-weight: 700; color: var(--text-muted); min-width: 32px; text-align: center; }
    .repo-content { flex: 1; min-width: 0; }
    .repo-name-row { display: flex; align-items: center; gap: 0.6rem; margin-bottom: 0.3rem; }
    .repo-name-row h3 { font-size: 1rem; font-weight: 700; color: var(--accent); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .repo-lang { font-family: var(--font-mono); font-size: 0.6rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; padding: 0.15rem 0.5rem; border: 1px solid var(--border); border-radius: 100px; flex-shrink: 0; }
    .repo-desc { font-size: 0.8rem; color: var(--text-secondary); margin-bottom: 0.5rem; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
    .repo-stats { display: flex; gap: 1rem; }
    .repo-stat { font-family: var(--font-mono); font-size: 0.75rem; color: var(--text-muted); }
    .repo-arrow { font-size: 1.2rem; color: var(--text-muted); transition: color 0.2s; }
    .repo-card:hover .repo-arrow { color: var(--accent); }
    .empty { text-align: center; color: var(--text-muted); padding: 4rem 0; }
  `]
})
export class ReposComponent implements OnInit {
  repos: GithubRepo[] = [];
  techName = '';
  loading = true;
  error = '';

  constructor(private route: ActivatedRoute, private api: ApiService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.params['id']);
    this.techName = this.route.snapshot.queryParams['name'] || 'Technology';

    this.api.getReposByTechnology(id).subscribe({
      next: repos => {
        this.repos = repos;
        this.loading = false;
      },
      error: (err) => {
        this.error = `Failed to load repos: ${err.message}`;
        this.loading = false;
      }
    });
  }

  formatNumber(n: number): string {
    if (n >= 1_000_000) return (n / 1_000_000).toFixed(1) + 'M';
    if (n >= 1_000) return (n / 1_000).toFixed(1) + 'K';
    return n?.toString() ?? '0';
  }
}
