import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Technology, GithubData, JobOffer, Page } from '../models/technology.model';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

export interface GithubRepo {
  id: number;
  name: string;
  fullName: string;
  description: string;
  url: string;
  stars: number;
  forks: number;
  language: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient, private auth: AuthService) {}

  // Public endpoints - no auth needed
  getTechnologies(): Observable<Page<Technology>> {
    return this.http.get<Page<Technology>>(`${this.baseUrl}/technologies?size=200`);
  }

  getGithubData(): Observable<GithubData[]> {
    return this.http.get<GithubData[]>(`${this.baseUrl}/github-data`);
  }

  getReposByTechnology(technologyId: number): Observable<GithubRepo[]> {
    return this.http.get<GithubRepo[]>(`${this.baseUrl}/github-data/repos/${technologyId}`);
  }

  getJobOffers(): Observable<JobOffer[]> {
    return this.http.get<JobOffer[]>(`${this.baseUrl}/job-offers`);
  }

  fetchGithubData(): Observable<string> {
    return this.http.post(`${this.baseUrl}/github-data/fetch`, {}, { responseType: 'text' });
  }

  // Auth required endpoints
  createTechnology(name: string, type: string): Observable<Technology> {
    return this.http.post<Technology>(`${this.baseUrl}/technologies`, { name, type }, { headers: this.authHeaders() });
  }

  private authHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return token ? new HttpHeaders({ Authorization: `Bearer ${token}` }) : new HttpHeaders();
  }
}
