package ru.otus.spring.testchangelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ru.otus.spring.models.*;
import ru.otus.spring.repositories.AuthorRepository;
import ru.otus.spring.repositories.BookRepository;
import ru.otus.spring.repositories.CommentaryRepository;
import ru.otus.spring.repositories.GenreRepository;
import ru.otus.spring.service.UserDetailsServiceCustom;

import java.util.List;

@Import({UserDetailsServiceCustom.class})
@ChangeLog(order = "001")
public class InitMongoDBDataTestChangeLog {

    private Author authorTolkien;
    private Author authorTolstoy;

    private Genre genreFantasy;
    private Genre genreNovel;

    private Book bookLOTR;
    private Book bookWAP;
    private Book bookAnna;

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
                new Book("The Lord of the Rings", List.of(authorTolkien), List.of(genreFantasy)));
        bookWAP = repository.save(
                new Book("War and Piece", List.of(authorTolstoy), List.of(genreNovel)));
        bookAnna = repository.save(
                new Book("Anna Karenina", List.of(authorTolstoy), List.of(genreNovel)));
    }

    @ChangeSet(order = "004", id = "initCommentaries", author = "kmrandy", runAlways = true)
    public void initCommentaries(CommentaryRepository repository){
        repository.save(new Commentary(bookLOTR, "Amazing book!"));
        repository.save(new Commentary(bookLOTR, "So cool!"));
        repository.save(new Commentary(bookWAP, "Boring..."));
        repository.save(new Commentary(bookAnna, "Interesting"));
    }
}
