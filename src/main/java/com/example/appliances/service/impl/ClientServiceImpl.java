package com.example.appliances.service.impl;

import com.example.appliances.entity.Client;
import com.example.appliances.entity.ProductCategory;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.ClientMapper;
import com.example.appliances.model.request.ClientRequest;
import com.example.appliances.model.response.ClientResponse;
import com.example.appliances.model.response.ProductCategoryResponse;
import com.example.appliances.repository.ClientRepository;
import com.example.appliances.service.ClientService;
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
public class ClientServiceImpl implements ClientService {

    ClientMapper clientMapper;

    ClientRepository clientRepository;

    public ClientServiceImpl(ClientMapper clientMapper, ClientRepository clientRepository) {
        this.clientMapper = clientMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientResponse create(ClientRequest clientRequest) {
        Client client = clientMapper.requestToEntity(clientRequest);
        Client save = clientRepository.save(client);
        return clientMapper.entityToResponse(save);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Клиент с таким id не существует!"));
    }

    @Override
    public Page<ClientResponse> getAllClient(int page,
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
        Page<Client> saleItemsPage = clientRepository.findAll(paging);

        return saleItemsPage.map(clientMapper::entityToResponse);
    }


    @Override
    public ClientResponse update(ClientRequest clientRequest, Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Товар с таким id не существует"));
        clientMapper.update(client, clientRequest);
        Client update = clientRepository.save(client);
        return clientMapper.entityToResponse(update);
    }

    @Override
    public List<ClientResponse> findAll() {
        List<Client> products = clientRepository.findAll();
        return products.stream().map(clientMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
            clientRepository.deleteById(id);
    }
}
