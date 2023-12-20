package com.example.appliances.service.impl;

import com.example.appliances.entity.Sale;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.SaleMapper;
import com.example.appliances.model.request.SaleRequest;
import com.example.appliances.model.response.SaleResponse;
import com.example.appliances.repository.SaleRepository;
import com.example.appliances.service.SaleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SaleServiceImpl implements SaleService {

     SaleRepository saleRepository;
     SaleMapper saleMapper;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
    }
    @Override
    @Transactional
    public SaleResponse create(SaleRequest saleRequest) {
        Sale sale = saleMapper.requestToEntity(saleRequest);
        Sale savedSale = saleRepository.save(sale);
        return saleMapper.entityToResponse(savedSale);
    }
    @Override
    @Transactional
    public SaleResponse findById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует!"));
        return saleMapper.entityToResponse(sale);
    }
    @Override
    @Transactional
    public SaleResponse update(SaleRequest saleRequest, Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RecordNotFoundException("Продажа с таким id не существует"));
        saleMapper.update(sale, saleRequest);
        Sale updatedSale = saleRepository.save(sale);
        return saleMapper.entityToResponse(updatedSale);
    }
    @Override
    @Transactional
    public List<SaleResponse> findAll() {
        List<Sale> sales = saleRepository.findAll();
        return sales.stream().map(saleMapper::entityToResponse).collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteById(Long id) {
        saleRepository.deleteById(id);
    }
}