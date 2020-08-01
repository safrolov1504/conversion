package ru.interview.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.interview.model.Role;
import ru.interview.model.User;
import ru.interview.services.CurrencyService;
import ru.interview.services.RoleService;
import ru.interview.services.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private UserService userService;
    private CurrencyService currencyService;
    private RoleService roleService;

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String mainPage(){
        currencyService.initiation(Calendar.getInstance().getTime());
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "index";
    }

    @GetMapping("/addUser")
    public String addUserPage(Model model,
                              @RequestParam Map<String,String> request){

        if(request.containsKey("name") && !request.get("name").isEmpty()
            && request.containsKey("password") && !request.get("password").isEmpty()
            && request.containsKey("email") && !request.get("email").isEmpty()
            && request.containsKey("first_name")&& !request.get("first_name").isEmpty()
            && request.containsKey("second_name")&& !request.get("second_name").isEmpty()){

            //проверка, что такой пользователь с таким именем уже есть
            if(userService.findUserByName(request.get("name")) == null){
                if(request.containsKey("choose_role")){
                    //проверка что выбрана роль
                    if(!roleService.findOneByName(request.get("choose_role")).isEmpty()){
                        userService.addUser(request,request.get("choose_role"));
                        model.addAttribute("error", "Пользователь добавлен");
                        return "redirect:/addUser/";

                    } else {
                        model.addAttribute("error", "Выберите роль нового пользователя");
                    }
                } else {
                    userService.addUser(request,"ROLE_USER");
                    return "redirect:/";
                }
            } else {
                model.addAttribute("error", "Пользователь с таким логино уже существует");
            }


//            User user = new User();
//            user.setName(request.get("name"));
//
//            user.setPassword(request.get("password"));
//            BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
//            String password = bcryptEncoder.encode(request.get("password"));
//            user.setPassword(password);
//
//            user.setEmail(request.get("email"));
//            user.setFirstName(request.get("first_name"));
//            user.setSecondName(request.get("second_name"));
//            user.setStatus("true");
//
//            List<Role> roles = new ArrayList<>();
//            Role role = new Role();
//            role.setId(1l);
//            role.setName("USER");
//            roles.add(role);
//
//            user.setRoles(roles);
//            userService.save(user);

        } else if(!request.isEmpty()){
            model.addAttribute("error", "Проверьте корректность введенных данных");
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles",roles);

        return "addUser";
    }
}
