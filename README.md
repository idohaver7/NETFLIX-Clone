# Netflix Clone

[![Build Status](https://img.shields.io/github/workflow/status/idohaver7/NETFLIX-Clone/CI)](https://github.com/idohaver7/NETFLIX-Clone/actions)
[![License](https://img.shields.io/github/license/idohaver7/NETFLIX-Clone)](LICENSE)
[![Issues](https://img.shields.io/github/issues/idohaver7/NETFLIX-Clone)](https://github.com/idohaver7/NETFLIX-Clone/issues)

A cross-platform Netflix clone built with React (Web), Android (Mobile), and Node.js (Backend).  
Replicates core Netflix features: streaming, recommendations, user authentication, and more.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Technologies](#technologies)
4. [Screenshots](#screenshots)
5. [Project Structure](#project-structure)
6. [Getting Started](#getting-started)
7. [API Documentation](#api-documentation)
8. [Contributing](#contributing)
9. [License](#license)
10. [Authors](#authors)
11. [Support](#support)

---

## Overview

Developed as a capstone project for an Advanced System Programming course, this Netflix Clone demonstrates the integration of multiple technologies into a full-stack streaming platform. The project emphasizes practical skills in system programming, client-server communication, and collaborative development.

- **Frontend:**  
  - **React Web App:** Delivers a dynamic and responsive UI for desktop users, closely mimicking the Netflix experience.
  - **Android Mobile App:** Built using Android Studio (Java), the mobile client provides a native experience for Android devices, with custom layouts and optimized user flows for touch interfaces.
- **Backend:**  
  - Node.js and Express.js power the API server, handling authentication, recommendations, and data management.
  - The backend server is designed to support multithreading, enabling efficient handling of concurrent requests and improved scalability.
  - MongoDB Atlas enables scalable, cloud-hosted storage for movies, user profiles, and categories.
- **Educational Focus:**  
  - The project applies advanced system programming concepts such as networking, concurrency (including multithreaded server design), secure authentication, and API design.
  - Development included collaboration, modular coding, and version control practices.
- **Key Features:**  
  - Real-time video streaming, personalized recommendations, secure user authentication, and an admin panel for content management.
  - DevOps integration using Docker, Docker Compose, and CI/CD for streamlined deployment.

This Netflix Clone serves both as a technical showcase and a learning platform for mastering system programming and full-stack development in a real-world scenario.
## Technologies

- **Frontend**: React (Hooks, Context API)
- **Mobile**: Android (Java)
- **Backend**: Node.js, Express.js
- **Database**: MongoDB (Atlas)
- **Auth**: JWT (JSON Web Tokens)
- **DevOps**: Docker, Docker Compose

---

## Screenshots

### Web App

| Home Page | Register | Sign In | Logged In |
|---|---|---|---|
| ![Home](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/homepage.png) | ![Register](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/register.png) | ![Sign In](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/signin.png) | ![Logged In](https://github.com/NirPerets/Advanced-EX4/blob/main/netflix-client/example/logged.png) |

### Android App

| Home Page | Sign Up | Login | After Login |
|---|---|---|---|
| ![Home](https://github.com/user-attachments/assets/35ae55ae-3029-4943-832d-0feaddf19031) | ![Sign Up](https://github.com/user-attachments/assets/d729d088-8b70-4ac6-b325-c2e99330f1e4) | ![Login](https://github.com/user-attachments/assets/f7fc57b5-b198-477d-b3a5-d894261b0dbd) | ![After Login](https://github.com/user-attachments/assets/f28e7d8d-a0e9-4fed-ac5c-a13e41ba4707) |

---

## Project Structure

```
/Server          # Node.js Backend & API
/netflix-client  # React Frontend
/AndroidApp      # Android Mobile App
```

---

## Getting Started

### Prerequisites

- Node.js & npm
- Docker & Docker Compose (optional)
- Android Studio
- MongoDB Atlas (already filled with sample data)

### Installation

#### Backend (Node.js)

**With Docker:**
```bash
cd Server
docker-compose build  
docker-compose up -d
docker-compose logs -f
```
**Without Docker:**
```bash
cd Server
# Terminal 1
cd server
npm install
NODE_ENV=local node app.js

# Terminal 2
cd tcpServer
make
./server
```

#### Web Frontend (React)

**With Docker:**
```bash
cd netflix-client
docker build -t netflix-client .
docker run -p 4000:3000 netflix-client
```
**Without Docker:**
```bash
cd netflix-client
npm install
npm start
```

#### Mobile App (Android)

- Open `/AndroidApp` in Android Studio.
- Build & run on emulator or device.

#### MongoDB

- Hosted on MongoDB Atlas
- Pre-filled with users, movies, categories
- Admin user: `admin:123123`

---

## API Documentation

**Base URL:** `http://foo.com/api/`

### Categories

| Endpoint | Method | Description |
|---|---|---|
| `/categories` | GET | List all categories |
| `/categories` | POST | Create a new category |
| `/categories/:id` | GET | Get category by ID |
| `/categories/:id` | PATCH | Update category |
| `/categories/:id` | DELETE | Delete category |

### Movies

| Endpoint | Method | Description |
|---|---|---|
| `/movies` | GET | Get recommended movies by category |
| `/movies` | POST | Create a new movie |
| `/movies/:id` | GET | Get movie by ID |
| `/movies/:id` | PUT | Update movie |
| `/movies/:id` | DELETE | Delete movie |
| `/movies/:id/recommend` | GET | Get recommendations |
| `/movies/:id/recommend` | POST | Add movie to user |
| `/movies/search/:query` | GET | Search movies |

---



## Authors

- [Ido Haver](https://github.com/idohaver7)
- [Nir Perets](https://github.com/NirPerets)
- [Daniel Gusakov](https://github.com/DanielGusakov)

---

## Support

For questions, open an [issue](https://github.com/idohaver7/NETFLIX-Clone/issues) or contact us at <idohaver7@gmail.com>.
