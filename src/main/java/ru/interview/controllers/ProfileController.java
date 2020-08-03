package ru.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.interview.model.User;
import ru.interview.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profilePage(Principal principal, Model model){
        String name = principal.getName();
        System.out.println(name);
        User user = userService.findUserByName(name);
        model.addAttribute("user",user);

        return "profile";
    }
}
