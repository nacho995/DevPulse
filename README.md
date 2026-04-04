# DevPulse

[![CI](https://github.com/nacho995/DevPulse/actions/workflows/ci.yml/badge.svg)](https://github.com/nacho995/DevPulse/actions/workflows/ci.yml)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-green)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-21-red)](https://angular.dev/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

> Real-time tech ecosystem observatory. Tracks 50+ technologies across GitHub — languages, frameworks, and databases — ranked by repos, stars, and forks.

**Live Demo:** [devpulse-frontend-0qpa.onrender.com](https://devpulse-frontend-0qpa.onrender.com)

---

## Features

- **Technology Ranking** — 50+ technologies ranked by repositories, stars, or forks with visual comparison bars
- **Filter by Type** — Toggle between languages, frameworks, and databases
- **Repository Explorer** — Click any technology to see its top 10 most starred GitHub repos with direct links
- **Job Offers** — Real job listings from The Muse API matched by technology
- **Visual Analytics** — Interactive charts (bar, doughnut, polar area) powered by Chart.js
- **Automated Data Sync** — Scheduler fetches GitHub data every 24h with authenticated API access (5000 req/hour)
- **Authentication** — User registration and login with JWT tokens
- **Swagger/OpenAPI** — Full API documentation at `/swagger-ui.html`

## Architecture

```
┌──────────────┐     ┌──────────────────────────────────────────────────┐
│   Angular    │────▶│                 Spring Boot API                  │
│   Frontend   │     │                                                  │
└──────────────┘     │  ┌────────────────────────────────────────────┐  │
                     │  │  infrastructure/adapter/in                  │  │
                     │  │  ├── web/         REST Controllers + DTOs   │  │
                     │  │  └── scheduler/   @Scheduled GitHub fetch   │  │
                     │  ├────────────────────────────────────────────┤  │
                     │  │  application/service                       │  │
                     │  │  └── Use case implementations              │  │
                     │  ├────────────────────────────────────────────┤  │
                     │  │  domain                                    │  │
                     │  │  ├── model/       Pure POJOs (no framework)│  │
                     │  │  └── port/        Interfaces (in/out)      │  │
                     │  ├────────────────────────────────────────────┤  │
                     │  │  infrastructure/adapter/out                 │  │
                     │  │  ├── persistence/ JPA entities + repos     │  │
                     │  │  ├── github/      GitHub API client        │  │
                     │  │  └── jobapi/      The Muse API client      │  │
                     │  └────────────────────────────────────────────┘  │
                     └──────────────────┬───────────────────────────────┘
                                        │
                              ┌─────────▼─────────┐
                              │    PostgreSQL      │
                              └───────────────────┘
```

**Hexagonal Architecture** — The domain layer has zero framework dependencies. Business logic is isolated from infrastructure concerns through ports (interfaces) and adapters (implementations). Swapping PostgreSQL for MongoDB or the GitHub API for GitLab requires no changes to the domain.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 4.0.5, Spring Data JPA, Hibernate 7 |
| Security | Spring Security, JWT (jjwt), BCrypt |
| Database | PostgreSQL 17 |
| Frontend | Angular 21, Chart.js, ng2-charts |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Testing | JUnit 5, Mockito |
| CI/CD | GitHub Actions |
| Infra | Docker, Docker Compose, Render |

## Getting Started

### Prerequisites

- Java 21 (JDK)
- Docker & Docker Compose
- Node.js 20+ and npm

### Setup

**1. Clone and start the database:**

```bash
git clone https://github.com/nacho995/DevPulse.git
cd DevPulse
docker compose up -d
```

**2. Configure environment (optional):**

```bash
cp .env.example .env
# Edit .env with your values
```

**3. Run the backend:**

```bash
cd backend
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`. Swagger UI at `http://localhost:8080/swagger-ui.html`.

**4. Run the frontend:**

```bash
cd frontend
npm install
npx ng serve
```

Dashboard at `http://localhost:4200`.

**5. Seed data and sync:**

```bash
# Add technologies
curl -X POST http://localhost:8080/api/technologies -H "Content-Type: application/json" -d '{"name":"Java","type":"language"}'
curl -X POST http://localhost:8080/api/technologies -H "Content-Type: application/json" -d '{"name":"Python","type":"language"}'
curl -X POST http://localhost:8080/api/technologies -H "Content-Type: application/json" -d '{"name":"React","type":"framework"}'

# Fetch GitHub data
curl -X POST http://localhost:8080/api/github-data/fetch
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5434/DevPulse` |
| `DB_USERNAME` | Database user | `postgres` |
| `DB_PASSWORD` | Database password | `123456` |
| `JWT_SECRET` | JWT signing key (min 32 chars) | — |
| `JWT_EXPIRATION` | Token expiration in ms | `86400000` (24h) |
| `GITHUB_TOKEN` | GitHub Personal Access Token | — (10 req/min without) |

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/api/technologies` | List all technologies (paginated) | Public |
| `POST` | `/api/technologies` | Create technology | JWT |
| `GET` | `/api/github-data` | All GitHub metrics | Public |
| `GET` | `/api/github-data/repos/{id}` | Top repos for a technology | Public |
| `POST` | `/api/github-data/fetch` | Trigger data sync | Public |
| `GET` | `/api/job-offers` | List job offers | Public |
| `POST` | `/api/auth/register` | Register user | Public |
| `POST` | `/api/auth/login` | Login, returns JWT | Public |

## Testing

```bash
cd backend
./mvnw test
```

Tests include:
- Unit tests for all mappers (Technology, JobOffer, GithubData)
- Service tests with Mockito mocks (GithubDataService stars ratio calculation)

## Project Structure

```
DevPulse/
├── backend/
│   └── src/main/java/com/devpulse/backend/
│       ├── domain/
│       │   ├── model/          # Technology, JobOffer, GithubData, User
│       │   └── port/
│       │       ├── in/         # Use case interfaces
│       │       └── out/        # Repository port interfaces
│       ├── application/
│       │   └── service/        # Use case implementations
│       └── infrastructure/
│           ├── adapter/
│           │   ├── in/
│           │   │   ├── web/    # REST controllers + DTOs + exception handlers
│           │   │   └── scheduler/  # GitHub data scheduler
│           │   └── out/
│           │       ├── persistence/  # JPA entities, repos, mappers, adapters
│           │       ├── github/       # GitHub API client
│           │       └── jobapi/       # The Muse API client
│           └── config/         # Security, JWT, CORS
├── frontend/
│   └── src/app/
│       ├── components/         # Dashboard, Login, Navbar, Repos
│       ├── services/           # API and Auth services
│       └── models/             # TypeScript interfaces
├── docker-compose.yml
├── .github/workflows/ci.yml
└── .env.example
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is open source under the [MIT License](LICENSE).
