drop table if exists books;
drop table if exists authors;
drop table if exists genres;
create table authors(id bigint auto_increment primary key,
                     first_name varchar(255),
                     last_name varchar(255));
create table genres(id bigint auto_increment primary key,
                    name varchar(255));
create table books(id bigint auto_increment primary key,
                   name varchar(255),
                   author_id bigint,
                   genre_id bigint,
                   foreign key (author_id) references authors(id),
                   foreign key (genre_id) references genres(id));