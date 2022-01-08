insert into authors(first_name, last_name) values ('John', 'Tolkien');
insert into authors(first_name, last_name) values ('Leo', 'Tolstoy');
insert into genres(name) values ('fantasy');
insert into genres(name) values ('novel');
insert into books(name, author_id, genre_id) values ('The Lord of the Rings', 1, 1);
insert into books(name, author_id, genre_id) values ('War and Piece', 2, 2);
insert into commentaries(text, book_id) values ('Amazing book!', 1);
insert into commentaries(text, book_id) values ('So cool!', 1);
insert into commentaries(text, book_id) values ('Boring...', 2);