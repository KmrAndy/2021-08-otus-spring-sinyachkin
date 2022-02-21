package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.spring.models.Author;
import ru.otus.spring.models.Book;
import ru.otus.spring.models.Commentary;
import ru.otus.spring.models.Genre;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentaryRepository;
import ru.otus.spring.repositories.GenreRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private Author authorTolkien;
    private Author authorTolstoy;

    private Genre genreFantasy;
    private Genre genreNovel;

    private Book bookLOTR;
    private Book bookWAP;

    @ChangeSet(order = "000", id = "dropDB", author = "kmrandy", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "kmrandy", runAlways = true)
    public void initAuthors(AuthorRepository repository){
        authorTolkien = repository.save(new Author("John", "Tolkien"));
        authorTolstoy = repository.save(new Author("Leo", "Tolstoy"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "kmrandy", runAlways = true)
    public void initGenres(GenreRepository repository){
        genreFantasy = repository.save(new Genre("fantasy"));
        genreNovel = repository.save(new Genre("novel"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "kmrandy", runAlways = true)
    public void initBooks(BookRepository repository){
        bookLOTR = repository.save(
                new Book("The Lord of the Rings", List.of(authorTolkien, authorTolstoy), List.of(genreFantasy)));
        bookWAP = repository.save(
                new Book("War and Piece", List.of(authorTolstoy), List.of(genreNovel)));
    }

    @ChangeSet(order = "004", id = "initCommentaries", author = "kmrandy", runAlways = true)
    public void initCommentaries(CommentaryRepository repository){
        repository.save(new Commentary(bookLOTR, "Amazing book!"));
        repository.save(new Commentary(bookLOTR, "So cool!"));
        repository.save(new Commentary(bookWAP, "Boring..."));
    }
}
