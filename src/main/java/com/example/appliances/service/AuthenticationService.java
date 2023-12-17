package com.example.appliances.service;



import com.example.appliances.model.request.AuthenticationModel;
import com.example.appliances.model.request.AuthenticationRequestModel;

import javax.servlet.http.HttpServletResponse;


public interface AuthenticationService {
    AuthenticationModel generateToken(AuthenticationRequestModel authRequest, HttpServletResponse response);
}
