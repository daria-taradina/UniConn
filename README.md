#  UniConn :unicorn: 

**A (Uni)versity (Conn)ection platform for CSUN students.**  
Discover communities, spark conversations, and stay in the loop with everything campus.

[![Live](https://img.shields.io/badge/Live%20App-uniconn--backend--production.up.railway.app-4A90D9?style=for-the-badge&logo=railway&logoColor=white)](https://uniconn-backend-production.up.railway.app)

*Built with💙 by fellow Matadors — COMP 380/L · Spring 2026 · CSUN*



## 💻 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.3.5, Spring Security, Hibernate/JPA |
| Database | MySQL (Railway managed) |
| Frontend | HTML, CSS, JS (Thymeleaf) |
| Hosting ☁️ | Railway (backend + DB), Cloudinary (media) |


## ⚙️ System Architecture

<img width="635" alt="UniConn System Architecture" src="https://github.com/user-attachments/assets/1cdbde73-acbc-4795-a028-3e72557e35a9" />

## ✨ Features

| Feature | Description | 
|---|---|
| **User Authentication** 🔐 | JWT-based login, registration, password reset via secret question |
| **User Profiles** 👤 | View and edit profile, view posts made by user across the platform and posts they like | 
| **User Connections** 👥 | Follow/unfollow users, follower and following counts & lists | 
| **Communities** 🧩 | Browse, create, moderate, join communities with categories and tags | 
| **Post Feed** 👣 | Personalized feed, trending tags, community and profile post views | 
| **Post Interactions** 💙💬 | Create posts with GIFs, like, comment, share link, tag filtering | 
| **Image Upload** 📷 | Profile and community pictures via Cloudinary CDN | 
| **Dark Mode** 🔅🌚 | System-aware dark/light theme toggle | 
| **Search** 🔍 | Search users and communities | 

## 🗂 Project Structure

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
│   │   ├── utils/              # JWT
│   │   └── validation/         # Custom annotation validators (tags)
│   └── resources/
│       ├── db/                 # SQL seed scripts (run manually in Workbench)
│       ├── static/
│       │   ├── css/            # Stylesheets
│       │   ├── js/             # Client-side feature logic
│       │   └── vector-logos/   # Custom design icons
│       ├── templates/                        # Thymeleaf HTML pages (organized by feature)
│       ├── application.properties            # Railway config
│       └── application.properties.template   # Local setup template

```


## 🌥 External Integrations

| Service | Purpose | Docs |
|---|---|---|
| [Railway](https://railway.app) | Cloud deployment and managed MySQL database | [Docs](https://docs.railway.app) |
| [Cloudinary](https://cloudinary.com) | Image upload, storage, and CDN delivery | [Docs](https://cloudinary.com/documentation) |
| [Giphy API](https://developers.giphy.com) | GIF search and embedding in posts | [Docs](https://developers.giphy.com/docs/api/) |


## 💕 Contributors

<table>
  <tr>
    <td align="center" width="340">
      <a href="https://github.com/dariatarad">
        <img src="https://github.com/dariatarad.png?size=150" width="90" alt="Daria Taradina" />
      </a>
      <br /><br />
      <a href="https://github.com/dariatarad">
        <img src="https://img.shields.io/badge/dariatarad-e8b4d0?style=for-the-badge&logo=github&logoColor=333" />
      </a>
      <br /><br />
      <sub>Tech Lead · Backend · Cloud & Deployment</sub>
    </td>
    <td align="center" width="340">
      <a href="https://github.com/rachelhann">
        <img src="https://github.com/rachelhann.png?size=150" width="90" alt="Rachel Hanna" />
      </a>
      <br /><br />
      <a href="https://github.com/rachelhann">
        <img src="https://img.shields.io/badge/rachelhann-c9b8e8?style=for-the-badge&logo=github&logoColor=333" />
      </a>
      <br /><br />
      <sub>Design Lead · Frontend · UI/UX</sub>
    </td>
    <td align="center" width="340">
      <a href="https://github.com/dariatarad">
        <img src="https://github.com/LilyF217.png?size=150" width="90" alt="Daria Taradina" />
      </a>
      <br /><br />
      <a href="https://github.com/LilyF217">
        <img src="https://img.shields.io/badge/LilyF217-9fc5e8?style=for-the-badge&logo=github&logoColor=333" />
      </a>
      <br /><br />
      <sub>Security · Auth</sub>
    </td>
  </tr>
</table>
