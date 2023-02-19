DELETE FROM Likes;
DELETE FROM GenreLine;
DELETE FROM FriendList;
DELETE FROM Users;
DELETE FROM Film;

ALTER TABLE Users ALTER COLUMN UserID RESTART WITH 1;
ALTER TABLE Film ALTER COLUMN FilmID RESTART WITH 1;
ALTER TABLE FriendList ALTER COLUMN FriendListId RESTART WITH 1;
ALTER TABLE GenreLine ALTER COLUMN GenreLineID RESTART WITH 1;
ALTER TABLE Likes ALTER COLUMN LikeID RESTART WITH 1;

MERGE INTO RatingMPA KEY(RatingID)
    VALUES (1, 'G', 'У фильма нет возрастных ограничений'),
           (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
           (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
           (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'),
           (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');

MERGE INTO Genre KEY(GenreID)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');