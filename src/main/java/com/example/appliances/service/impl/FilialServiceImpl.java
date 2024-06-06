package com.example.appliances.service.impl;

import com.example.appliances.entity.Filial;
import com.example.appliances.entity.User;
import com.example.appliances.exception.CustomError;
import com.example.appliances.exception.CustomException;
import com.example.appliances.exception.RecordNotFoundException;
import com.example.appliances.mapper.FilialMapper;
import com.example.appliances.model.request.FilialRequest;
import com.example.appliances.model.response.FilialResponse;
import com.example.appliances.repository.FilialRepository;
import com.example.appliances.repository.UserRepository;
import com.example.appliances.service.FilialService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilialServiceImpl implements FilialService {
    UserRepository userRepository;
    FilialRepository organizationsRepository;
    PasswordEncoder passwordEncoder;

    FilialMapper filialMapper;

    @Autowired
    public FilialServiceImpl(UserRepository userRepository,
                             FilialRepository organizationsRepository,
                             PasswordEncoder passwordEncoder, FilialMapper filialMapper) {
        this.userRepository = userRepository;
        this.organizationsRepository = organizationsRepository;
        this.passwordEncoder = passwordEncoder;
        this.filialMapper = filialMapper;

    }

    @Override
    @Transactional(readOnly = true)
    public List<FilialResponse> getAllUserOrganizations(String pin, String password) {
        User user = userRepository.getUserByPinQuery(pin);
        if (user == null)
            throw new CustomException(CustomError.USER_NOT_FOUND);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CustomException(CustomError.USER_NOT_AUTHENTICATE);

        List<Filial> organizations = new ArrayList<>();
        if (user.getFilials().isEmpty())
            throw new CustomException(CustomError.USER_NOT_HAVE_ANY_ORGANISATION);

        for (Filial organizationId : user.getFilials()) {
            Filial organization = organizationsRepository.findById(organizationId.getId())
                    .orElseThrow(() -> new CustomException(CustomError.ORGANISATION_NOT_FOUND));
            organizations.add(organization);
        }

        return organizations.stream().map(filialMapper::entityToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FilialResponse saveOrganization(FilialRequest organizationModel) {
        Filial organization = filialMapper.requestToEntity(organizationModel);
        String newFilCode = generateNextFilCode();
        organization.setFilCode(newFilCode);
        try {
            Filial savedOrganization = organizationsRepository.save(organization);
            return filialMapper.entityToResponse(savedOrganization);
        } catch (RuntimeException e) {
            throw new RuntimeException("Не удалось сохранить Организацию в базе данных", e);
        }
    }


    private String generateNextFilCode() {
        String minFilialCode = "00001";


        List<String> existingScreens = organizationsRepository.findAllFilCodes();

        if (!existingScreens.isEmpty()) {
            //мин значение с бд
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                String nextScreenValue = String.format("%05d", i);
                if (!existingScreens.contains(nextScreenValue)) {
                    minFilialCode = nextScreenValue;
                    break;
                }
            }
        }

        return minFilialCode;
    }
    @Transactional
    @Override
    public Page<FilialResponse> getAllOrganizations(int page, int size, Optional<Boolean> sortOrder, String sortBy, Optional<Long> id, Optional<String> filCode) {
        Pageable paging;

        // Установим значение по умолчанию для sortBy, если оно пустое или null
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id"; // или любое другое поле по умолчанию
        }

        Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
        paging = PageRequest.of(page, size, direction, sortBy);

        Page<Filial> filialsPage;

        // Фильтрация на основе предоставленных параметров
        if (id.isPresent() && filCode.isPresent()) {
            filialsPage = organizationsRepository.findByIdAndFilCode(id.get(), filCode.get(), paging);
        } else if (id.isPresent()) {
            filialsPage = organizationsRepository.findById(id.get(), paging);
        } else if (filCode.isPresent()) {
            filialsPage = organizationsRepository.findByFilCode(filCode.get(), paging);
        } else {
            filialsPage = organizationsRepository.findAll(paging);
        }

        return filialsPage.map(filialMapper::entityToResponse);
    }

    @Override
    @Transactional
    public FilialResponse getOrganizationById(Long id) {
        Filial organizations = this.organizationsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Организация с таким id не существует!"));
        return filialMapper.entityToResponse(organizations);
    }

    @Override
    @Transactional
    public void deleteOrganizationById(Long id) {
        Filial organizations = this.organizationsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Организация с таким id не существует!"));
        organizationsRepository.deleteById(organizations.getId());
    }

    @Override
    @Transactional
    public FilialResponse updateOrganization(FilialRequest organizationModel, Long id) {
        Filial organization = this.organizationsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Организация с таким id не существует!"));

        filialMapper.update(organization,organizationModel);

        Filial savedFilial = organizationsRepository.save(organization);
        return filialMapper.entityToResponse(savedFilial);
    }

//    @Transactional(readOnly = true)
//    public List<Organization> getOrganizationsForExpert(User expert) {
//        return userRepository.findOrganizationsByExpert(expert);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Organization> getOrganizationsWithExpert(User expertAll) {
//        return userRepository.findOrganizationsByExpert(expertAll);
//    }


//    @JsonIgnore
//    private OrganizationModel organizationToModel(Organization organizations) {
//        return this.modelMapper.map(organizations, OrganizationModel.class);
//    }
//
//    @JsonIgnore
//    private Organization modelToOrganization(OrganizationModel organizationModel) {
//        return this.modelMapper.map(organizationModel, Organization.class);
//    }
}