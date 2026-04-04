import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Technology, GithubData, JobOffer, Page } from '../models/technology.model';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders(): HttpHeaders {
    const token = this.auth.getToken();
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }

  getTechnologies(): Observable<Page<Technology>> {
    return this.http.get<Page<Technology>>(`${this.baseUrl}/technologies?size=200`, { headers: this.getHeaders() });
  }

  getGithubData(): Observable<GithubData[]> {
    return this.http.get<GithubData[]>(`${this.baseUrl}/github-data`, { headers: this.getHeaders() });
  }

  getGithubDataByTechnology(id: number): Observable<GithubData[]> {
    return this.http.get<GithubData[]>(`${this.baseUrl}/github-data/technologies/${id}`, { headers: this.getHeaders() });
  }

  getStarsRatio(id: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/github-data/stars-ratio/${id}`, { headers: this.getHeaders() });
  }

  getJobOffers(): Observable<JobOffer[]> {
    return this.http.get<JobOffer[]>(`${this.baseUrl}/job-offers`, { headers: this.getHeaders() });
  }

  fetchGithubData(): Observable<string> {
    return this.http.post(`${this.baseUrl}/github-data/fetch`, {}, { headers: this.getHeaders(), responseType: 'text' });
  }

  createTechnology(name: string, type: string): Observable<Technology> {
    return this.http.post<Technology>(`${this.baseUrl}/technologies`, { name, type }, { headers: this.getHeaders() });
  }
}
