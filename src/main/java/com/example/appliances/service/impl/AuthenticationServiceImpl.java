package com.example.appliances.service.impl;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.User;
import com.example.appliances.exception.CustomError;
import com.example.appliances.exception.CustomException;
import com.example.appliances.filter.JwtUtil;
import com.example.appliances.model.request.AuthenticationModel;
import com.example.appliances.model.request.AuthenticationRequestModel;
import com.example.appliances.repository.FilialRepository;
import com.example.appliances.repository.UserRepository;
import com.example.appliances.service.AuthenticationService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.stream.Collectors;


@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {
    JwtUtil jwtUtil;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    ModelMapper modelMapper;
    FilialRepository organizationsRepository;

    @Autowired
    public AuthenticationServiceImpl(JwtUtil jwtUtil,
                                     AuthenticationManager authenticationManager,
                                     UserRepository userRepository,
                                     ModelMapper modelMapper, FilialRepository organizationsRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.organizationsRepository = organizationsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationModel generateToken(AuthenticationRequestModel authRequest, HttpServletResponse response) {
        User user = userRepository.getUserByPinQuery(authRequest.getPin());

        if (user == null) {
            throw new CustomException(CustomError.USER_NOT_FOUND);
        }

        if (!user.getIsActive()) {
            throw new CustomException(CustomError.USER_NOT_ACTIVE);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getPin(), authRequest.getPassword()));
        } catch (Exception ex) {
            throw new CustomException(CustomError.USER_NOT_AUTHENTICATE);
        }

        Filial organization = null;
        boolean found = false;
        for (Filial org : user.getFilials()) {
            if (org.getFilCode().equals(authRequest.getFilCode())) {
                found = true;
                organization = org; // Присваиваем найденное значение переменной organization
                break;
            }
        }

        if (!found) {
            throw new CustomException(CustomError.USER_NOT_HAVE_THIS_ORGANISATION);
        }

        HashSet<String> authorities = new HashSet<>();
        for (var role : user.getRoles()) {
            for (var permission : role.getPermissions()) {
                authorities.add(permission.getName());
            }
        }

        // Используем переменную jwt один раз
        String jwt = jwtUtil.generateToken(authRequest.getPin(), authRequest.getFilCode());

        return AuthenticationModel.builder()
                .id(user.getId())
                .pin(user.getPin())
                .surname(user.getSurname())
                .name(user.getName())
                .patronymic(user.getPatronymic())
                .phone(user.getPhone())
                .email(user.getEmail())
                .jwtToken(jwt)
                .organizations(organization)
                .roles(user.getRoles())
                .permissions(authorities.stream().collect(Collectors.toList()))
                .build();
    }


}
