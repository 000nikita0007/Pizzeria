package com.example.pizza_world.service;

import com.example.pizza_world.bean.User;
import com.example.pizza_world.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
//Для спринг секьюрити , хуй знает зачем ))
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    protected UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDao.findByLogin(login);

        if (user != null) {
            Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
            grantedAuthoritySet.add(new SimpleGrantedAuthority(user.getRole().getName()));
            return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthoritySet);
        } else {
            throw new UsernameNotFoundException("Cannot find user with login=" + login);
        }
    }
}
