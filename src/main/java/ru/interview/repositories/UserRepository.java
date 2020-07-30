package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.interview.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User,Long> {
    Optional<User> findOneByName(String name);
}
