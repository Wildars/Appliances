package com.example.appliances.api;


import com.example.appliances.model.request.AuthenticationModel;
import com.example.appliances.model.request.AuthenticationRequestModel;
import com.example.appliances.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
public class AuthApi {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AuthenticationModel generateToken(@Valid @RequestBody AuthenticationRequestModel authRequest, HttpServletResponse response) {
        return authenticationService.generateToken(authRequest, response);
    }
}
