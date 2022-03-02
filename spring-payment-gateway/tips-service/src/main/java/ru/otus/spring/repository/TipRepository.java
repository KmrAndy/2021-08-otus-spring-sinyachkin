package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.model.Tip;

import java.util.List;

public interface TipRepository extends MongoRepository<Tip, String> {
    List<Tip> findTipsByFromUserId (String fromUserId);
}
