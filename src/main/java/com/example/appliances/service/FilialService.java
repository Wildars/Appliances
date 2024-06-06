package com.example.appliances.service;

import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.FilialResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface FilialService {
    public List<FilialResponse> getAllUserOrganizations(String pin, String password);

    FilialResponse saveOrganization(FilialRequest organizationModel);

    public Page<FilialResponse> getAllOrganizations(int page, int size, Optional<Boolean> sortOrder, String sortBy, Optional<Long> id, Optional<String> filCode);
    FilialResponse getOrganizationById(Long id);

    void deleteOrganizationById(Long id);

    FilialResponse updateOrganization(FilialRequest organizationModel, Long id);

//    public List<FilialResponse> getOrganizationsForExpert(FilialRequest expert) ;
//
//    public List<FilialResponse> getOrganizationsWithExpert(FilialRequest expertAll) ;

}
