package com.example.appliances.service;


import com.example.appliances.model.request.StatusDto;

import java.util.List;

public interface StatusService {
    StatusDto saveStatus(StatusDto statusDto);
    StatusDto updateStatus(StatusDto statusDto, long id);
    List<StatusDto> findAllStatus();
    StatusDto getStatusById(Long id);
    void deleteStatus(Long id);
}
