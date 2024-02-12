package com.example.appliances.service;


import com.example.appliances.model.request.ImageRequest;

public interface ImageService {
    Long uploadPhoto( ImageRequest imageRequest);
}
