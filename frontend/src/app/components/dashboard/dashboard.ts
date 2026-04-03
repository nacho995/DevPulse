import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';
import { ChartData, ChartOptions } from 'chart.js';
import { ApiService } from '../../services/api.service';
import { Technology, GithubData, JobOffer } from '../../models/technology.model';

interface TechRanking {
  tech: Technology;
  data: GithubData | undefined;
  rank: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [BaseChartDirective, FormsModule],
  template: `
    <div class="dashboard">
      <header class="dash-header">
        <div class="header-text">
          <h1>Observatory</h1>
          <p class="subtitle">Real-time tech ecosystem intelligence — {{ technologies.length }} technologies tracked</p>
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
        <div class="metric-pill">
          <span class="metric-label">Job Offers</span>
          <span class="metric-value">{{ jobOffers.length }}</span>
        </div>
      </section>

      <!-- FILTERS -->
      <section class="filter-bar">
        <div class="filter-group">
          <label>Filter by type</label>
          <div class="filter-pills">
            <button class="fpill" [class.active]="filterType === 'all'" (click)="filterType = 'all'; applyFilters()">All</button>
            <button class="fpill" [class.active]="filterType === 'language'" (click)="filterType = 'language'; applyFilters()">Languages</button>
            <button class="fpill" [class.active]="filterType === 'framework'" (click)="filterType = 'framework'; applyFilters()">Frameworks</button>
            <button class="fpill" [class.active]="filterType === 'database'" (click)="filterType = 'database'; applyFilters()">Databases</button>
          </div>
        </div>
        <div class="filter-group">
          <label>Sort by</label>
          <div class="filter-pills">
            <button class="fpill" [class.active]="sortBy === 'repos'" (click)="sortBy = 'repos'; applyFilters()">Repos</button>
            <button class="fpill" [class.active]="sortBy === 'stars'" (click)="sortBy = 'stars'; applyFilters()">Stars</button>
            <button class="fpill" [class.active]="sortBy === 'forks'" (click)="sortBy = 'forks'; applyFilters()">Forks</button>
          </div>
        </div>
      </section>

      <!-- RANKING TABLE -->
      <section class="ranking-section">
        <h2 class="section-title">
          <span class="title-marker"></span>
          Technology Ranking
        </h2>
        <div class="ranking-list">
          @for (item of filteredRanking; track item.tech.id; let i = $index) {
            <div class="rank-row" [style.animation-delay]="i * 40 + 'ms'">
              <div class="rank-pos">{{ i + 1 }}</div>
              <div class="rank-name">
                <span class="rank-tech-name">{{ item.tech.name }}</span>
                <span class="rank-type-tag">{{ item.tech.type }}</span>
              </div>
              <div class="rank-bar-wrap">
                <div class="rank-bar" [style.width]="getBarWidth(getSortValue(item), maxSortValue)" [style.background]="getColor(i)"></div>
              </div>
              <div class="rank-value">{{ formatNumber(getSortValue(item)) }}</div>
            </div>
          }
        </div>
      </section>

      <!-- CHARTS -->
      <section class="charts-section">
        <h2 class="section-title">
          <span class="title-marker"></span>
          Visual Analytics
        </h2>
        <div class="chart-grid">
          <div class="chart-panel large">
            <div class="panel-header">
              <h3>Top 15 by Repositories</h3>
              <span class="panel-tag">bar</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="reposChartData" [options]="barOptions" type="bar"></canvas>
            </div>
          </div>
          <div class="chart-panel">
            <div class="panel-header">
              <h3>Stars Distribution (Top 10)</h3>
              <span class="panel-tag">doughnut</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="starsChartData" [options]="doughnutOptions" type="doughnut"></canvas>
            </div>
          </div>
          <div class="chart-panel">
            <div class="panel-header">
              <h3>Fork Activity (Top 10)</h3>
              <span class="panel-tag">polar</span>
            </div>
            <div class="chart-wrap">
              <canvas baseChart [data]="forksChartData" [options]="polarOptions" type="polarArea"></canvas>
            </div>
          </div>
        </div>
      </section>

      <!-- JOB OFFERS -->
      @if (jobOffers.length > 0) {
        <section class="cards-section">
          <h2 class="section-title">
            <span class="title-marker" style="background: var(--info)"></span>
            Latest Job Offers ({{ jobOffers.length }})
          </h2>
          <div class="jobs-grid">
            @for (job of jobOffers.slice(0, 12); track job.id) {
              <a [href]="job.url" target="_blank" class="job-card">
                <div class="job-title">{{ job.title }}</div>
                <div class="job-meta">
                  <span class="job-company">{{ job.company }}</span>
                  <span class="job-dot">&#183;</span>
                  <span class="job-location">{{ job.location }}</span>
                </div>
                <span class="job-source">{{ job.source }}</span>
              </a>
            }
          </div>
        </section>
      }
    </div>
  `,
  styles: [`
    .dashboard { max-width: 1400px; margin: 0 auto; padding: 2rem 2rem 4rem; }
    .dash-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 2rem; animation: fadeInUp 0.5s ease-out; }
    h1 { font-family: var(--font-display); font-size: 2.4rem; font-weight: 800; color: var(--text-primary); letter-spacing: -0.04em; line-height: 1; }
    .subtitle { color: var(--text-secondary); font-size: 0.95rem; margin-top: 0.4rem; }
    .refresh-btn { display: flex; align-items: center; gap: 0.5rem; padding: 0.6rem 1.4rem; background: var(--bg-card); border: 1px solid var(--border); color: var(--text-primary); border-radius: var(--radius-sm); font-family: var(--font-display); font-size: 0.85rem; font-weight: 600; cursor: pointer; transition: all 0.25s; }
    .refresh-btn:hover:not(:disabled) { border-color: var(--accent); color: var(--accent); }
    .refresh-btn:disabled { opacity: 0.5; }
    .refresh-icon { font-size: 1.1rem; }
    .spinning { animation: spin 1s linear infinite; }
    @keyframes spin { to { transform: rotate(360deg); } }

    .metrics-strip { display: flex; gap: 1rem; margin-bottom: 2rem; animation: fadeInUp 0.5s ease-out 0.1s both; flex-wrap: wrap; }
    .metric-pill { display: flex; align-items: center; gap: 0.6rem; padding: 0.6rem 1.2rem; background: var(--bg-surface); border: 1px solid var(--border); border-radius: 100px; }
    .metric-label { font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.06em; font-weight: 600; }
    .metric-value { font-family: var(--font-mono); font-size: 0.95rem; font-weight: 700; color: var(--text-primary); }
    .metric-value.accent { color: var(--accent); }

    .filter-bar { display: flex; gap: 2rem; margin-bottom: 2rem; padding: 1.2rem; background: var(--bg-card); border: 1px solid var(--border); border-radius: var(--radius-md); animation: fadeInUp 0.5s ease-out 0.15s both; flex-wrap: wrap; }
    .filter-group label { display: block; font-size: 0.7rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; font-weight: 600; margin-bottom: 0.5rem; }
    .filter-pills { display: flex; gap: 0.4rem; }
    .fpill { padding: 0.35rem 0.9rem; font-size: 0.8rem; font-family: var(--font-display); font-weight: 500; border: 1px solid var(--border); background: transparent; color: var(--text-secondary); border-radius: 100px; cursor: pointer; transition: all 0.2s; }
    .fpill:hover { border-color: var(--text-secondary); color: var(--text-primary); }
    .fpill.active { background: var(--accent); color: var(--bg-void); border-color: var(--accent); }

    .section-title { display: flex; align-items: center; gap: 0.7rem; font-size: 1.1rem; font-weight: 700; color: var(--text-primary); margin-bottom: 1.2rem; letter-spacing: -0.01em; }
    .title-marker { width: 3px; height: 18px; background: var(--accent); border-radius: 2px; }

    .ranking-section { margin-bottom: 3rem; }
    .ranking-list { display: flex; flex-direction: column; gap: 4px; }
    .rank-row { display: flex; align-items: center; gap: 1rem; padding: 0.6rem 1rem; background: var(--bg-card); border: 1px solid var(--border); border-radius: var(--radius-sm); animation: fadeInUp 0.3s ease-out both; transition: border-color 0.2s; }
    .rank-row:hover { border-color: var(--border-glow); }
    .rank-pos { font-family: var(--font-mono); font-size: 0.8rem; font-weight: 700; color: var(--text-muted); width: 28px; text-align: center; }
    .rank-name { display: flex; align-items: center; gap: 0.6rem; min-width: 160px; }
    .rank-tech-name { font-weight: 600; font-size: 0.9rem; color: var(--text-primary); }
    .rank-type-tag { font-family: var(--font-mono); font-size: 0.55rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; padding: 0.15rem 0.5rem; border: 1px solid var(--border); border-radius: 100px; }
    .rank-bar-wrap { flex: 1; height: 8px; background: var(--bg-void); border-radius: 4px; overflow: hidden; }
    .rank-bar { height: 100%; border-radius: 4px; transition: width 1s ease-out; min-width: 2px; }
    .rank-value { font-family: var(--font-mono); font-size: 0.85rem; font-weight: 700; color: var(--text-primary); min-width: 70px; text-align: right; }

    .charts-section { animation: fadeInUp 0.5s ease-out 0.3s both; margin-bottom: 3rem; }
    .chart-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
    .chart-panel { background: var(--bg-card); border: 1px solid var(--border); border-radius: var(--radius-md); padding: 1.4rem; }
    .chart-panel.large { grid-column: 1 / -1; }
    .panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
    .panel-header h3 { font-size: 0.95rem; font-weight: 600; color: var(--text-primary); }
    .panel-tag { font-family: var(--font-mono); font-size: 0.65rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.08em; padding: 0.2rem 0.6rem; border: 1px solid var(--border); border-radius: 4px; }
    .chart-wrap { position: relative; }

    .cards-section { margin-bottom: 3rem; }
    .jobs-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 0.8rem; }
    .job-card { display: block; background: var(--bg-card); border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 1rem 1.2rem; text-decoration: none; transition: all 0.2s; }
    .job-card:hover { border-color: var(--info); transform: translateY(-1px); }
    .job-title { color: var(--text-primary); font-weight: 600; font-size: 0.9rem; margin-bottom: 0.4rem; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    .job-meta { display: flex; align-items: center; gap: 0.4rem; font-size: 0.8rem; color: var(--text-secondary); }
    .job-dot { color: var(--text-muted); }
    .job-source { display: inline-block; margin-top: 0.5rem; font-family: var(--font-mono); font-size: 0.65rem; color: var(--info); text-transform: uppercase; letter-spacing: 0.06em; }

    @media (max-width: 768px) {
      .dashboard { padding: 1rem; }
      .dash-header { flex-direction: column; align-items: flex-start; gap: 1rem; }
      .filter-bar { flex-direction: column; gap: 1rem; }
      .rank-name { min-width: 100px; }
      .chart-grid { grid-template-columns: 1fr; }
      .chart-panel.large { grid-column: auto; }
    }
  `]
})
export class DashboardComponent implements OnInit {
  technologies: Technology[] = [];
  githubData: GithubData[] = [];
  jobOffers: JobOffer[] = [];
  filteredRanking: TechRanking[] = [];
  loading = false;
  totalRepos = 0;
  totalStars = 0;
  maxSortValue = 1;
  filterType = 'all';
  sortBy = 'repos';

  reposChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  starsChartData: ChartData<'doughnut'> = { labels: [], datasets: [] };
  forksChartData: ChartData<'polarArea'> = { labels: [], datasets: [] };

  private allColors = [
    '#00ffc8','#ff4466','#ffaa00','#4488ff','#aa66ff','#ff66aa','#44ddff','#88ff44',
    '#ff8844','#ff44ff','#44ffaa','#ffdd44','#8844ff','#ff4488','#44ff44','#dd44ff',
    '#44aaff','#ffaa44','#44ffdd','#ff6644','#66ff44','#4444ff','#ff44dd','#aaff44',
    '#ff4444','#44ff88','#ff88ff','#88ffaa','#ddff44','#ff44aa'
  ];

  barOptions: ChartOptions<'bar'> = {
    responsive: true, indexAxis: 'y', maintainAspectRatio: false,
    plugins: { legend: { display: false } },
    scales: {
      x: { ticks: { color: '#7878a0', font: { family: 'JetBrains Mono', size: 10 } }, grid: { color: '#1e1e4a' }, border: { display: false } },
      y: { ticks: { color: '#e8e8f0', font: { family: 'Outfit', size: 11 } }, grid: { display: false }, border: { display: false } }
    }
  };

  doughnutOptions: ChartOptions<'doughnut'> = {
    responsive: true, cutout: '60%',
    plugins: { legend: { position: 'right', labels: { color: '#7878a0', font: { family: 'Outfit', size: 11 }, padding: 12 } } }
  };

