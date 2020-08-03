package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.model.User;
import ru.interview.services.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private CurrencyService currencyService;
    private CurrencyValueService currencyValueService;
    private UrlService urlService;
    private UserService userService;
    private HistoryService historyService;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @Autowired
    public void setCurrencyValueService(CurrencyValueService currencyValueService) {
        this.currencyValueService = currencyValueService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }
    @Autowired
    public void setUrlService(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping
    public String profilePage(Model model,
                              @RequestParam(required = false) String dateUpdate,
                              @RequestParam (required = false) String userName){
        List<User> users = userService.findAll();
        model.addAttribute("users",users);

        //обработка запроса на добавление курса валют на определнный день
        if(dateUpdate!=null){
            try {
                Date date = new SimpleDateFormat("YYYY-MM-DD").parse(dateUpdate);
                currencyService.initiation(date);
            } catch (ParseException e) {
                model.addAttribute("error2", "Введите данные корректно. Неправильный формат даты. Требуемый формат YYYY-MM-DD");
            }
        }

        //Обработка функции очистки истории одного пользователя из админа
        if(userName != null){
            System.out.println(userName);
            User user = userService.findUserByName(userName);
            if(user != null){
                historyService.delAllByUser(user);
            } else {
                model.addAttribute("error", "Выберите пользователя");
            }
        }

        model.addAttribute("urlNow", urlService.getOneByDate());
        model.addAttribute("errorUrl",currencyService.getErrorUrl());
        return "admin";
    }

    //блокировка пользователя
    @GetMapping("/block")
    public String blocUser(@RequestParam String id){
        System.out.println("block "+id);
        userService.changeStatus(id, "false");
        return "redirect:/admin";
    }

    //разблокировка пользователя
    @GetMapping("/unblock")
    public String unblockUser(@RequestParam String id){
        System.out.println("unblock "+id);
        userService.changeStatus(id, "true");
        return "redirect:/admin";
    }


    //запрос на обновление валют в ЦБ на сегоднешню дату
    @GetMapping("/updateCurrency")
    public String updateCurrency(){
        currencyService.initiation(Calendar.getInstance().getTime());
        return "redirect:/admin";
    }

    //очистка истории всех пользователей
    @GetMapping("/clearHistory")
    public String clearCurrency(){
        historyService.delAll();
        return "redirect:/admin";
    }

    //изменение ссылки на сайт ЦБ
    @GetMapping("/changeUrl")
    public String changeUrl(@RequestParam (required = false) String newUrl){
        urlService.setNewUrl(newUrl);
        return "redirect:/admin";
    }
}
