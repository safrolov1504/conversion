package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.model.Currency;
import ru.interview.model.CurrencyValue;
import ru.interview.repositories.HistorySpecifications;
import ru.interview.services.CurrencyService;
import ru.interview.services.CurrencyValueService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {
    CurrencyService currencyService;
    CurrencyValueService currencyValueService;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setCurrencyValueService(CurrencyValueService currencyValueService) {
        this.currencyValueService = currencyValueService;
    }

    @GetMapping
    public String statisticPage(Model model, @RequestParam (required = false) String currency_statistic){
        List<Currency> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies",currencies);

        Currency currency = null;
        if(currency_statistic != null){
            //достаем валюты из которой переводят
            String[] s = currency_statistic.split(" ");
            currency_statistic = s[0];
            currency = currencyService.findOneByCharCode(currency_statistic);
            model.addAttribute("currency",currency.getCharCode());
        }
        if(currency != null){
            List<CurrencyValue> currencyValues = currencyValueService.getAllByCurrency(currency);

            Collections.sort(currencyValues);

            model.addAttribute("currencyValues",currencyValues);
        }

        return "statistic";
    }
}
