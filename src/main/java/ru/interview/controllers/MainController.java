package ru.interview.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.interview.services.CurrencyService;

@Controller
public class MainController {

    private CurrencyService currencyService;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String mainPage(){
        currencyService.initiation();
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "index";
    }
}
