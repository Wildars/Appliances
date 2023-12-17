package com.example.appliances.service.impl;

import com.example.appliances.components.CustomUserDetails;
import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Role;
import com.example.appliances.entity.User;
import com.example.appliances.repository.FilialRepository;
import com.example.appliances.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;
    FilialRepository organizationsRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, FilialRepository organizationsRepository) {
        this.userRepository = userRepository;
        this.organizationsRepository = organizationsRepository;
    }

    public CustomUserDetails loadUser(String pin, String filCode) throws UsernameNotFoundException {
        User user = userRepository.getByPin(pin);
        if (user == null)
            throw new UsernameNotFoundException(pin);

        List<Role> roles = user.getRoles();
        List<GrantedAuthority> grantedAuthorities = roles.stream().map(r -> {
            return new SimpleGrantedAuthority(r.getName());
        }).collect(Collectors.toList());

        Filial organization = organizationsRepository.findByFilCode(filCode);

        return new CustomUserDetails(user, organization, grantedAuthorities);
    }

    @Override
    public UserDetails loadUserByUsername(String pin) throws UsernameNotFoundException {
        User user = userRepository.getByPin(pin);
        if (user == null) {
            throw new UsernameNotFoundException(pin);
        }
        return new CustomUserDetails(user, new Filial(), new ArrayList<GrantedAuthority>());
    }
}
