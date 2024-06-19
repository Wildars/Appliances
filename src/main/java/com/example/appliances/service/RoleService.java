package com.example.appliances.service;


import com.example.appliances.model.request.RoleRequest;
import com.example.appliances.model.response.RoleResponse;

import java.util.List;


public interface RoleService {
    public RoleResponse create(RoleRequest roleRequest);
    public RoleResponse findById(Long id);

    public RoleResponse update(RoleRequest roleRequest, Long roleId);
    public List<RoleResponse> findAll() ;
    void deleteById(Long id);

}
