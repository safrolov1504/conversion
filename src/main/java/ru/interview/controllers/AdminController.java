package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.model.CurrencyValue;
import ru.interview.model.User;
import ru.interview.repositories.HistorySpecifications;
import ru.interview.services.CurrencyService;
import ru.interview.services.CurrencyValueService;
import ru.interview.services.HistoryService;
import ru.interview.services.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private CurrencyService currencyService;
    private CurrencyValueService currencyValueService;
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

    @GetMapping
    public String profilePage(Model model,
                              @RequestParam(required = false) String dateUpdate,
                              @RequestParam (required = false) String userName){
        List<User> users = userService.findAll();
        model.addAttribute("users",users);

        //обработка запроса на добавление курса валют на определнный день на определнный день
        if(dateUpdate!=null){
            try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateUpdate);
                    currencyService.initiation(date);
            } catch (ParseException e) {
                model.addAttribute("error2", "Введите данные корректно. Неправильный формат даты. Требуемый формат yyyy-MM-dd");
            }
        }

        //Обработка функции очистки истории одного пользователя
        if(userName != null){
            System.out.println(userName);
            User user = userService.findUserByName(userName);
            if(user != null){
                historyService.delAllByUser(user);
            } else {
                model.addAttribute("error", "Выберите пользователя");
            }
        }
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


    //запрос на обновление валют в ЦБ
    @GetMapping("/updateCurrency")
    public String updateCurrency(){

        return "redirect:/admin";
    }

    //очистка истории всех пользователей
    @GetMapping("/clearHistory")
    public String clearCurrency(){
        historyService.delAll();
        return "redirect:/admin";
    }
}
