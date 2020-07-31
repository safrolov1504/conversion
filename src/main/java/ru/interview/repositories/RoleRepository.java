package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.interview.model.Currency;
import ru.interview.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
