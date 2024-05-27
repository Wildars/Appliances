package com.example.appliances.service.impl;


import com.example.appliances.entity.*;
import com.example.appliances.enums.SaleStatusEnum;
import com.example.appliances.exception.CustomError;
import com.example.appliances.exception.CustomException;
import com.example.appliances.exception.OrderNotFoundException;
import com.example.appliances.exception.SaleItemNotFoundException;
import com.example.appliances.mapper.OrderMapper;
import com.example.appliances.model.request.OrderItemRequest;
import com.example.appliances.model.request.OrderRequest;
import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.response.OrderResponse;
import com.example.appliances.repository.OrderRepository;
import com.example.appliances.repository.SaleStatusRepository;
import com.example.appliances.repository.UserRepository;
import com.example.appliances.service.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    ProductService productService;
    UserService userService;

    ClientService clientService;

    SaleStatusRepository saleStatusRepository;
    UserRepository userRepository;

    StorageService storageService;

    SaleStatusRepository systemStatusRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductService productService, UserService userService, ClientService clientService, SaleStatusRepository saleStatusRepository, UserRepository userRepository, StorageService storageService, SaleStatusRepository systemStatusRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.userService = userService;
        this.clientService = clientService;
        this.saleStatusRepository = saleStatusRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.systemStatusRepository = systemStatusRepository;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.requestToEntity(orderRequest);

        // Устанавливаем текущего пользователя
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);

        // Устанавливаем начальный статус заказа
        SaleStatus status = saleStatusRepository.findById(SaleStatusEnum.ACCEPTED.getId()).orElseThrow(() -> new RuntimeException("SaleStatus not found"));
        order.setStatus(status);

        // Перебираем элементы заказа из OrderRequest и устанавливаем им ссылку на заказ
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        // Получаем информацию о товаре из склада и проверяем наличие
        List<UUID> productIds = orderRequest.getOrderItems().stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toList());
        List<Product> products = storageService.getProductsById(productIds);

        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            storageService.checkProductAvailability(itemRequest.getProductId(), itemRequest.getQuantity());
            // Обновляем количество товара на складе (уменьшаем)
//            storageService.updateStockByProductId(itemRequest.getProductId(), itemRequest.getQuantity());
        }

        // Получаем информацию о клиенте и его скидке
        Client client = clientService.findById(orderRequest.getClientId());
        order.setClient(client);

        // Вычисляем общую сумму заказа
        BigDecimal totalAmount = orderRequest.getOrderItems().stream()
                .map(item -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount.doubleValue());

        // Вычисляем итоговую стоимость с учетом скидки
        double totalAmountWithDiscount = calculateTotalPriceWithDiscount(order);
        order.setTotalAmount(totalAmountWithDiscount);

        // Генерируем и устанавливаем номер накладной
        String newScreen = generateNextNakladnoy();
        order.setNumberNakladnoy(newScreen);

        Order savedOrder = orderRepository.save(order);

        // Отправка SMS клиенту, если нужно
//    String messageBody = String.format("Здравствуйте, %s! Ваш заказ на сумму %.2f был успешно принят.",
//            client.getName(), order.getTotalAmount());
//    twilioService.sendSms(client.getPhoneNumber(), messageBody);

        // Используем маппер для преобразования сущности Order в OrderResponse
        return orderMapper.entityToResponse(savedOrder);
    }



    //смс оповещение
