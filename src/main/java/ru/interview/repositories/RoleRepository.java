package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.interview.model.Currency;
import ru.interview.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    List<Role> findOneByName(String roleString);
}
