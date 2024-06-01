package com.example.appliances.api;

import com.example.appliances.model.request.TransferRequest;
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

    // Другие методы контроллера Transfer, такие как получение истории перегонки и т. д., могут быть добавлены здесь.
}
