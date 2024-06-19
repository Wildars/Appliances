package com.example.appliances.api;

import com.example.appliances.model.request.RoleRequest;
import com.example.appliances.model.response.RoleResponse;
import com.example.appliances.service.RoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/roles")
public class RoleApi {
    RoleService roleService;

    @Autowired
    public RoleApi(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleResponse> saveRole(@Valid @RequestBody RoleRequest roleResponse){
        RoleResponse saveRole = roleService.create(roleResponse);
        return new ResponseEntity<>(saveRole, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<RoleResponse> findAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        RoleResponse getRoleById = roleService.findById(id);
        return new ResponseEntity<>(getRoleById, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody RoleRequest roleResponse, @PathVariable Long id) {
        RoleResponse updated = roleService.update(roleResponse, id);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletedRole(@PathVariable Long id) {
        roleService.deleteById(id);
    }


}
