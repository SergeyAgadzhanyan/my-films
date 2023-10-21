drop table if exists FILM_GENRE;

drop table if exists FILM_LIKES;

drop table if exists FILM_RATING;

drop table if exists FILMS;

drop table if exists GENRE;

drop table if exists RATING;

drop table if exists USER_FRIENDS;

drop table if exists FRIEND_STATUS;

drop table if exists USERS;


create table IF NOT EXISTS RATING
(
    RATING_ID INTEGER auto_increment,
    TITLE     CHARACTER VARYING not null,
    constraint RATING_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    ID           INTEGER auto_increment,
    NAME         CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING     not null,
    RELEASE_DATE DATE                  not null,
    DURATION     INTEGER               not null,
    RATING_ID    INTEGER               not null,
    constraint FILMS_PK
        primary key (ID),
    constraint FILMS_RATING_RATING_ID_FK
        foreign key (RATING_ID) references RATING

);

create table IF NOT EXISTS FRIEND_STATUS
(
    STATUS_ID    INTEGER auto_increment,
    STATUS_TITLE CHARACTER VARYING not null,
    constraint STATUS_PK
        primary key (STATUS_ID)
);



create table IF NOT EXISTS GENRE
(
    GENRE_ID INTEGER auto_increment not null,
    TITLE    CHARACTER VARYING(50)  not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRE_FILMS_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);




create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment,
    NAME     CHARACTER VARYING(50) not null,
    LOGIN    CHARACTER VARYING(50) not null,
    EMAIL    CHARACTER VARYING(50) not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKES_PK
        primary key (FILM_ID, USER_ID),
    constraint FILM_LIKES_FILMS_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_LIKES_USERS_ID_FK
        foreign key (USER_ID) references USERS
);



create table IF NOT EXISTS USER_FRIENDS
(
    USER_ID   INTEGER           not null,
    FRIEND_ID INTEGER           not null,
    STATUS_ID INTEGER default 1 not null,
    constraint USER_FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint USER_FRIENDS_FRIEND_STATUS_STATUS_ID_FK
        foreign key (STATUS_ID) references FRIEND_STATUS,
    constraint USER_FRIENDS_USERS_ID_FK
        foreign key (FRIEND_ID) references USERS,
    constraint USER_FRIENDS_USERS_ID_FK_2
        foreign key (USER_ID) references USERS
);
create unique index if not exists USER_EMAIL_UINDEX on USERS (EMAIL);
create unique index if not exists USER_LOGIN_UINDEX on USERS (LOGIN);

