package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.interview.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

    Currency findOneByNumCode(Integer numCode);

    Currency findOneByCharCode(String currencyFrom);
}
