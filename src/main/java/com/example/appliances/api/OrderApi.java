package com.example.appliances.api;

import com.example.appliances.entity.Order;
import com.example.appliances.entity.SaleStatus;
import com.example.appliances.model.request.OrderRequest;
import com.example.appliances.model.request.OrderRequestDelivery;
import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.response.OrderResponse;
import com.example.appliances.service.OrderService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderApi {

    private final OrderService orderService;

    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    @PostMapping("/delivery")
    public ResponseEntity<OrderResponse> createOrderDelivery(@RequestBody OrderRequestDelivery orderRequest) {
        OrderResponse createdOrder = orderService.createOrderDelivery(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder( @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.getOrder(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderRequest orderRequest) {
        OrderResponse updatedOrder = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/filtered")
//    public List<Order> getAllOrders(
//            @RequestParam(required = false) LocalDateTime startDate,
//            @RequestParam(required = false) LocalDateTime endDate,
//            @RequestParam(required = false) Long status
//    ) {
//        return orderService.getAllFiltered(startDate, endDate, status);
//    }


    @PatchMapping("/{orderId}/verify")
    public ResponseEntity<String> verifyOrder(@PathVariable Long orderId) {
        try {
            orderService.doneOrder(orderId);
            return ResponseEntity.ok("Order verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to verify order: " + e.getMessage());
        }
    }


    @GetMapping("/page")
    public Page<OrderResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "25") int size,
                                                      @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                      @RequestParam(required = false) String sortBy,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<Date> dateDelivery,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<Date> creationDate,
                                                      @RequestParam(required = false) Optional<Long> managerId,
                                                      @RequestParam(required = false) Optional<SaleStatus> status) {
        return orderService.getAll(page, size, sortOrder, sortBy, dateDelivery, creationDate, managerId, status);
    }


    @GetMapping("/page/byUser")
    public Page<OrderResponse> findAllBySpecificationByUser(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "25") int size,
                                                      @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                      @RequestParam(required = false) String sortBy) {
        return orderService.getAllByUser(page, size, sortOrder, sortBy);
    }


//    @GetMapping("/order/{orderId}/pdf")
//    public ResponseEntity<byte[]> getOrderPdf(@PathVariable Long orderId) {
//        // Здесь вам нужно получить данные о заказе, например, из вашего сервиса заказов
//        Map<String, Object> orderData = getOrderDataById(orderId);
//
//        // Проверяем, есть ли данные о заказе
//        if (orderData == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Генерируем PDF
//        byte[] pdfBytes = generateOrderPdf(orderData);
//
//        // Создаем HTTP-заголовки для ответа
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("filename", "order_" + orderId + ".pdf");
//        headers.setContentLength(pdfBytes.length);
//
//        // Возвращаем PDF в ответе
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    }
//
//    private Map<String, Object> getOrderDataById(Long orderId) {
//        // Логика для получения данных о заказе по его идентификатору orderId
//        // Замените это на вашу реализацию
//        Map<String, Object> orderData = new HashMap<>();
//        // Здесь должен быть ваш код для получения данных о заказе по orderId
//        // Пример:
//        orderData.put("id", orderId);
//        orderData.put("clientName", "Иван Иванов");
//        // Заполните остальные данные о заказе
//        return orderData;
//    }

//    private byte[] generateOrderPdf(Map<String, Object> orderData) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Document document = new Document();
//        try {
//            PdfWriter.getInstance(document, byteArrayOutputStream);
//            document.open();
//            // Добавьте содержимое PDF-документа здесь
//            document.add(new com.itextpdf.text.Paragraph("Order ID: " + orderData.get("id")));
//            document.add(new com.itextpdf.text.Paragraph("Client Name: " + orderData.get("clientName")));
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//        return byteArrayOutputStream.toByteArray();
//    }


    @GetMapping("/api/statistics/orders/count")
    public Long countAllOrders() {
        return orderService.countAllOrders();
    }

    @GetMapping("/api/statistics/orders/successful")
    public Long countSuccessfulOrders() {
        return orderService.countSuccessfulOrders();
    }

    @GetMapping("/api/statistics/orders/unsuccessful")
    public Long countUnsuccessfulOrders() {
        return orderService.countUnsuccessfulOrders();
    }

    @GetMapping("/api/statistics/orders/accepted")
    public Long countAcceptedOrders() {
        return orderService.countAcceptedOrders();
    }

    @GetMapping("/api/statistics/orders/sendet")
    public Long countSendetOrders() {
        return orderService.countSendetOrders();
    }



    @PutMapping("/{orderId}/reject")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> rejectOrder(
            @PathVariable Long orderId,
            @RequestBody SaleItemElementRequest request) {
        orderService.rejectOrder(orderId, request);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/api/statistics/orders")
    public Map<String, Long> getOrderStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalOrders", orderService.countAllOrders());
        statistics.put("successfulOrders", orderService.countSuccessfulOrders());
        statistics.put("unsuccessfulOrders", orderService.countUnsuccessfulOrders());
        statistics.put("aceptedOrders", orderService.countAcceptedOrders());
        statistics.put("sendetOrders", orderService.countSendetOrders());
        return statistics;
    }
}