  polarOptions: ChartOptions<'polarArea'> = {
    responsive: true,
    plugins: { legend: { position: 'right', labels: { color: '#7878a0', font: { family: 'Outfit', size: 11 }, padding: 12 } } },
    scales: { r: { ticks: { display: false }, grid: { color: '#1e1e4a' } } }
  };

  constructor(private api: ApiService) {}

  ngOnInit() { this.loadData(); }

  loadData() {
    this.api.getTechnologies().subscribe(page => {
      this.technologies = page.content;
      this.api.getGithubData().subscribe(data => {
        this.githubData = data;
        this.computeMetrics();
        this.applyFilters();
        this.buildCharts();
      });
      this.api.getJobOffers().subscribe(jobs => this.jobOffers = jobs);
    });
  }

  refreshData() {
    this.loading = true;
    this.api.fetchGithubData().subscribe({
      next: () => setTimeout(() => { this.loadData(); this.loading = false; }, 5000),
      error: () => this.loading = false
    });
  }

  applyFilters() {
    let filtered = this.technologies;
    if (this.filterType !== 'all') {
      filtered = filtered.filter(t => t.type === this.filterType);
    }
    this.filteredRanking = filtered
      .map((tech, i) => ({ tech, data: this.getGithubDataFor(tech.id), rank: i }))
      .sort((a, b) => this.getSortValue(b) - this.getSortValue(a));
    this.maxSortValue = Math.max(...this.filteredRanking.map(r => this.getSortValue(r)), 1);
  }

