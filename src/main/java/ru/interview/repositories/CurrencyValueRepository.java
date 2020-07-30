package ru.interview.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.interview.model.Currency;
import ru.interview.model.CurrencyValue;

import java.util.Date;
import java.util.List;

@Repository
public interface CurrencyValueRepository extends JpaRepository<CurrencyValue,Long> {
    List<CurrencyValue> findAllByDate(Date date);
    CurrencyValue findOneByCurrencyAndDate(Currency currency, Date date);
}
