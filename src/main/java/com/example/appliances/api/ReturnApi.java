package com.example.appliances.api;

import com.example.appliances.model.request.ReturnFilialRequest;
import com.example.appliances.model.response.ReturnFilialResponse;
import com.example.appliances.service.ReturnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
public class ReturnApi {
    private final ReturnService returnService;

    public ReturnApi(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReturnFilialResponse> createReturn(@RequestBody ReturnFilialRequest request) {
        try {
            ReturnFilialResponse response = returnService.createReturn(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{returnId}/accept")
    public ResponseEntity<String> acceptReturn(@PathVariable Long returnId) {
        try {
            returnService.acceptReturn(returnId);
            return ResponseEntity.ok("Return accepted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during return acceptance: " + e.getMessage());
        }
    }

    @PostMapping("/{returnId}/revoke")
    public ResponseEntity<String> revokeReturn(@PathVariable Long returnId) {
        try {
            returnService.revokeReturn(returnId);
            return ResponseEntity.ok("Return revoked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during return revocation: " + e.getMessage());
        }
    }

    @PostMapping("/{returnId}/refuse")
    public ResponseEntity<String> refuseReturn(@PathVariable Long returnId) {
        try {
            returnService.refuseReturn(returnId);
            return ResponseEntity.ok("Return refused successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during return refusal: " + e.getMessage());
        }
    }
}