  getSortValue(item: TechRanking): number {
    if (!item.data) return 0;
    switch (this.sortBy) {
      case 'stars': return item.data.stars;
      case 'forks': return item.data.forks;
      default: return item.data.repos;
    }
  }

  getGithubDataFor(techId: number): GithubData | undefined {
    return this.githubData.find(d => d.technologyId === techId);
  }

  getBarWidth(value: number, max: number): string {
    return max ? Math.round((value / max) * 100) + '%' : '0%';
  }

  getColor(i: number): string {
    return this.allColors[i % this.allColors.length];
  }

  formatNumber(n: number): string {
    if (!n) return '0';
    if (n >= 1_000_000) return (n / 1_000_000).toFixed(1) + 'M';
    if (n >= 1_000) return (n / 1_000).toFixed(1) + 'K';
    return n.toString();
  }

  private computeMetrics() {
    this.totalRepos = this.githubData.reduce((s, d) => s + d.repos, 0);
    this.totalStars = this.githubData.reduce((s, d) => s + d.stars, 0);
  }

  private buildCharts() {
    const sorted = [...this.technologies]
      .map(t => ({ t, d: this.getGithubDataFor(t.id) }))
      .filter(x => x.d)
      .sort((a, b) => (b.d!.repos) - (a.d!.repos));

    const top15 = sorted.slice(0, 15);
    const top10 = sorted.slice(0, 10);

    this.reposChartData = {
      labels: top15.map(x => x.t.name),
      datasets: [{ data: top15.map(x => x.d!.repos), backgroundColor: this.allColors.slice(0, 15), borderRadius: 4, barPercentage: 0.7 }]
    };

    this.starsChartData = {
      labels: top10.map(x => x.t.name),
      datasets: [{ data: top10.map(x => x.d!.stars), backgroundColor: this.allColors.slice(0, 10).map(c => c + '99'), borderColor: this.allColors.slice(0, 10), borderWidth: 2 }]
    };

    this.forksChartData = {
      labels: top10.map(x => x.t.name),
      datasets: [{ data: top10.map(x => x.d!.forks), backgroundColor: this.allColors.slice(0, 10).map(c => c + '66'), borderColor: this.allColors.slice(0, 10), borderWidth: 1 }]
    };
  }
}
