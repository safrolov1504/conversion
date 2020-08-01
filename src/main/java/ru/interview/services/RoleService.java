package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.model.Role;
import ru.interview.model.User;
import ru.interview.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private UserService userService;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }


    public List<Role> findOneByName(String roleName){
        return roleRepository.findOneByName(roleName);
    }

    //метод добавляет админа по умолчанию и роли
    public void addRoleAndAdmin(){
        //создаем роли для системы
        List<Role> roles = new ArrayList<>();
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleRepository.save(roleUser);
        roles.add(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
        roles.add(roleAdmin);

        //создаем админа по умолчанию
        userService.addMainAdmin(roles);
    }
}
