export interface Technology {
  id: number;
  name: string;
  type: string;
  createdAt: string;
}

export interface GithubData {
  id: number;
  technologyId: number;
  stars: number;
  forks: number;
  repos: number;
  createdAt: string;
}

export interface JobOffer {
  id: number;
  title: string;
  company: string;
  location: string;
  source: string;
  url: string;
  modality: string;
  publishedAt: string;
  createdAt: string;
}

export interface AuthResponse {
  token: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
