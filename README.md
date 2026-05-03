<div align="center">

<img src="React-Frontend/public/favicon.svg" alt="LeetBoard Logo" width="64" height="64"/>

# LeetBoard

**A competitive LeetCode progress tracker with real-time leaderboards, OAuth2 authentication, and automated stats sync.**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev)
[![Vite](https://img.shields.io/badge/Vite-8-646CFF?style=flat-square&logo=vite&logoColor=white)](https://vitejs.dev)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-4169E1?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

[Features](#-features) · [Architecture](#-architecture) · [Getting Started](#-getting-started) · [API Reference](#-api-reference) · [Configuration](#-configuration) · [Roadmap](#-roadmap)

</div>

---

## Overview

LeetBoard is a full-stack web application that lets developers track and compare their LeetCode progress in real time. Sign in with Google, link your LeetCode username, and instantly appear on a ranked leaderboard sorted by difficulty-weighted solve count. Stats are automatically refreshed every 6 hours via a scheduled background job.

Built for engineering students and competitive programmers who want accountability and visibility into their coding practice.

---

## ✨ Features

| Feature | Details |
|---|---|
| **Google OAuth2 Login** | One-click sign-in, no passwords stored |
| **LeetCode Account Linking** | Validated against the live LeetCode GraphQL API at onboarding |
| **Personal Dashboard** | Easy / Medium / Hard breakdown with total solve count |
| **Global Leaderboard** | Ranked by Hard → Medium → Easy solve count |
| **Automated Sync** | Cron job refreshes all user stats every 6 hours |
| **JWT Authentication** | Stateless, token-based sessions with 24h expiry |
| **Responsive UI** | Mobile-first design, works on all screen sizes |

---

## 🏗 Architecture

```
┌───────────────────────────────────────────────────────────────┐
│                        Browser (React 19)                     │
│                                                               │
│   LandingPage → OAuth Redirect → OnboardingPage → Dashboard  │
│                                      ↓                        │
│                              LeaderboardPage                  │
└─────────────────────┬─────────────────────────────────────────┘
                      │  HTTP / REST (JWT Bearer)
                      │  Vite Proxy → /api → :8080
┌─────────────────────▼─────────────────────────────────────────┐
│                  Spring Boot Backend (:8080)                   │
│                                                               │
│  SecurityConfig (JWT Filter + OAuth2 Success Handler)         │
│       │                                                       │
│  AuthController        UserStatsController                    │
│       │                       │                              │
│  AuthService           UserStatsService                       │
│       │                       │                              │
│  LeetCodeService ──────────────┘                              │
│       │                                                       │
│  LeetCodeClient (WebFlux → LeetCode GraphQL)                  │
│       │                                                       │
│  UserStatsScheduler (every 6 hours)                          │
└─────────────────────┬─────────────────────────────────────────┘
                      │  JPA / Hibernate
┌─────────────────────▼─────────────────────────────────────────┐
│                    PostgreSQL                                  │
│                                                               │
│   users              user_stats                               │
│   ─────────          ──────────                               │
│   id (PK)            id (PK)                                  │
│   username           user_id (FK → users)                    │
│   email (UQ)         leetcode_username (UQ)                   │
│   leet_username (UQ) easy_count                               │
│   course             medium_count                             │
│   semester           hard_count                               │
│   is_onboarded       last_sync                               │
└───────────────────────────────────────────────────────────────┘
```

### Data Flow — Authentication

```
User clicks "Continue with Google"
        │
        ▼
Spring OAuth2 → Google Authorization Server
        │
        ▼ (callback)
OAuth2SuccessHandler
  ├─ Find or create Users record
  ├─ Generate JWT (24h, signed HS256)
  └─ Redirect → /onboarding?token=... or /dashboard?token=...
        │
        ▼
React reads token from URL, stores in localStorage, strips from URL
        │
        ▼
All subsequent API calls: Authorization: Bearer <token>
        │
        ▼
JwtAuthFilter validates token → populates SecurityContext
```

---

## 🗂 Project Structure

```
LeetBoard/
├── React-Frontend/
│   ├── src/
│   │   ├── Api/
│   │   │   └── axios.js              # Axios instance with JWT interceptors
│   │   ├── Components/
│   │   │   ├── Navbar.jsx
│   │   │   └── StatCard.jsx
│   │   ├── Pages/
│   │   │   ├── LandingPage.jsx
│   │   │   ├── OnboardingPage.jsx
│   │   │   ├── DashboardPage.jsx
│   │   │   └── LeaderboardPage.jsx
│   │   ├── App.jsx                   # Route definitions + auth guards
│   │   ├── App.css
│   │   ├── index.css                 # Design tokens & global styles
│   │   └── main.jsx
│   ├── public/
│   │   ├── favicon.svg
│   │   └── icons.svg
│   └── vite.config.js                # Dev proxy → :8080
│
└── Spring-Boot-Backend/
    └── src/main/java/com/example/leetboardPro/
        ├── Client/
        │   └── LeetCodeClient.java   # WebFlux GraphQL client
        ├── Config/
        │   └── CorsConfig.java
        ├── Controller/
        │   ├── AuthController.java
        │   └── UserStatsController.java
        ├── DTO/
        │   ├── AuthResponseDTO.java
        │   ├── LeetCodeRawData.java
        │   ├── OnboardingRequestDTO.java
        │   └── UserStatsDTO.java
        ├── Mapper/
        │   └── UserStatsMapper.java
        ├── Model/
        │   ├── Users.java
        │   └── UserStats.java
        ├── Repository/
        │   ├── UserRepo.java
        │   └── UserStatsRepository.java
        ├── Scheduler/
        │   └── UserStatsScheduler.java
        ├── Security/
        │   ├── JwtAuthFilter.java
        │   ├── JwtUtil.java
        │   ├── OAuth2SuccessHandler.java
        │   └── SecurityConfig.java
        └── Service/
            ├── AuthService.java
            ├── LeetCodeService.java
            └── UserStatsService.java
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version |
|---|---|
| Java (JDK) | 21+ (project targets Java 25) |
| Maven | 3.9+ (wrapper included) |
| Node.js | 20+ |
| PostgreSQL | 14+ |
| Google OAuth2 credentials | [console.cloud.google.com](https://console.cloud.google.com) |

---

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/leetboard.git
cd leetboard
```

---

### 2. Database Setup

```sql
-- Connect to PostgreSQL and run:
CREATE DATABASE leetboard;
```

---

### 3. Backend Configuration

Create `Spring-Boot-Backend/src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/leetboard
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# OAuth2 — Google
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
spring.security.oauth2.client.registration.google.scope=email,profile

# JWT
# Generate: openssl rand -base64 64
jwt.secret=YOUR_BASE64_ENCODED_SECRET_KEY_AT_LEAST_512_BITS
```

> **Getting Google OAuth2 credentials:**
> 1. Go to [Google Cloud Console](https://console.cloud.google.com) → APIs & Services → Credentials
> 2. Create an OAuth 2.0 Client ID (Web application)
> 3. Add `http://localhost:8080/login/oauth2/code/google` as an authorized redirect URI
> 4. Add `http://localhost:3000` as an authorized JavaScript origin

---

### 4. Start the Backend

```bash
cd Spring-Boot-Backend
./mvnw spring-boot:run
# Windows: mvnw.cmd spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

### 5. Start the Frontend

```bash
cd React-Frontend
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

---

## 📡 API Reference

All protected endpoints require `Authorization: Bearer <token>`.

### Authentication

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/oauth2/authorization/google` | Public | Initiates Google OAuth2 flow |
| `POST` | `/api/auth/onboarding` | Bearer | Links LeetCode account & sets course/semester |

**POST `/api/auth/onboarding`** — Request body:
```json
{
  "leetUsername": "neetcode",
  "course": "BCA",
  "semester": 3
}
```

---

### Stats

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `GET` | `/api/stats` | Bearer | All users' stats |
| `GET` | `/api/stats/{userId}` | Bearer | Stats for a specific user by their internal ID |
| `GET` | `/api/stats/leaderboard` | Bearer | All users sorted by Hard → Medium → Easy |

**GET `/api/stats/leaderboard`** — Response:
```json
[
  {
    "leetcodeUsername": "neetcode",
    "easyCount": 120,
    "mediumCount": 95,
    "hardCount": 42,
    "lastSync": "2025-05-03T10:00:00",
    "user": {
      "id": 1,
      "username": "Nick White",
      "email": "nick@example.com",
      "course": "BCA",
      "semester": 3,
      "onboarded": true
    }
  }
]
```

---

## ⚙️ Configuration

### Scheduler

The background sync job runs every 6 hours via Spring `@Scheduled`:

```java
// UserStatsScheduler.java
@Scheduled(cron = "0 0 */6 * * *")
public void refreshGlobalLeaderBoard() { ... }
```

To change the interval, update the cron expression or externalize it to `application.properties`:

```properties
scheduler.stats-sync.cron=0 0 */6 * * *
```

### CORS

Allowed origins are configured in `CorsConfig.java`. For production, replace:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "https://your-production-domain.com"
));
```

### Vite Proxy

The frontend proxies all `/api` requests to the backend during development (`vite.config.js`). In production, configure your reverse proxy (Nginx, etc.) instead.

---

## 🧩 Tech Stack

### Backend
- **Spring Boot 4.0.5** — Application framework
- **Spring Security** — OAuth2 + JWT stateless authentication
- **Spring Data JPA / Hibernate** — ORM with PostgreSQL
- **Spring WebFlux (WebClient)** — Non-blocking HTTP client for LeetCode GraphQL API
- **JJWT 0.12.6** — JWT creation and validation
- **Lombok** — Boilerplate reduction
- **PostgreSQL** — Primary database

### Frontend
- **React 19** — UI library
- **Vite 8** — Build tool and dev server
- **React Router DOM 7** — Client-side routing
- **Axios** — HTTP client with interceptors
- **jwt-decode** — Client-side JWT parsing
- **lucide-react** — Icon library

---

## 🐛 Known Issues & Limitations

- **Route order bug:** `/api/stats/leaderboard` is defined after `/api/stats/{id}` in `UserStatsController`. Spring MVC resolves this correctly (literal paths take precedence), but the leaderboard endpoint should be declared first as a best practice to avoid ambiguity.
- **JWT not refreshed post-onboarding:** After completing onboarding, the JWT still contains `isOnboarded: false`. The frontend redirects correctly but the token payload is stale until the next login.
- **Exposed `Users` entity in DTO:** `UserStatsDTO.user` is the raw JPA entity, which risks serializing internal fields. A dedicated `UserSummaryDTO` should be used instead.
- **`semester` type mismatch:** The `OnboardingPage` sends `semester` as a string from the input field; the backend `OnboardingRequestDTO` expects `Integer`. Ensure frontend coerces to a number or the backend handles parsing gracefully.
- **No rate limiting on LeetCode API calls:** The scheduler adds a 1.5s sleep between users, but there's no circuit breaker or retry logic for transient API failures.

---

## 🗺 Roadmap

- [ ] Refresh JWT token after onboarding completion
- [ ] Replace raw `Users` entity in `UserStatsDTO` with a `UserSummaryDTO`
- [ ] Add pagination to the leaderboard endpoint
- [ ] Implement manual "Sync Now" button for users
- [ ] Add acceptance rate and submission history charts
- [ ] Dockerize the full stack with `docker-compose`
- [ ] CI/CD pipeline with GitHub Actions
- [ ] Add filtering by course and semester on the leaderboard
- [ ] Configurable scheduler interval via `application.properties`
- [ ] Add circuit breaker (Resilience4j) for LeetCode API calls

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feat/your-feature`
5. Open a Pull Request

Please follow [Conventional Commits](https://www.conventionalcommits.org/) for commit messages.

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

<div align="center">
  <sub>Built with ☕ and LeetCode pain.</sub>
</div>