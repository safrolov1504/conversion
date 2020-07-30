package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.model.Currency;
import ru.interview.model.History;
import ru.interview.services.CurrencyService;
import ru.interview.services.HistoryService;
import ru.interview.services.UserService;
import ru.interview.utils.HistoryFilter;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/history")
public class HistoryController {
    private HistoryService historyService;
    private UserService userService;
    private CurrencyService currencyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public String history(Model model, Principal principal,
                          @RequestParam Map<String,String> requestParam){
        //отправляем лист валют, для формирования выпадающего списка
        List<Currency> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies",currencies);

        //проверяем корректность введеных данных в поиске
        Currency currencyFrom = null;
        if(requestParam.containsKey("currency_from_search")){
            //достаем валюты из которой переводят
            String currencyFromS = requestParam.get("currency_from_search");
            String[] s = currencyFromS.split(" ");
            currencyFromS = s[0];
            currencyFrom = currencyService.findOneByCharCode(currencyFromS);
        }

        Currency currencyTo = null;
        if(requestParam.containsKey("currency_to_search")){
            //достаем валюты из которой переводят
            String currencyToS = requestParam.get("currency_to_search");
            String[] s = currencyToS.split(" ");
            currencyToS = s[0];
            currencyTo = currencyService.findOneByCharCode(currencyToS);
        }

        try {
            HistoryFilter historyFilter = new HistoryFilter(requestParam);


            List<History> histories;
            //случай когда и две валюты выбраны
            if(currencyFrom != null && currencyTo != null){
                histories = historyService
                        .findAllByUserNameAndCurrency1AndCurrency1(userService.findUserByName(principal.getName()),currencyFrom,currencyTo);
            } else if(currencyFrom != null){
                //случай когда один заполнен
                histories = historyService.findAllByUserNameAndCurrency1(userService.findUserByName(principal.getName()),currencyFrom);
            } else if(currencyTo != null){
                //случай когда один заполнен
                histories = historyService.findAllByUserNameAndCurrency2(userService.findUserByName(principal.getName()),currencyTo);
            } else {
                //загружаем историю когда нет фильтра
                histories = historyService.findAllByUserName(historyFilter.getSpec(), userService.findUserByName(principal.getName()));
            }
            model.addAttribute("histories",histories);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "history";
    }
}
