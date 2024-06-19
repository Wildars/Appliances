package com.example.appliances.service;

import com.example.appliances.entity.Transfer;
import com.example.appliances.model.request.TransferRequest;
import com.example.appliances.model.response.TransferItemResponse;
import com.example.appliances.model.response.TransferResponse;

import java.util.List;
import java.util.UUID;

public interface TransferService {

    public void transferProducts(List<TransferRequest> transferRequests);

    public TransferResponse findByIdransfers(Long id);

    public List<TransferItemResponse> findAllransfersItem();

    public TransferItemResponse findByIdransfersItem(Long id);

    public List<TransferResponse> findAllTransfers();

    public void transferProductsFromWishList(Long wishListFilialId, Long storageId);


//    public void rejectTranserFromWishList(Long wishListFilialId);

    public void returnWishList(Long orderId);

    public void rejectWishList(Long wishListFilialId);

    public void receiveProductsFromWishList(Long wishListFilialId);
    public void refuseProductsFromWishList(Long wishListFilialId);

}
