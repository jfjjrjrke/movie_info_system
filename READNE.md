# 🎬 Movie Info Management System

> **A Java Swing + MySQL desktop application for browsing, bookmarking, and managing movie information.**

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Tech Stack](#tech-stack)
4. [Project Structure](#project-structure)
5. [Setup & Installation](#setup--installation)
6. [Usage](#usage)
7. [Screenshots](#screenshots)
8. [Contributing](#contributing)
9. [License](#license)

---

## Overview

`Movie Info Management System` is a lightweight desktop GUI application that lets users:

* **Browse** a catalog of movies pulled from an external API
* **Bookmark** favorites for quick access
* **View** trailers, posters, and reviews
* **Store** all data locally in a MySQL database via JDBC

It is designed as a semester project demonstrating **full‑stack Java development** with clean architecture and modular design.

---

## Features

| Module          | Key Functions                                                 |
| --------------- | ------------------------------------------------------------- |
| 📚 **Library**  | Search & filter movies by title / genre / year                |
| ⭐ **Favorites** | One‑click bookmark, remove bookmark                           |
| 💬 **Reviews**  | Fetch & display reviews via API (e.g., TMDb)                  |
| 🗂 **Admin**    | CRUD operations for movie records through DAO layer           |
| 🔍 **Search**   | Real‑time search suggestions (RSyntaxTextArea auto‑highlight) |

---

## Tech Stack

| Layer           | Technology                          |
| --------------- | ----------------------------------- |
| **Language**    | Java 17 (OpenJDK)                   |
| **GUI**         | Swing + RSyntaxTextArea 3.6.0       |
| **ORM / DAO**   | Pure JDBC (MySQL Connector/J 8.4.0) |
| **Helper Libs** | Lombok 1.18.10 (code generation)    |
| **Build**       | Maven 3.9.x                         |
| **Database**    | MySQL 8.0                           |
| **IDE**         | Eclipse 2025‑06 (Milestone)         |

---

## Project Structure

```text
movie-info-system/
├── src/main/java/
│   ├── com.example.dao/        # DAO layer (CRUD)
│   ├── com.example.model/      # POJO entities
│   ├── com.example.ui/         # Swing GUI panels & frames
│   └── com.example.util/       # Helper classes (DB, API)
├── src/main/resources/
│   └── log4j2.xml
├── pom.xml                     # Maven dependencies
└── README.md                   # You are here
```

---

## Setup & Installation

1. **Clone repo** & navigate:

   ```bash
   git clone https://github.com/yourname/movie-info-system.git
   cd movie-info-system
   ```
2. **Import into Eclipse** (`File → Import → Existing Maven Projects`).
3. **Configure database**:

   ```sql
   CREATE DATABASE movies CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

   Update `DBConfig.java` with your credentials.
4. **Build & run**:

   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.example.Main"
   ```

---

## Usage

* **Run Main** class → opens the main dashboard.
* Use the **Search bar** to fetch movie data.
* Click ⭐ to add/remove favorites.
* Admin menu → add custom movies manually.

---

## Screenshots

| Home Screen   | Details Dialog |
| ------------- | -------------- |
| *(Add image)* | *(Add image)*  |

---

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

1. Fork the project
2. Create your feature branch (`git checkout -b feat/AmazingFeature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feat/AmazingFeature`)
5. Open a pull request

---

## License

Distributed under the **MIT License**. See `LICENSE` for more information.
