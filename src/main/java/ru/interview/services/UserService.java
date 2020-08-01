package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.interview.model.Role;
import ru.interview.model.User;
import ru.interview.repositories.RoleRepository;
import ru.interview.repositories.UserRepository;
import ru.interview.utils.StaticFaction;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByName(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        if (user.getStatus().equals("false")){
            throw new UsernameNotFoundException("User is blocked");
        }

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public User findUserByName(String user){
        Optional<User> oneByName = userRepository.findOneByName(user);
        if(oneByName.isEmpty()){
            return null;
        } else {
            return oneByName.get();
        }
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void changeStatus(String id, String status) {
        Long idLong = Long.parseLong(id);
        User newUser = userRepository.findById(idLong).get();
        newUser.setStatus(status);
        userRepository.save(newUser);
    }

    public void addUser(Map<String,String> mapUser, String roleString){
        User user = new User();
        user.setName(mapUser.get("name"));

        user.setPassword(mapUser.get("password"));
        String password = StaticFaction.encodePassword(mapUser.get("password"));
        user.setPassword(password);

        user.setEmail(mapUser.get("email"));
        user.setFirstName(mapUser.get("first_name"));
        user.setSecondName(mapUser.get("second_name"));
        user.setStatus("true");

        List<Role> roles = roleRepository.findOneByName(roleString);

        user.setRoles(roles);
        userRepository.save(user);
    }

    //метод добавляющий админа по умолчанию
    public void addMainAdmin(List<Role> roles) {
        User user = new User();
        user.setName("admin");
        user.setPassword(StaticFaction.encodePassword("100"));
        user.setStatus("true");
        user.setFirstName("Main admin");
        user.setSecondName("Main admin");
        user.setEmail("admin@admin.ru");
        user.setRoles(roles);
        userRepository.save(user);
    }
}
