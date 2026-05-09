<div align="center">

<img src="src/main/resources/static/vector-logos/unicornLogo.png" width="80" alt="UniConn Logo" />   

# UniConn

**A (Uni)versity (Conn)ection platform for CSUN students.**  
Discover communities, spark conversations, and stay in the loop with everything campus.

[![Live](https://img.shields.io/badge/Live%20App-uniconn--backend--production.up.railway.app-4A90D9?style=for-the-badge&logo=railway&logoColor=white)](https://uniconn-backend-production.up.railway.app)

*Built with ❤️ by fellow Matadors — COMP 380/L · Spring 2026 · CSUN*

</div>


## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.3.5, Spring Security, Hibernate/JPA |
| Database | MySQL (Railway managed) |
| Frontend | HTML, CSS, Vanilla JS (Thymeleaf) |
| Auth | JWT |
| Hosting | Railway (backend + DB), Cloudinary (media) |


## System Architecture

<img width="3435" alt="UniConn System Architecture" src="https://github.com/user-attachments/assets/c175489b-556c-4020-9671-c45519e65b72" />


## Features

| Feature | Description | Status |
|---|---|---|
| **User Profiles** | View and edit profile, profile picture upload via Cloudinary | ✅ Complete |
| **User Connections** | Follow/unfollow users, follower and following counts | ✅ Complete |
| **Communities** | Browse, create, join communities with categories and tags | ✅ Complete |
| **Community Roles** | `ADMIN`, `MODERATOR`, `REGULAR_MEMBER` role management | ✅ Complete |
| **Post Feed** | Personalized feed, trending tags, community and profile post views | ✅ Complete |
| **Post Interactions** | Create posts with GIFs, like, comment, share link, tag filtering | ✅ Complete |
| **Image Upload** | Profile and community pictures via Cloudinary CDN | ✅ Complete |
| **Dark Mode** | System-aware dark/light theme toggle | ✅ Complete |
| **Search** | Search users and communities | ✅ Complete |
| **Notifications** | In-app notification system | 🔜 Planned |

## Project Structure

```
src/
├── main/
│   ├── java/com/uniconn/backend/
│   │   ├── composite_keys/     # Composite PKs for join tables (UserFollowId, PostLikeId, ...)
│   │   ├── config/             # Security and Cloudinary configuration
│   │   ├── controllers/        # REST controllers
│   │   ├── dtos/               # Data transfer objects (request/response)
│   │   ├── entities/           # JPA entities (User, Post, Community, ...)
│   │   ├── exception/          # GlobalExceptionHandler and custom exceptions
│   │   ├── repositories/       # Spring Data JPA repositories
│   │   ├── services/           # Business logic
│   │   └── validation/         # Custom annotation validators (tags)
│   └── resources/
│       ├── db/                 # SQL seed scripts (run manually in Workbench)
│       ├── static/
│       │   ├── css/
│       │   ├── js/
│       │   └── vector-logos/
│       ├── templates/          # Thymeleaf HTML pages (organized by feature)
│       ├── application.properties          # Railway/production config
│       └── application.properties.template # Local setup template
```


## External Integrations

| Service | Purpose | Docs |
|---|---|---|
| [Railway](https://railway.app) | Cloud deployment and managed MySQL database | [Docs](https://docs.railway.app) |
| [Cloudinary](https://cloudinary.com) | Image upload, storage, and CDN delivery | [Docs](https://cloudinary.com/documentation) |
| [Giphy API](https://developers.giphy.com) | GIF search and embedding in posts | [Docs](https://developers.giphy.com/docs/api/) |



## Image Upload

Image upload is a two-step process to keep file handling separate from business logic:

1. **Upload** — send the file to the upload endpoint, receive a Cloudinary URL
2. **Save** — include the returned URL in the profile or community update request

**Requirements:** JPEG, PNG, or WebP · Max 2MB · Auto-cropped to 256×256px

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/upload/user` | Upload user profile picture |
| `POST` | `/api/upload/community` | Upload community picture |
| `PATCH` | `/api/users/me/picture` | Save picture URL to user account |
| `PATCH` | `/api/community/{communityId}/picture` | Save picture URL to community |

All endpoints require `Authorization: Bearer <token>`.



## Local Development

<details>
<summary>Setup instructions (click to expand)</summary>

### Prerequisites

- [Java 21+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [MySQL 8+](https://dev.mysql.com/downloads/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) or Eclipse

### Steps

**1. Clone the repository**

```bash
git clone https://github.com/rachelhann/UniConn-COMP-380.git
```

**2. Create the database**

```sql
CREATE DATABASE uni_conn;
```

**3. Configure `application.properties`**

Copy `application.properties.template` → `application.properties` and fill in your credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/uni_conn
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**4. Run**

```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080`.

</details>



## Test Data

Sample seed data is in `src/main/resources/db/`. Run scripts in order in MySQL Workbench:

1. `users_test_data.sql`
2. `community_test_data.sql`
3. `posts_test_data.sql`

**Summary:** 20 users · 20 tags · 10 communities · posts with likes and comments  
All test accounts use password `Password123!` with `@my.csun.edu` emails. Verification queries are included in each file.


## Configuration Notes

> [!IMPORTANT]
> Set `spring.jpa.hibernate.ddl-auto=update` on first run so Hibernate creates all tables.
> After verifying the schema, switch to `validate` to prevent unintended modifications.

- All student emails must end with `@my.csun.edu`
- Passwords are stored as BCrypt hashes
- Naming conventions: `snake_case` in SQL · `camelCase` in Java · `kebab-case` on GitHub



## Contributing

> [!CAUTION]
> Do not edit entities or composite keys without coordinating with the owner. Leave comments if changes are needed — don't modify someone else's code directly.

- Branch off `main` using `feature/your-feature-name`
- Use [conventional commit messages](https://gist.github.com/qoomon/5dfcdf8eec66a051ecd85625518cfd13)
- Open a PR using the [PR template](PULL_REQUEST_TEMPLATE.md) — PRs are reviewed before merging
- Frontend HTML/CSS changes merge directly to `src/resources/static`


## Contributors

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/dariatarad">
        <img src="https://github.com/dariatarad.png" width="80" style="border-radius:50%" alt="Daria Taradina" /><br />
        <sub><b>Daria Taradina</b></sub>
      </a><br />
      <sub>Backend Architecture · Communities · Posts · User Connections · DevOps</sub>
    </td>
    <td align="center">
      <a href="https://github.com/rachelhann">
        <img src="https://github.com/rachelhann.png" width="80" style="border-radius:50%" alt="Rachel Hanna" /><br />
        <sub><b>Rachel Hanna</b></sub>
      </a><br />
      <sub>Frontend · Auth · Dark Mode · Notifications · UI/UX</sub>
    </td>
  </tr>
</table>
