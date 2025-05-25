-- create_tables.sql

CREATE DATABASE IF NOT EXISTS movie_db;
USE movie_db;

-- 영화 정보 테이블
CREATE TABLE IF NOT EXISTS movie (
  movie_id INT NOT NULL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  release_date DATE,
  overview TEXT,
  poster_path VARCHAR(255),
  rating FLOAT
);

-- 리뷰 테이블
CREATE TABLE IF NOT EXISTS review (
  review_id INT AUTO_INCREMENT PRIMARY KEY,
  movie_id INT,
  reviewer VARCHAR(100),
  content TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE,
  UNIQUE (movie_id, reviewer)
);

-- 즐겨찾기 테이블
CREATE TABLE IF NOT EXISTS favorite (
  favorite_id INT AUTO_INCREMENT PRIMARY KEY,
  movie_id INT,
  user_id VARCHAR(100),
  added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE,
  UNIQUE (movie_id, user_id)
);
