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



    @PostMapping("/create/transfer/wishlist")
    public ResponseEntity<String> transferProductsFromWishList(@RequestParam Long wishListFilialId, @RequestParam Long storageId) {
        try {
            transferService.transferProductsFromWishList(wishListFilialId, storageId);
            return ResponseEntity.ok("Products transferred successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during product transfer: " + e.getMessage());
        }
    }


//
//    @PostMapping("/transfer/{wishListFilialId}")
//    public ResponseEntity<String> transferProductsFromWishList(@PathVariable Long wishListFilialId) {
//        try {
//            wishListService.transferProductsFromWishList(wishListFilialId, fixedStorageId);
//            return ResponseEntity.ok("Products transferred successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during product transfer: " + e.getMessage());
//        }
//    }

    @PatchMapping("/return/{orderId}")
    public ResponseEntity<String> returnWishList(@PathVariable Long orderId) {
        try {
            transferService.returnWishList(orderId);
            return ResponseEntity.ok("WishList returned successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during returning WishList: " + e.getMessage());
        }
    }

    @PatchMapping("/reject/{wishListFilialId}")
    public ResponseEntity<String> rejectWishList(@PathVariable Long wishListFilialId) {
        try {
            transferService.rejectWishList(wishListFilialId);
            return ResponseEntity.ok("WishList rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during rejecting WishList: " + e.getMessage());
        }
    }

    @PostMapping("/receive/{wishListFilialId}")
    public ResponseEntity<String> receiveProductsFromWishList(@PathVariable Long wishListFilialId) {
        try {
            transferService.receiveProductsFromWishList(wishListFilialId);
            return ResponseEntity.ok("Products received successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during receiving products: " + e.getMessage());
        }
    }

    @PostMapping("/refuse/transfer/wishlist")
    public ResponseEntity<String> refuseProductsFromWishList(@RequestParam Long wishListFilialId) {
        try {
            transferService.refuseProductsFromWishList(wishListFilialId);
            return ResponseEntity.ok("Products refused successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during product refusal: " + e.getMessage());
        }
    }

}
