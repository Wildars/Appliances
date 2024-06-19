package com.example.appliances.service;

import com.example.appliances.model.request.ReturnFilialRequest;
import com.example.appliances.model.response.ReturnFilialResponse;

public interface ReturnService {
    public void refuseReturn(Long returnId);

    public void revokeReturn(Long returnId);

    public void acceptReturn(Long returnId);

    public ReturnFilialResponse createReturn(ReturnFilialRequest request);
}
