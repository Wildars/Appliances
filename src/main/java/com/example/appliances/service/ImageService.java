package com.example.appliances.service;


import com.example.appliances.model.request.ImageDto;

public interface ImageService {
    Long uploadPhoto( ImageDto imageDto);
}
