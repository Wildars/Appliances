package com.example.appliances.service;

import com.example.appliances.entity.User;
import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.UserRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface UserService {
    public UserResponse saveUser(UserRequest userRequest);

    UserResponse getUseByUserPin(String username);

    public Page<UserResponse> getAllUsers(int page,
                                          int size,
                                          Optional<Boolean> sortOrder,
                                          String sortBy,
                                          Boolean isActive) ;


    UserResponse getUserById(Long id);

    UserResponse updateUser(UserRequest userModel, Long id);

    void deactivate(Long id);
    void activate(Long id);

    public User getCurrentUser();
    Boolean check();

    public SupplierResponse saveProvider(SupplierRequest request);
}
