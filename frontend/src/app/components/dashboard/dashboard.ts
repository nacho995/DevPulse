import { Component, OnInit } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { ApiService } from '../../services/api.service';
import { Technology, GithubData } from '../../models/technology.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [BaseChartDirective],
  template: `
    <div class="dashboard">
      <header class="dash-header">
        <div class="header-text">
          <h1>Observatory</h1>
          <p class="subtitle">Real-time tech ecosystem intelligence from GitHub</p>
        </div>
        <button (click)="refreshData()" class="refresh-btn" [disabled]="loading">
          <span class="refresh-icon" [class.spinning]="loading">&#8635;</span>
          {{ loading ? 'Syncing...' : 'Sync Data' }}
        </button>
      </header>

      <section class="metrics-strip">
        <div class="metric-pill">
          <span class="metric-label">Technologies</span>
          <span class="metric-value">{{ technologies.length }}</span>
        </div>
        <div class="metric-pill">
          <span class="metric-label">Total Repos</span>
          <span class="metric-value">{{ formatNumber(totalRepos) }}</span>
        </div>
        <div class="metric-pill">
          <span class="metric-label">Total Stars</span>
          <span class="metric-value accent">{{ formatNumber(totalStars) }}</span>
        </div>
      </section>

      <section class="cards-section">
        <h2 class="section-title">
          <span class="title-marker"></span>
          Technologies
        </h2>
        <div class="tech-grid">
          @for (tech of technologies; track tech.id; let i = $index) {
            <div class="tech-card" [style.animation-delay]="i * 80 + 'ms'">
              <div class="card-top">
                <div class="card-name-row">
                  <h3>{{ tech.name }}</h3>
                  <span class="type-tag">{{ tech.type }}</span>
                </div>
              </div>
              @if (getGithubDataFor(tech.id); as data) {
                <div class="card-stats">
                  <div class="stat-item">
                    <div class="stat-bar">
                      <div class="stat-fill repos" [style.width]="getBarWidth(data.repos, maxRepos)"></div>
                    </div>
                    <div class="stat-info">
                      <span class="stat-num">{{ formatNumber(data.repos) }}</span>
                      <span class="stat-lbl">repositories</span>
                    </div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-bar">
                      <div class="stat-fill stars" [style.width]="getBarWidth(data.stars, maxStars)"></div>
                    </div>
                    <div class="stat-info">
                      <span class="stat-num">{{ formatNumber(data.stars) }}</span>
                      <span class="stat-lbl">stars</span>
                    </div>
                  </div>
                  <div class="stat-item">
                    <div class="stat-bar">
                      <div class="stat-fill forks" [style.width]="getBarWidth(data.forks, maxForks)"></div>
                    </div>
                    <div class="stat-info">
                      <span class="stat-num">{{ formatNumber(data.forks) }}</span>
                      <span class="stat-lbl">forks</span>
                    </div>
                  </div>
                </div>
              } @else {
                <div class="no-data">No data yet</div>
              }
            </div>
          }
        </div>
      </section>

      <section class="charts-section">
        <h2 class="section-title">
          <span class="title-marker"></span>
          Visual Analytics
        </h2>
        <div class="chart-grid">
          <div class="chart-panel large">
            <div class="panel-header">
              <h3>Repository Count</h3>
              <span class="panel-tag">bar</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="reposChartData" [options]="barOptions" type="bar"></canvas>
            </div>
          </div>
          <div class="chart-panel">
            <div class="panel-header">
              <h3>Stars Distribution</h3>
              <span class="panel-tag">doughnut</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="starsChartData" [options]="doughnutOptions" type="doughnut"></canvas>
            </div>
          </div>
          <div class="chart-panel">
            <div class="panel-header">
              <h3>Fork Activity</h3>
              <span class="panel-tag">polar</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="forksChartData" [options]="polarOptions" type="polarArea"></canvas>
            </div>
          </div>
        </div>
      </section>
    </div>
  `,
  styles: [`
    .dashboard {
      max-width: 1400px;
      margin: 0 auto;
      padding: 2rem 2rem 4rem;
    }

    .dash-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-end;
      margin-bottom: 2rem;
      animation: fadeInUp 0.5s ease-out;
    }
    h1 {
      font-family: var(--font-display);
      font-size: 2.4rem;
      font-weight: 800;
      color: var(--text-primary);
      letter-spacing: -0.04em;
      line-height: 1;
    }
    .subtitle {
      color: var(--text-secondary);
      font-size: 0.95rem;
      margin-top: 0.4rem;
    }
    .refresh-btn {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.6rem 1.4rem;
      background: var(--bg-card);
      border: 1px solid var(--border);
      color: var(--text-primary);
      border-radius: var(--radius-sm);
      font-family: var(--font-display);
      font-size: 0.85rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.25s;
    }
    .refresh-btn:hover:not(:disabled) { border-color: var(--accent); color: var(--accent); }
    .refresh-btn:disabled { opacity: 0.5; }
    .refresh-icon { font-size: 1.1rem; transition: transform 0.3s; }
    .spinning { animation: spin 1s linear infinite; }
    @keyframes spin { to { transform: rotate(360deg); } }

    .metrics-strip {
      display: flex;
      gap: 1rem;
      margin-bottom: 2.5rem;
      animation: fadeInUp 0.5s ease-out 0.1s both;
    }
    .metric-pill {
      display: flex;
      align-items: center;
      gap: 0.6rem;
      padding: 0.6rem 1.2rem;
      background: var(--bg-surface);
      border: 1px solid var(--border);
      border-radius: 100px;
    }
    .metric-label {
      font-size: 0.75rem;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.06em;
      font-weight: 600;
    }
    .metric-value {
      font-family: var(--font-mono);
      font-size: 0.95rem;
      font-weight: 700;
      color: var(--text-primary);
    }
    .metric-value.accent { color: var(--accent); }

    .section-title {
      display: flex;
      align-items: center;
      gap: 0.7rem;
      font-size: 1.1rem;
      font-weight: 700;
      color: var(--text-primary);
      margin-bottom: 1.2rem;
      letter-spacing: -0.01em;
    }
    .title-marker {
      width: 3px;
      height: 18px;
      background: var(--accent);
      border-radius: 2px;
    }

    .tech-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 1rem;
      margin-bottom: 3rem;
    }
    .tech-card {
      background: var(--bg-card);
      border: 1px solid var(--border);
      border-radius: var(--radius-md);
      padding: 1.4rem;
      transition: all 0.3s;
      animation: fadeInUp 0.5s ease-out both;
    }
    .tech-card:hover {
      border-color: var(--border-glow);
      transform: translateY(-2px);
      box-shadow: 0 8px 32px rgba(0,0,0,0.3);
    }
    .card-top { margin-bottom: 1.2rem; }
    .card-name-row { display: flex; justify-content: space-between; align-items: center; }
    .tech-card h3 {
      font-size: 1.15rem;
      font-weight: 700;
      color: var(--text-primary);
    }
    .type-tag {
      font-family: var(--font-mono);
      font-size: 0.65rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.1em;
      padding: 0.25rem 0.7rem;
      background: var(--accent-glow);
      color: var(--accent);
      border-radius: 100px;
      border: 1px solid rgba(0, 255, 200, 0.15);
    }
    .card-stats { display: flex; flex-direction: column; gap: 0.8rem; }
    .stat-item {}
    .stat-bar {
      width: 100%;
      height: 4px;
      background: var(--bg-void);
      border-radius: 2px;
      overflow: hidden;
      margin-bottom: 0.4rem;
    }
    .stat-fill {
      height: 100%;
      border-radius: 2px;
      transition: width 1s ease-out;
    }
    .stat-fill.repos { background: var(--accent); }
    .stat-fill.stars { background: var(--warning); }
    .stat-fill.forks { background: var(--purple); }
    .stat-info { display: flex; align-items: baseline; gap: 0.4rem; }
    .stat-num {
      font-family: var(--font-mono);
      font-size: 0.9rem;
      font-weight: 700;
      color: var(--text-primary);
    }
    .stat-lbl { font-size: 0.75rem; color: var(--text-muted); }
    .no-data {
      color: var(--text-muted);
      font-size: 0.85rem;
      font-style: italic;
      padding: 1rem 0;
    }

    .charts-section { animation: fadeInUp 0.5s ease-out 0.3s both; }
    .chart-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      grid-template-rows: auto auto;
      gap: 1rem;
    }
    .chart-panel {
      background: var(--bg-card);
      border: 1px solid var(--border);
      border-radius: var(--radius-md);
      padding: 1.4rem;
    }
    .chart-panel.large { grid-column: 1 / -1; }
    .panel-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1rem;
    }
    .panel-header h3 {
      font-size: 0.95rem;
      font-weight: 600;
      color: var(--text-primary);
    }
    .panel-tag {
      font-family: var(--font-mono);
      font-size: 0.65rem;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.08em;
      padding: 0.2rem 0.6rem;
      border: 1px solid var(--border);
      border-radius: 4px;
    }
    .chart-wrap { position: relative; }

    @media (max-width: 768px) {
      .dashboard { padding: 1rem; }
      .dash-header { flex-direction: column; align-items: flex-start; gap: 1rem; }
      .metrics-strip { flex-wrap: wrap; }
      .tech-grid { grid-template-columns: 1fr; }
      .chart-grid { grid-template-columns: 1fr; }
      .chart-panel.large { grid-column: auto; }
    }
  `]
})
export class DashboardComponent implements OnInit {
  technologies: Technology[] = [];
  githubData: GithubData[] = [];
  loading = false;
  totalRepos = 0;
  totalStars = 0;
  maxRepos = 1;
  maxStars = 1;
  maxForks = 1;

  reposChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  starsChartData: ChartData<'doughnut'> = { labels: [], datasets: [] };
  forksChartData: ChartData<'polarArea'> = { labels: [], datasets: [] };

  barOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: true,
    aspectRatio: 3,
    plugins: { legend: { display: false } },
    scales: {
      y: {
        ticks: { color: '#7878a0', font: { family: 'JetBrains Mono', size: 10 } },
        grid: { color: '#1e1e4a' },
        border: { display: false }
      },
      x: {
        ticks: { color: '#7878a0', font: { family: 'Outfit', size: 12 } },
        grid: { display: false },
        border: { display: false }
      }
    }
  };

  doughnutOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    cutout: '65%',
    plugins: {
      legend: {
        position: 'bottom',
        labels: { color: '#7878a0', font: { family: 'Outfit', size: 12 }, padding: 16 }
      }
    }
  };

  polarOptions: ChartOptions<'polarArea'> = {
    responsive: true,
    plugins: {
      legend: {
        position: 'bottom',
        labels: { color: '#7878a0', font: { family: 'Outfit', size: 12 }, padding: 16 }
      }
    },
    scales: {
      r: {
        ticks: { display: false },
        grid: { color: '#1e1e4a' }
      }
    }
  };

  private colors = ['#00ffc8', '#ff4466', '#ffaa00', '#4488ff', '#aa66ff', '#ff66aa'];

  constructor(private api: ApiService) {}

  ngOnInit() { this.loadData(); }

  loadData() {
    this.api.getTechnologies().subscribe(page => {
      this.technologies = page.content;
      this.api.getGithubData().subscribe(data => {
        this.githubData = data;
        this.computeMetrics();
        this.buildCharts();
      });
    });
  }

  refreshData() {
    this.loading = true;
    this.api.fetchGithubData().subscribe({
      next: () => setTimeout(() => { this.loadData(); this.loading = false; }, 2500),
      error: () => this.loading = false
    });
  }

  getGithubDataFor(techId: number): GithubData | undefined {
    return this.githubData.find(d => d.technologyId === techId);
  }

  getBarWidth(value: number, max: number): string {
    return max ? Math.round((value / max) * 100) + '%' : '0%';
  }

  formatNumber(n: number): string {
    if (n >= 1_000_000) return (n / 1_000_000).toFixed(1) + 'M';
    if (n >= 1_000) return (n / 1_000).toFixed(1) + 'K';
    return n?.toString() ?? '0';
  }

  private computeMetrics() {
    this.totalRepos = this.githubData.reduce((s, d) => s + d.repos, 0);
    this.totalStars = this.githubData.reduce((s, d) => s + d.stars, 0);
    this.maxRepos = Math.max(...this.githubData.map(d => d.repos), 1);
    this.maxStars = Math.max(...this.githubData.map(d => d.stars), 1);
    this.maxForks = Math.max(...this.githubData.map(d => d.forks), 1);
  }

  private buildCharts() {
    const labels = this.technologies.map(t => t.name);
    const repos = this.technologies.map(t => this.getGithubDataFor(t.id)?.repos ?? 0);
    const stars = this.technologies.map(t => this.getGithubDataFor(t.id)?.stars ?? 0);
    const forks = this.technologies.map(t => this.getGithubDataFor(t.id)?.forks ?? 0);

    this.reposChartData = {
      labels,
      datasets: [{
        data: repos,
        backgroundColor: this.colors.slice(0, labels.length),
        borderRadius: 6,
        borderSkipped: false,
        barPercentage: 0.6
      }]
    };

    this.starsChartData = {
      labels,
      datasets: [{
        data: stars,
        backgroundColor: this.colors.slice(0, labels.length).map(c => c + '99'),
        borderColor: this.colors.slice(0, labels.length),
        borderWidth: 2
      }]
    };

    this.forksChartData = {
      labels,
      datasets: [{
        data: forks,
        backgroundColor: this.colors.slice(0, labels.length).map(c => c + '66'),
        borderColor: this.colors.slice(0, labels.length),
        borderWidth: 1
      }]
    };
  }
}
