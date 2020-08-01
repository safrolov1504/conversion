package ru.interview.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.interview.model.Currency;
import ru.interview.model.History;
import ru.interview.model.User;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long>, JpaSpecificationExecutor<History> {

    void deleteAllByUser(User userByName);
}
