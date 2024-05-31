package com.example.appliances.service.impl;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.Manager;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ClientMapper;
import com.example.appliances.mapper.ManagerMapper;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.request.ManagerRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ManagerResponse;
import com.example.appliances.repository.ClientRepository;
import com.example.appliances.repository.ManagerRepository;
import com.example.appliances.service.ClientService;
import com.example.appliances.service.ManagerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ManagerServiceImpl implements ManagerService {

    ManagerMapper managerMapper;

    ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerMapper managerMapper, ManagerRepository managerRepository) {
        this.managerMapper = managerMapper;
        this.managerRepository = managerRepository;
    }


    @Override
    public ManagerResponse create(ManagerRequest request) {
        Manager client = managerMapper.requestToEntity(request);
        Manager save = managerRepository.save(client);
        return managerMapper.entityToResponse(save);
    }

    @Override
    public Manager findById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Manager с таким id не существует!"));
    }

    @Override
    public Page<ManagerResponse> getAllClient(int page,
                                                               int size,
                                                               Optional<Boolean> sortOrder,
                                                               String sortBy) {
        Pageable paging = null;

        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Manager> saleItemsPage = managerRepository.findAll(paging);

        return saleItemsPage.map(managerMapper::entityToResponse);
    }


    @Override
    public ManagerResponse update(ManagerRequest request, Long id) {
        Manager client = managerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует"));
        managerMapper.update(client, request);
        Manager update = managerRepository.save(client);
        return managerMapper.entityToResponse(update);
    }

    @Override
    public List<ManagerResponse> findAll() {
        List<Manager> products = managerRepository.findAll();
        return products.stream().map(managerMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        managerRepository.deleteById(id);
    }
}
