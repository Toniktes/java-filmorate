# java-filmorate
Схема БД.

![Схема БД](https://user-images.githubusercontent.com/78084673/220184889-469159e9-f6ed-4a98-94bf-c37e04f21145.png)
```
Код для dbdiagram.io (https://dbdiagram.io)
Table Film {
filmId int [pk, increment]
name varchar [not null]
description varchar
releaseDate timestamp
duration int
rate int
RatingMpa varchar [ref: - RatingMpa.ratingMpaId]
}

Table RatingMpa {
  ratingMpaId int [pk, increment]
  name varchar [not null]
  description varchar
}


Table Users {
  userId int [pk, increment] 
  email varchar
  login varchar
  birthday date
}

Table Likes {
  likeId int [pk, increment] 
  userId int [ref: > Users.userId]
  filmId int [ref: > Film.filmId]
}

Table FriendList {
  friendListId int [pk, increment]
  userId int [ref: > Users.userId]
  friendID int
  status bool 
}

Table Genre {
  genreId int [pk, increment]
  name varchar
}

Table GenreLine {
  genreLineId int [pk, increment]
  filmId int [ref: > Film.filmId]
  genreId int [ref: > Genre.genreId]
}
```
