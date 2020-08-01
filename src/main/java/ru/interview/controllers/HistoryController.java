package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.Collection;
import java.util.Collections;
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
        //запрашиваем лист валют, для формирования выпадающего списка
        List<Currency> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies",currencies);

        //добавялем фильтр
        HistoryFilter historyFilter = new HistoryFilter(requestParam,currencyService,userService.findUserByName(principal.getName()));

        List<History> histories = historyService.findAllByUserName(historyFilter.getSpec());
        if(historyFilter.getError() != null){
            model.addAttribute("error",historyFilter.getError());
        }
        Collections.sort(histories);
        model.addAttribute("histories",histories);

        return "history";
    }

    //удаление всей истории пользователя
    @GetMapping("/clear")
    public String historyClean(Principal principal){
        historyService.delAllByUser(userService.findUserByName(principal.getName()));
        return "redirect:/history";
    }

    //удаление выбраной строчки
    @GetMapping("/del/{id}")
    public String historyDel(@PathVariable Long id, Model model){
        historyService.delById(id);
        return "redirect:/history";
    }

}
