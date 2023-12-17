package com.example.appliances.api;

import com.example.appliances.model.request.UserRequest;
import com.example.appliances.model.response.UserResponse;
import com.example.appliances.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/users")
public class UserApi {
    UserService userService;

    @Autowired
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userModel) {
        UserResponse saveUser = userService.saveUser(userModel);
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public Page<UserResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "25") int size,
                                                     @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(required = false) Boolean isActive) {
        return userService.getAllUsers(page, size, sortOrder, sortBy, isActive);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse getUserById = userService.getUserById(id);
        return new ResponseEntity<>(getUserById, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userModel, @PathVariable Long id) {
        UserResponse updated = userService.updateUser(userModel, id);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @DeleteMapping("/deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        userService.deactivate(id);
    }

    @PutMapping("/activate/{id}")
    public void activate(@PathVariable Long id) {
        userService.activate(id);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check() {
        return new ResponseEntity<>(userService.check(), HttpStatus.OK);
    }
}