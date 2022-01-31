package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.Commentary;

public interface CommentaryRepository extends MongoRepository<Commentary, String>, CommentaryRepositoryCustom {

}
