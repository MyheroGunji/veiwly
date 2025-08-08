# 🎬 Viewly - Movie & TV Watchlist App

A personal movie and TV tracking app where users can search titles via TMDB API, save them to their Watchlist, write private reviews, and visualize their viewing stats with charts and badges.

---

##  Features

-  **Search** movies and TV shows via TMDB API
-  **Add to Watchlist** (Want to Watch / Watched)
-  **Private Reviews** for each movie/show
-  **Statistics Dashboard** (watch time, genre distribution, ratings)
-  **Badge System** (Earn badges based on activities)
-  **User login** (Spring Security InMemoryUser or optional JDBC)

---

##  Screenshots

> (*Insert images here from your `/static/img` folder or screenshots directory.*)

---

##  Demo Video (Optional but highly recommended)
➡️ Watch the demo video here

---
##  Technologies Used

| Layer        | Stack                           |
|--------------|----------------------------------|
| Frontend     | Thymeleaf + Bootstrap            |
| Backend      | Spring Boot (Java 17)            |
| Database     | MySQL (or fallback: H2 optional) |
| API          | TMDB REST API                    |
| Visualization| Chart.js                         |
| Auth         | Spring Security (InMemoryUser)   |

---

##  Project Structure (主要ファイル構成)
<img width="776" height="460" alt="image" src="https://github.com/user-attachments/assets/f3de5302-c4a2-4edc-b759-dac5eab14008" />


---

##  How to Run Locally

### Requirements
- Java 17
- Maven
- MySQL running (`movieapp` DB)
- TMDB API Key

---

### Step-by-step Setup

1. **Clone this repo:**
```bash
git clone https://github.com/MyheroGunji/veiwly.git
cd veiwly

```

2. **Set up MySQL database:**

```sql
CREATE DATABASE movieapp_db;
```

3. **Configure your database in `application.properties`:**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/movieapp_db
spring.datasource.username=root
spring.datasource.password=Mhrcfnc5@Family

# TMDB API Key
tmdb.api.key=6630d7c54c802d0a954063c6b4c0e163
```

4. **Run the app:**

```bash
./mvnw spring-boot:run
```

5. **Login credentials (InMemoryUser):**

```
Username: user
Password: pass
```

```markdown
Note:
This application requires a MySQL database named `movieapp` to be running locally.
If a MySQL environment is not available, you may switch to an H2 in-memory database using the optional configuration provided above.

TMDB API key must be obtained from https://www.themoviedb.org/ and inserted into `application.properties`.

Login is enabled via Spring Security using a pre-configured in-memory user (see credentials above).
```



