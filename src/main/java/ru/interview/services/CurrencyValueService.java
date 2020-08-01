package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.model.Currency;
import ru.interview.model.CurrencyValue;
import ru.interview.repositories.CurrencyValueRepository;

import java.util.List;

@Service
public class CurrencyValueService {
    CurrencyValueRepository currencyValueRepository;

    @Autowired
    public void setCurrencyValueRepository(CurrencyValueRepository currencyValueRepository) {
        this.currencyValueRepository = currencyValueRepository;
    }

    public List<CurrencyValue> getAllByCurrency(Currency currency) {
        return currencyValueRepository.findAllByCurrency(currency);
    }

}
