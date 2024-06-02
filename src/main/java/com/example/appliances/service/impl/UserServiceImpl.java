package com.example.appliances.service.impl;

import com.example.appliances.components.CustomUserDetails;
import com.example.appliances.entity.Filial;
import com.example.appliances.entity.Role;
import com.example.appliances.entity.Supplier;
import com.example.appliances.entity.User;
import com.example.appliances.exception.CustomError;
import com.example.appliances.exception.CustomException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.exception.UnauthorizedException;
import com.example.appliances.mapper.DefaultMapper;
import com.example.appliances.mapper.SupplierMapper;
import com.example.appliances.mapper.UserMapper;
import com.example.appliances.model.request.SupplierRequest;
import com.example.appliances.model.request.UserRequest;
import com.example.appliances.model.response.SupplierResponse;
import com.example.appliances.model.response.UserResponse;
import com.example.appliances.repository.FilialRepository;
import com.example.appliances.repository.RoleRepository;
import com.example.appliances.repository.SupplierRepository;
import com.example.appliances.repository.UserRepository;
import com.example.appliances.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    DefaultMapper defaultMapper;

    SupplierRepository supplierRepository;

    SupplierMapper supplierMapper;
    FilialRepository filialRepository;

    UserMapper userMapper;
    RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, DefaultMapper defaultMapper, SupplierRepository supplierRepository, SupplierMapper supplierMapper, FilialRepository filialRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.defaultMapper = defaultMapper;
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.filialRepository = filialRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    // Сохранение менеджеров
    @Override
    @Transactional
    public UserResponse saveUser(UserRequest userRequest) {
        User user = userMapper.requestToEntity(userRequest);
        user.setIsActive(true);

        List<Role> roles = defaultMapper.setRoles(userRequest.getRoleIds());
        user.setRoles(roles);

        List<Filial> organizations = defaultMapper.setOrganizations(userRequest.getFilialIds());
        user.setFilials(organizations);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            User savedUser = userRepository.save(user);
            return userMapper.entityToResponse(savedUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("Не удалось сохранить пользователя в базе данных", e);
        }
    }

    @Override
    @Transactional
    public SupplierResponse saveProvider(SupplierRequest request) {
        // Преобразуем запрос в сущность Supplier
        Supplier user = supplierMapper.requestToEntity(request);

        // Активируем пользователя
        user.setIsActive(true);

        // Присваиваем роль с ID 3
        Role role = defaultMapper.setRole(3L);  // предполагается, что у вас есть метод setRole, который принимает ID роли
        user.setRole(role);

        // Проверяем, что пароль не равен null
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Пароль не может быть null");
        }

        // Кодируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            // Сохраняем пользователя в базу данных
            Supplier savedUser = supplierRepository.save(user);

            // Преобразуем сохраненного пользователя в ответ и возвращаем его
            return supplierMapper.entityToResponse(savedUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("Не удалось сохранить пользователя в базе данных", e);
        }
    }

    private List<Role> fetchRoles(List<Role> roleModels) {
        if (roleModels == null || roleModels.isEmpty()) {
            return Collections.emptyList();
        }

        return roleModels.stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + role.getId())))
                .collect(Collectors.toList());
    }

    private List<Filial> fetchOrganizations(List<Filial> organizationModels) {
        if (organizationModels == null || organizationModels.isEmpty()) {
            return Collections.emptyList();
        }

        return organizationModels.stream()
                .map(organization -> filialRepository.findById(organization.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid organization ID: " + organization.getId())))
                .collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public UserResponse getUseByUserPin(String pin) {
        if (this.userRepository.getByPin(pin) == null) {
            throw new RecordNotFoundException("Пользователь не существует!");
        }
        User user = this.userRepository.getByPin(pin);
        return userMapper.entityToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(int page,
                                          int size,
                                          Optional<Boolean> sortOrder,
                                          String sortBy,
                                          Boolean isActive) {
        Pageable paging = null;
        if (sortOrder.isPresent()) {
            Sort.Direction direction = sortOrder.get() ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }

        Page<User> usersPage = null;
        if (isActive != null) {
            if (isActive) {
                usersPage = userRepository.findByIsActiveTrue(paging);
            } else {
                usersPage = userRepository.findByIsActiveFalse(paging);
            }
        } else {
            usersPage = userRepository.findAll(paging);
        }

        return usersPage.map(userMapper::entityToResponse);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));
        return userMapper.entityToResponse(user);
    }

    @Override
    public void deactivate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public void activate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));
        user.setIsActive(true);
        userRepository.save(user);
    }

    public User getCurrentUser() {
        // Получаем текущий контекст аутентификации из SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверяем, аутентифицирован ли пользователь
        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем имя пользователя из контекста аутентификации
            String login = authentication.getName();

            // Возвращаем заглушку
            return userRepository.findByPin(login);
        } else {
            // Если пользователь не аутентифицирован, можно выбрасывать исключение или возвращать null, в зависимости от вашего случая
            throw new UnauthorizedException("User is not authenticated");
        }
    }
    @Override
    @Transactional
    public UserResponse updateUser(UserRequest request, Long id) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        userMapper.update(updatedUser, request);

        User savedUser = userRepository.save(updatedUser);

        return userMapper.entityToResponse(savedUser);
    }

    public Boolean check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findById(customUserDetails.getUser().getId())
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        return user.getIsActive();
    }
}
