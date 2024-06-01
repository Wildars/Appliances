package com.example.appliances.service;

import com.example.appliances.model.request.TransferRequest;

import java.util.List;
import java.util.UUID;

public interface TransferService {

    public void transferProducts(List<TransferRequest> transferRequests);
}
