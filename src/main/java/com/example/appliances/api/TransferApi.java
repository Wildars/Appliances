package com.example.appliances.api;

import com.example.appliances.entity.Transfer;
import com.example.appliances.model.request.TransferRequest;
import com.example.appliances.model.response.TransferItemResponse;
import com.example.appliances.model.response.TransferResponse;
import com.example.appliances.service.TransferService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/transfers")
public class TransferApi {

    TransferService transferService;

    @Autowired
    public TransferApi(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transferProducts")
    public ResponseEntity<String> transferProducts(@RequestBody List<TransferRequest> transferRequests) {
        transferService.transferProducts(transferRequests);
        return new ResponseEntity<>("Товары успешно перегнаны со склада в филиал", HttpStatus.OK);
    }

    @GetMapping("/transfer/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        TransferResponse transfer = transferService.findByIdransfers(id);
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @GetMapping("/transfer/all")
    public ResponseEntity<List<TransferResponse>> getAllTransfers() {
        List<TransferResponse> transfers = transferService.findAllTransfers();
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    @GetMapping("/transfer/item/{id}")
    public ResponseEntity<TransferItemResponse> getTransferItemById(@PathVariable Long id) {
        TransferItemResponse transfer = transferService.findByIdransfersItem(id);
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @GetMapping("/transfer/item/all")
    public ResponseEntity<List<TransferItemResponse>> getAllTransfersItem() {
        List<TransferItemResponse> transfers = transferService.findAllransfersItem();
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }
}
