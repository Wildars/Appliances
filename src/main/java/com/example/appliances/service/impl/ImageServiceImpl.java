package com.example.appliances.service.impl;

import com.example.appliances.entity.Image;
import com.example.appliances.mapper.ImageMapper;
import com.example.appliances.model.request.ImageRequest;
import com.example.appliances.repository.ImageRepo;
import com.example.appliances.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//@NoArgsConstructor
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepo imageRepo;
    private final ImageMapper imageMapper;

    @Override
    public Long uploadPhoto(ImageRequest imageRequest) {

        Image image = imageMapper.toEntity(imageRequest);

        Long id = imageRepo.save(image).getId();

        return id;

    }
}

