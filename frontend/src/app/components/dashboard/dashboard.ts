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
      <div class="header">
        <h1>Tech Ecosystem Dashboard</h1>
        <button (click)="refreshData()" class="btn-refresh" [disabled]="loading">
          {{ loading ? 'Fetching...' : 'Refresh GitHub Data' }}
        </button>
      </div>

      <div class="stats-grid">
        @for (tech of technologies; track tech.id) {
          <div class="stat-card">
            <h3>{{ tech.name }}</h3>
            <span class="badge">{{ tech.type }}</span>
            @if (getGithubDataFor(tech.id); as data) {
              <div class="stat-numbers">
                <div class="stat">
                  <span class="stat-value">{{ formatNumber(data.repos) }}</span>
                  <span class="stat-label">Repos</span>
                </div>
                <div class="stat">
                  <span class="stat-value">{{ formatNumber(data.stars) }}</span>
                  <span class="stat-label">Stars</span>
                </div>
                <div class="stat">
                  <span class="stat-value">{{ formatNumber(data.forks) }}</span>
                  <span class="stat-label">Forks</span>
                </div>
              </div>
            }
          </div>
        }
      </div>

      <div class="charts-grid">
        <div class="chart-card">
          <h3>Repositories by Language</h3>
          <canvas baseChart
            [data]="reposChartData"
            [options]="chartOptions"
            type="bar">
          </canvas>
        </div>
        <div class="chart-card">
          <h3>Stars Comparison</h3>
          <canvas baseChart
            [data]="starsChartData"
            [options]="chartOptions"
            type="bar">
          </canvas>
        </div>
        <div class="chart-card">
          <h3>Forks Distribution</h3>
          <canvas baseChart
            [data]="forksChartData"
            [options]="doughnutOptions"
            type="doughnut">
          </canvas>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard { padding: 2rem; max-width: 1200px; margin: 0 auto; }
    .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
    h1 { color: #fff; font-size: 1.8rem; }
    .btn-refresh {
      background: #00d4aa;
      color: #0f0f23;
      border: none;
      padding: 0.6rem 1.5rem;
      border-radius: 8px;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .btn-refresh:hover { background: #00b894; }
    .btn-refresh:disabled { opacity: 0.5; cursor: not-allowed; }
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 1.2rem;
      margin-bottom: 2rem;
    }
    .stat-card {
      background: #16213e;
      border-radius: 12px;
      padding: 1.5rem;
      border: 1px solid #2a2a4a;
    }
    .stat-card h3 { color: #fff; margin: 0 0 0.5rem; }
    .badge {
      display: inline-block;
      background: #0f3460;
      color: #00d4aa;
      padding: 0.2rem 0.8rem;
      border-radius: 20px;
      font-size: 0.75rem;
      text-transform: uppercase;
      margin-bottom: 1rem;
    }
    .stat-numbers { display: flex; gap: 1.2rem; margin-top: 0.8rem; }
    .stat { display: flex; flex-direction: column; }
    .stat-value { color: #fff; font-size: 1.2rem; font-weight: 700; }
    .stat-label { color: #a0a0b8; font-size: 0.75rem; }
    .charts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 1.5rem;
    }
    .chart-card {
      background: #16213e;
      border-radius: 12px;
      padding: 1.5rem;
      border: 1px solid #2a2a4a;
    }
    .chart-card h3 { color: #fff; margin: 0 0 1rem; }
  `]
})
export class DashboardComponent implements OnInit {
  technologies: Technology[] = [];
  githubData: GithubData[] = [];
  loading = false;

  reposChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  starsChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  forksChartData: ChartData<'doughnut'> = { labels: [], datasets: [] };

  chartOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: false } },
    scales: {
      y: { ticks: { color: '#a0a0b8' }, grid: { color: '#2a2a4a' } },
      x: { ticks: { color: '#a0a0b8' }, grid: { display: false } }
    }
  };

  doughnutOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { labels: { color: '#a0a0b8' } } }
  };

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.api.getTechnologies().subscribe(page => {
      this.technologies = page.content;
      this.api.getGithubData().subscribe(data => {
        this.githubData = data;
        this.buildCharts();
      });
    });
  }

  refreshData() {
    this.loading = true;
    this.api.fetchGithubData().subscribe({
      next: () => {
        setTimeout(() => {
          this.loadData();
          this.loading = false;
        }, 2000);
      },
      error: () => this.loading = false
    });
  }

  getGithubDataFor(techId: number): GithubData | undefined {
    return this.githubData.find(d => d.technologyId === techId);
  }

  formatNumber(n: number): string {
    if (n >= 1_000_000) return (n / 1_000_000).toFixed(1) + 'M';
    if (n >= 1_000) return (n / 1_000).toFixed(1) + 'K';
    return n.toString();
  }

  private buildCharts() {
    const colors = ['#00d4aa', '#e74c3c', '#f39c12', '#3498db', '#9b59b6', '#1abc9c'];
    const labels = this.technologies.map(t => t.name);
    const repos = this.technologies.map(t => this.getGithubDataFor(t.id)?.repos ?? 0);
    const stars = this.technologies.map(t => this.getGithubDataFor(t.id)?.stars ?? 0);
    const forks = this.technologies.map(t => this.getGithubDataFor(t.id)?.forks ?? 0);

    this.reposChartData = {
      labels,
      datasets: [{ data: repos, backgroundColor: colors, borderRadius: 6 }]
    };

    this.starsChartData = {
      labels,
      datasets: [{ data: stars, backgroundColor: colors, borderRadius: 6 }]
    };

    this.forksChartData = {
      labels,
      datasets: [{ data: forks, backgroundColor: colors }]
    };
  }
}
