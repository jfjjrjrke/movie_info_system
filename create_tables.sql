-- create_tables.sql

CREATE DATABASE IF NOT EXISTS movie_db;
USE movie_db;

-- 영화 정보 테이블
CREATE TABLE movie (
  movie_id INT NOT NULL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  release_date DATE DEFAULT NULL,
  overview TEXT,
  poster_path VARCHAR(255) DEFAULT NULL,
  rating FLOAT DEFAULT NULL
);

-- 리뷰 테이블
CREATE TABLE review (
  review_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  movie_id INT DEFAULT NULL,
  reviewer VARCHAR(100) DEFAULT NULL,
  content TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE
);

-- 즐겨찾기 테이블
CREATE TABLE favorite (
  favorite_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  movie_id INT DEFAULT NULL,
  user_id VARCHAR(100) DEFAULT NULL,
  added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE CASCADE
);
