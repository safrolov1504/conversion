package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.interview.model.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {
}
