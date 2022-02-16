package ru.otus.spring.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
}