insert into authors(id, first_name, last_name) values (1, 'John', 'Tolkien');
insert into authors(id, first_name, last_name) values (2, 'Leo', 'Tolstoy');
insert into genres(id, name) values (1, 'fantasy');
insert into genres(id, name) values (2, 'novel');
insert into books(id, name, author_id, genre_id) values (1, 'The Lord of the Rings', 1, 1);
insert into books(id, name, author_id, genre_id) values (2, 'War and Piece', 2, 2);