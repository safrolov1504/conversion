package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.interview.utils.StaticFaction;
import ru.interview.model.Currency;
import ru.interview.model.History;
import ru.interview.model.User;
import ru.interview.repositories.HistoryRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class HistoryService {
    private CurrencyService currencyService;
    private UserService userService;
    private HistoryRepository historyRepository;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void addToHistory(String name, String currencyFrom,
                             String currencyTo, Double countFrom, Double result) {
        History history = new History();
        User user = userService.findUserByName(name);
        history.setUser(user);

        Currency currency = currencyService.findOneByCharCode(currencyFrom);
        history.setCurrency1(currency);
        history.setCountCur1(StaticFaction.round(countFrom,3));

        currency = currencyService.findOneByCharCode(currencyTo);
        history.setCurrency2(currency);
        history.setCountCur2(StaticFaction.round(result,3));

        history.setDate(Calendar.getInstance().getTime());
        historyRepository.save(history);
    }

    public List<History> findAllByUserName(User userByName) {
        return historyRepository.findAllByUser(userByName);
    }

    public List<History> findAllByUserName(Specification<History> spec, User userByName) {
        historyRepository.findAll(spec);
//        historyRepository.findAll(spec,userByName);
        return historyRepository.findAll(spec);
    }


    public List<History> findAllByUserNameAndCurrency1(User userByName, Currency currencyFrom) {
        return historyRepository.findAllByUserAndCurrency1(userByName,currencyFrom);
    }

    public List<History> findAllByUserNameAndCurrency1AndCurrency1(User userByName, Currency currencyFrom, Currency currencyTo) {
        return historyRepository.findAllByUserAndCurrency1AndCurrency2(userByName,currencyFrom,currencyTo);
    }

    public List<History> findAllByUserNameAndCurrency2(User userByName, Currency currencyTo) {
        return historyRepository.findAllByUserAndCurrency2(userByName,currencyTo);
    }
}