//    public void makeSMS(String toPhoneNumber) {
//        try {
//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//            Call call = Call.creator(
//                            new PhoneNumber(toPhoneNumber),
//                            new PhoneNumber(TWILIO_PHONE_NUMBER),
//                            URI.create("http://demo.twilio.com/docs/voice.xml"))
//                    .create();
//        } catch (ApiException e) {
//            if (e.getMessage().contains("unverified")) {
//                // Handle the case where the number is unverified
//                System.err.println("The number " + toPhoneNumber + " is unverified. Trial accounts may only make calls to verified numbers.");
//            } else {
//                // Handle other API exceptions
//                System.err.println("An error occurred: " + e.getMessage());
//            }
//        }
//    }


    @Override
    public void doneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Находим статус VERIFIED в базе данных
        SaleStatus verifiedStatus = systemStatusRepository.findById(SaleStatusEnum.SENDET.getId()).orElseThrow(() -> new RuntimeException("SystemStatus not found"));

        // Меняем статус заказа на VERIFIED
        order.setStatus(verifiedStatus);

        // Сохраняем обновленный заказ
        orderRepository.save(order);
    }
    public Double calculateTotalAmount(OrderRequest orderRequest) {
        Double totalAmount = 0.0;

        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItems();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            UUID productId = orderItemRequest.getProductId();
            Integer quantity = orderItemRequest.getQuantity();

            // Найдите товар (Product) по его идентификатору
            Product product = productService.getById(productId);

            // Если товар найден, добавьте его стоимость к общей сумме заказа
            if (product != null) {
                BigDecimal price = product.getPrice();
                totalAmount += price.multiply(BigDecimal.valueOf(quantity)).doubleValue();
            }
        }

        return totalAmount;
    }

    @Override
    @Transactional
    public OrderResponse getOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Hibernate.initialize(order.getOrderItems());
            for (OrderItem orderItem : order.getOrderItems()) {
                Hibernate.initialize(orderItem.getProduct().getPhotoPaths());
            }
            return orderMapper.entityToResponse(order);
        } else {
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
    }

    @Override
    @Transactional
    public Page<OrderResponse> getAllByUser(int page,
                                      int size,
                                      Optional<Boolean> sortOrder,
                                      String sortBy) {
        User currentUser = userService.getCurrentUser();

        // Создаем объект Pageable для запроса страницы заказов
        Pageable paging = PageRequest.of(page, size);

        // Если указано направление сортировки и поле для сортировки, применяем их
        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        }

        // Получаем страницу заказов текущего пользователя из репозитория
        Page<Order> userOrdersPage = orderRepository.findByUser(currentUser, paging);

        // Преобразуем страницу заказов в страницу ответов и возвращаем
        return userOrdersPage.map(orderMapper::entityToResponse);
    }


    @Override
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            orderMapper.update(order, orderRequest);
            order = orderRepository.save(order);
            return orderMapper.entityToResponse(order);
        } else {
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
    }

    @Override
    @Transactional
    public List<Order> getAllFiltered(LocalDateTime startDate, LocalDateTime endDate, Long status) {
        return orderRepository.findAllFiltered(startDate, endDate, status);
    }

    @Override
    public Page<OrderResponse> getAll(int page,
                                      int size,
                                      Optional<Boolean> sortOrder,
                                      String sortBy) {


        Pageable paging = null;


        if (sortOrder.isPresent()){
            Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page, size, direction, sortBy);
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Order> saleItemsPage = orderRepository.findAll(paging);

        return saleItemsPage.map(orderMapper::entityToResponse);
    }


    public String generateNextNakladnoy() {
        // Текущая дата в формате yyyyMMdd
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Поиск всех номеров накладных, которые начинаются с текущей даты
        List<String> existingNakladnoyNumbers = orderRepository.findNakladnoyNumbersByDate(currentDate);

        int nextNumber = 1;

        if (!existingNakladnoyNumbers.isEmpty()) {
            // Поиск максимального номера для текущей даты и увеличение его на 1
            for (String number : existingNakladnoyNumbers) {
                String[] parts = number.split("\\+");
                if (parts.length == 2 && parts[0].equals(currentDate)) {
                    int currentNumber = Integer.parseInt(parts[1]);
                    if (currentNumber >= nextNumber) {
                        nextNumber = currentNumber + 1;
                    }
                }
            }
        }

        // Форматирование следующего номера накладной
        String nextNakladnoyNumber = String.format("%s%05d", currentDate, nextNumber);

        return nextNakladnoyNumber;
    }



    private double calculateTotalPriceWithDiscount(Order saleItem) {
        Client client = saleItem.getClient();
        DiscountCategory discountCategory = client.getDiscountCategory();

        double discountPercentage = (discountCategory != null) ? discountCategory.getPercentage() : 0.0;
        double discount = saleItem.getTotalAmount() * (discountPercentage / 100.0);

        return saleItem.getTotalAmount() - discount;
    }





    @Override
    @Transactional
    public void sendOrder(Long saleItemId, SaleItemElementRequest request) {

        updateQueueEntryStatus(saleItemId, SaleStatusEnum.SENDET, null);
    }
    @Override
    @Transactional
    public void doneOrder(Long saleItemId, SaleItemElementRequest request) {

        updateQueueEntryStatus(saleItemId, SaleStatusEnum.DONE, null);
    }
    @Override
    @Transactional
    public void rejectOrder(Long queueEntryId, SaleItemElementRequest request) {
//        Order saleItem = orderRepository.findById(queueEntryId)
//                .orElseThrow(() -> new SaleItemNotFoundException("Покупка с ID " + queueEntryId + " не найдена"));
//
//        // Получить информацию о товаре, который был куплен
//        List<Product> products = saleItem.getProducts();
//
//        // Итерируем по товарам и увеличиваем количество каждого товара на складе
//        for (Product product : products) {
//            storageService.returnStockByProductId(product.getId(), saleItem.getQuantity());
//        }
//
//        // Обновить статус покупки на "REJECTED" и добавить комментарии
//        updateQueueEntryStatus(queueEntryId, SaleStatusEnum.REJECTED, request.getComments());
    }

    private void updateQueueEntryStatus(Long queueEntryId, SaleStatusEnum status, String description) {
        Order queueEntry = orderRepository.findById(queueEntryId)
                .orElseThrow(() -> new CustomException(CustomError.ENTITY_NOT_FOUND));

        SaleStatus queueEntryStatus = saleStatusRepository.findById(status.getId())
                .orElseThrow(() -> new EntityNotFoundException("QueueEntryStatus with id " + status.getId() + " not found"));

        queueEntry.setStatus(queueEntryStatus);

        if (status == SaleStatusEnum.REJECTED) {
            queueEntry.setComment(description);
        }

        orderRepository.save(queueEntry);
    }


    @Override
    @Transactional
    public byte[] generateOrderPdf(Map<String, Object> orderData) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Заголовок
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("Заказ #" + orderData.get("id"), titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Информация о заказе
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell(createCell("Клиент:", orderData.get("clientName").toString()));
            table.addCell(createCell("Дата заказа:", orderData.get("created_date").toString()));
            table.addCell(createCell("Адрес:", orderData.get("address").toString()));
            table.addCell(createCell("Телефон:", orderData.get("phoneNumber").toString()));
            table.addCell(createCell("Общая сумма:", orderData.get("totalAmount").toString()));
            document.add(table);
            document.add(new Paragraph("\n"));

            // Товары в заказе
            PdfPTable productsTable = new PdfPTable(3);
            productsTable.setWidthPercentage(100);
            productsTable.addCell("Наименование товара");
            productsTable.addCell("Количество");
            productsTable.addCell("Цена");

            for (Map<String, Object> product : (Iterable<Map<String, Object>>) orderData.get("orderItems")) {
                productsTable.addCell(product.get("productName").toString());
                productsTable.addCell(product.get("quantity").toString());
                productsTable.addCell(product.get("price").toString());
            }

            document.add(productsTable);
            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при генерации PDF", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Возникла ошибка", e);
        }
    }

    private PdfPCell createCell(String label, String value) {
        PdfPCell cell = new PdfPCell(new Paragraph(label));
        cell.setPadding(5);
        cell.setColspan(1);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        PdfPCell valueCell = new PdfPCell(new Paragraph(value));
        valueCell.setPadding(5);
        valueCell.setColspan(1);
        valueCell.setBorder(PdfPCell.NO_BORDER);
        valueCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        valueCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        return cell;
    }
}