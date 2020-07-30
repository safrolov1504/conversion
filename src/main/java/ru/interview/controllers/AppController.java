package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.utils.StaticFaction;
import ru.interview.model.Currency;
import ru.interview.services.CurrencyService;
import ru.interview.services.HistoryService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
public class AppController {
    private CurrencyService currencyService;
    private HistoryService historyService;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public String profilePage(Model model, Principal principal,
                              @RequestParam Map<String,String> requestMap){

        //проверяем пришел ли запрос на перевод + не пустой ли он
        if(requestMap.containsKey("count_from")
                && requestMap.containsKey("currency_from")
                && requestMap.containsKey("currency_to")
                && !requestMap.get("count_from").equals("")){

                //достаем валюты из которой переводят
                String currencyFrom = requestMap.get("currency_from");
                String[] s = currencyFrom.split(" ");
                currencyFrom = s[0];

                //достаем валюту в которую переводим
                String currencyTo = requestMap.get("currency_to");
                s = currencyTo.split(" ");
                currencyTo = s[0];

                //достаем исходной значение + заменяем, если ввели запятую, а не точку
                String countFromS = requestMap.get("count_from").replace(',','.');

                //проверяем, что сюда пришли точно сокращения от валют, а не другой текст
                if((currencyService.findOneByCharCode(currencyFrom) != null)
                        && (currencyService.findOneByCharCode(currencyTo) != null)
                        && StaticFaction.isDigit(countFromS)){

                    Double countFrom = Double.parseDouble(countFromS);

                    //проверяем есть ли актуальная информация о валютах в БД
                    currencyService.initiation();

                    Double result = StaticFaction.round(currencyService.result(currencyFrom,currencyTo,countFrom),3);

                    //добавялем в историю
                    historyService.addToHistory(principal.getName(), currencyFrom, currencyTo, countFrom,result);

                    model.addAttribute("result", result);
                }
        }

        //отправляем лист валют, для формирования выпадающего списка
        List<Currency> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies",currencies);
        return "app";
    }


}
