package com.example.appliances.service.impl;


import com.example.appliances.entity.*;
import com.example.appliances.enums.SaleStatusEnum;
import com.example.appliances.exception.CustomError;
import com.example.appliances.exception.CustomException;
import com.example.appliances.exception.OrderNotFoundException;
import com.example.appliances.mapper.OrderMapper;
import com.example.appliances.model.request.OrderItemRequest;
import com.example.appliances.model.request.OrderRequest;
import com.example.appliances.model.request.OrderRequestDelivery;
import com.example.appliances.model.request.SaleItemElementRequest;
import com.example.appliances.model.response.OrderResponse;
import com.example.appliances.repository.ManagerRepository;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    ShiftScheduleService shiftScheduleService;
    ProductService productService;
    UserService userService;

    ManagerRepository managerRepository;
    TwilioService twilioService;

    ClientService clientService;

    FilialItemService filialItemService;
    SaleStatusRepository saleStatusRepository;
    UserRepository userRepository;

    StorageService storageService;

    SaleStatusRepository systemStatusRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ShiftScheduleService shiftScheduleService, ProductService productService, UserService userService, ManagerRepository managerRepository, TwilioService twilioService, ClientService clientService, FilialItemService filialItemService, SaleStatusRepository saleStatusRepository, UserRepository userRepository, StorageService storageService, SaleStatusRepository systemStatusRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.shiftScheduleService = shiftScheduleService;
        this.productService = productService;
        this.userService = userService;
        this.managerRepository = managerRepository;
        this.twilioService = twilioService;
        this.clientService = clientService;
        this.filialItemService = filialItemService;
        this.saleStatusRepository = saleStatusRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
        this.systemStatusRepository = systemStatusRepository;
    }


    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Преобразуем запрос в сущность Order
        Order order = orderMapper.requestToEntity(orderRequest);

        order.setIsDelivery(false);
        // Устанавливаем текущего пользователя
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);

        // Устанавливаем начальный статус заказа
        SaleStatus status = saleStatusRepository.findById(SaleStatusEnum.DONE.getId())
                .orElseThrow(() -> new RuntimeException("SaleStatus not found"));
        order.setStatus(status);

        // Перебираем элементы заказа из OrderRequest и устанавливаем им ссылку на заказ
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(itemRequest.getQuantity());

            // Проверяем наличие товара и обновляем количество товара на складе
            filialItemService.checkProductAvailability(itemRequest.getFilialItemId(), itemRequest.getQuantity());
            filialItemService.updateStockByProductId(itemRequest.getFilialItemId(), itemRequest.getQuantity());

            // Получаем FilialItem и устанавливаем его в orderItem
            FilialItem filialItem = filialItemService.getFilialItemById(itemRequest.getFilialItemId());
            orderItem.setFilialItem(filialItem);

            // Добавляем orderItem в список orderItems
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Получаем информацию о клиенте и его скидке
        Client client = clientService.findById(orderRequest.getClientId());
        order.setClient(client);

        Manager manager = managerRepository.findById(orderRequest.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        LocalDateTime now = LocalDateTime.now();
        if (!shiftScheduleService.isManagerWorking(manager.getId(), now)) {
            throw new RuntimeException("Manager is not working at this time");
        }
        order.setManager(manager);

        // Вычисляем общую сумму заказа
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> {
                    Product product = item.getFilialItem().getProduct();
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

        // Устанавливаем дополнительные поля заказа
        order.setAddress(orderRequest.getAddress());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setName(orderRequest.getName());
        order.setComment(orderRequest.getComment());

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Отправка SMS клиенту, если нужно
//         String messageBody = String.format("Здравствуйте, %s! Ваш заказ на сумму %.2f был успешно принят. Номер накладной: %s",
//                 client.getName(),order.getTotalAmount(),order.getNumberNakladnoy());
//         twilioService.sendSms(client.getPhoneNumber(), messageBody);

        // Преобразуем сохраненный заказ в ответ
        return orderMapper.entityToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse createOrderDelivery(OrderRequestDelivery orderRequest) {
        // Преобразуем запрос в сущность Order
        Order order = orderMapper.requestToEntityDelivery(orderRequest);

        order.setIsDelivery(true);
        // Устанавливаем текущего пользователя
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);

        // Устанавливаем начальный статус заказа
        SaleStatus status = saleStatusRepository.findById(SaleStatusEnum.ACCEPTED.getId())
                .orElseThrow(() -> new RuntimeException("SaleStatus not found"));
        order.setStatus(status);

        // Перебираем элементы заказа из OrderRequest и устанавливаем им ссылку на заказ
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(itemRequest.getQuantity());

            // Проверяем наличие товара и обновляем количество товара на складе
            filialItemService.checkProductAvailability(itemRequest.getFilialItemId(), itemRequest.getQuantity());
            filialItemService.updateStockByProductId(itemRequest.getFilialItemId(), itemRequest.getQuantity());

            // Получаем FilialItem и устанавливаем его в orderItem
            FilialItem filialItem = filialItemService.getFilialItemById(itemRequest.getFilialItemId());
            orderItem.setFilialItem(filialItem);

            // Добавляем orderItem в список orderItems
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Получаем информацию о клиенте и его скидке
        Client client = clientService.findById(orderRequest.getClientId());
        order.setClient(client);

        // Вычисляем общую сумму заказа
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> {
                    Product product = item.getFilialItem().getProduct();
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount.doubleValue());

        // Устанавливаем менеджера и проверяем его занятость
        Manager manager = managerRepository.findById(orderRequest.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        LocalDateTime now = LocalDateTime.now();
        if (!shiftScheduleService.isManagerWorking(manager.getId(), now)) {
            throw new RuntimeException("Manager is not working at this time");
        }
        order.setManager(manager);

        // Вычисляем итоговую стоимость с учетом скидки
        double totalAmountWithDiscount = calculateTotalPriceWithDiscount(order);
        order.setTotalAmount(totalAmountWithDiscount);

        // Генерируем и устанавливаем номер накладной
        String newScreen = generateNextNakladnoy();
        order.setNumberNakladnoy(newScreen);

        // Устанавливаем дополнительные поля заказа
        order.setDateDelivery(orderRequest.getDateDelivery());
        order.setAddress(orderRequest.getAddress());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setName(orderRequest.getName());
        order.setComment(orderRequest.getComment());
        order.setSchedule(orderRequest.getSchedule());

        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);

        // Отправка SMS клиенту, если нужно
//         String messageBody = String.format("Здравствуйте, %s! Ваш заказ на сумму %.2f был успешно принят. Номер накладной: %s",
//                 client.getName(),order.getTotalAmount(),order.getNumberNakladnoy());
//         twilioService.sendSms(client.getPhoneNumber(), messageBody);

        // Преобразуем сохраненный заказ в ответ
        return orderMapper.entityToResponse(savedOrder);
    }



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
//    public Double calculateTotalAmount(OrderRequest orderRequest) {
//        Double totalAmount = 0.0;
//
//        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItems();
//        for (OrderItemRequest orderItemRequest : orderItemRequests) {
//            UUID productId = orderItemRequest.getProductId();
//            Integer quantity = orderItemRequest.getQuantity();
//
//            // Найдите (Product) по его идентификатору
//            Product product = productService.getById(productId);
//
//            // Если товар найден, добавьте его стоимость к общей сумме заказа
//            if (product != null) {
//                BigDecimal price = product.getPrice();
//                totalAmount += price.multiply(BigDecimal.valueOf(quantity)).doubleValue();
//            }
//        }
//
//        return totalAmount;
//    }


    @Override
    @Transactional
    public OrderResponse getOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Hibernate.initialize(order.getOrderItems());
            for (OrderItem orderItem : order.getOrderItems()) {
                Hibernate.initialize(orderItem.getFilialItem());
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

//    @Override
//    @Transactional
//    public List<Order> getAllFiltered(LocalDateTime startDate, LocalDateTime endDate, Long status) {
//        return orderRepository.findAllFiltered(startDate, endDate, status);
//    }

    @Override
    @Transactional
    public Page<OrderResponse> getAll(int page,
                                      int size,
                                      Optional<Boolean> sortOrder,
                                      String sortBy,
                                      Optional<Date> dateDelivery,
                                      Optional<Date> creationDate,
                                      Optional<Long> managerId,
                                      Optional<SaleStatus> status) {
        // Установим значение по умолчанию для sortBy, если оно пустое или null
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id"; // или любое другое поле по умолчанию
        }

        Pageable paging;
        Sort.Direction direction = sortOrder.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC;
        paging = PageRequest.of(page, size, direction, sortBy);

        Page<Order> ordersPage;

        // Фильтрация на основе предоставленных параметров
        if (dateDelivery.isPresent() && creationDate.isPresent() && managerId.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndCreationDateAndManagerIdAndStatus(
                    dateDelivery.get(), creationDate.get(), managerId.get(), status.get(), paging);
        } else if (dateDelivery.isPresent() && creationDate.isPresent() && managerId.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndCreationDateAndManagerId(
                    dateDelivery.get(), creationDate.get(), managerId.get(), paging);
        } else if (dateDelivery.isPresent() && creationDate.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndCreationDateAndStatus(
                    dateDelivery.get(), creationDate.get(), status.get(), paging);
        } else if (dateDelivery.isPresent() && managerId.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndManagerIdAndStatus(
                    dateDelivery.get(), managerId.get(), status.get(), paging);
        } else if (creationDate.isPresent() && managerId.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByCreationDateAndManagerIdAndStatus(
                    creationDate.get(), managerId.get(), status.get(), paging);
        } else if (dateDelivery.isPresent() && creationDate.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndCreationDate(
                    dateDelivery.get(), creationDate.get(), paging);
        } else if (dateDelivery.isPresent() && managerId.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndManagerId(
                    dateDelivery.get(), managerId.get(), paging);
        } else if (dateDelivery.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByDateDeliveryAndStatus(
                    dateDelivery.get(), status.get(), paging);
        } else if (creationDate.isPresent() && managerId.isPresent()) {
            ordersPage = orderRepository.findByCreationDateAndManagerId(
                    creationDate.get(), managerId.get(), paging);
        } else if (creationDate.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByCreationDateAndStatus(
                    creationDate.get(), status.get(), paging);
        } else if (managerId.isPresent() && status.isPresent()) {
            ordersPage = orderRepository.findByManagerIdAndStatus(
                    managerId.get(), status.get(), paging);
        } else if (dateDelivery.isPresent()) {
            ordersPage = orderRepository.findByDateDelivery(dateDelivery.get(), paging);
        } else if (creationDate.isPresent()) {
            ordersPage = orderRepository.findByCreationDate(creationDate.get(), paging);
        } else if (managerId.isPresent()) {
            ordersPage = orderRepository.findByManagerId(managerId.get(), paging);
        } else if (status.isPresent()) {
            ordersPage = orderRepository.findByStatus(status.get(), paging);
        } else {
            ordersPage = orderRepository.findAll(paging);
        }

        return ordersPage.map(orderMapper::entityToResponse);
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
    public void rejectOrder(Long orderId, SaleItemElementRequest request) {
        // Находим заказ по его идентификатору
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Изменяем статус заказа на "Отмененный"
        SaleStatus cancelledStatus = saleStatusRepository.findById(SaleStatusEnum.REJECTED.getId())
                .orElseThrow(() -> new RuntimeException("Cancelled SaleStatus not found"));
        order.setStatus(cancelledStatus);

        // Устанавливаем описание из запроса
        order.setDescription(request.getDescription());

        // Возвращаем товары на склад
        for (OrderItem orderItem : order.getOrderItems()) {
            FilialItem filialItem = orderItem.getFilialItem();
            int quantityToReturn = orderItem.getQuantity();
            filialItem.setQuantity(filialItem.getQuantity() + quantityToReturn);
            filialItemService.save(filialItem); // Предполагаем, что есть метод save для FilialItem
        }

        // Сохраняем обновленный заказ
        orderRepository.save(order);
    }
    private void updateQueueEntryStatus(Long queueEntryId, SaleStatusEnum status, String description) {
        Order queueEntry = orderRepository.findById(queueEntryId)
                .orElseThrow(() -> new CustomException(CustomError.ENTITY_NOT_FOUND));

        SaleStatus queueEntryStatus = saleStatusRepository.findById(status.getId())
                .orElseThrow(() -> new EntityNotFoundException("QueueEntryStatus with id " + status.getId() + " not found"));

        queueEntry.setStatus(queueEntryStatus);

        if (status == SaleStatusEnum.REJECTED) {
            queueEntry.setDescription(description);
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
            String orderId = orderData.get("id") != null ? orderData.get("id").toString() : "N/A";
            Paragraph title = new Paragraph("Заказ #" + orderId, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Информация о заказе
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.addCell(createCell("Клиент:", orderData.get("clientName") != null ? orderData.get("clientName").toString() : "N/A"));
            table.addCell(createCell("Дата заказа:", orderData.get("created_date") != null ? orderData.get("created_date").toString() : "N/A"));
            table.addCell(createCell("Адрес:", orderData.get("address") != null ? orderData.get("address").toString() : "N/A"));
            table.addCell(createCell("Телефон:", orderData.get("phoneNumber") != null ? orderData.get("phoneNumber").toString() : "N/A"));
            table.addCell(createCell("Общая сумма:", orderData.get("totalAmount") != null ? orderData.get("totalAmount").toString() : "N/A"));
            document.add(table);
            document.add(new Paragraph("\n"));

            // Товары в заказе
            PdfPTable productsTable = new PdfPTable(3);
            productsTable.setWidthPercentage(100);
            productsTable.addCell("Наименование товара");
            productsTable.addCell("Количество");
            productsTable.addCell("Цена");

            for (Map<String, Object> product : (Iterable<Map<String, Object>>) orderData.get("orderItems")) {
                productsTable.addCell(product.get("productName") != null ? product.get("productName").toString() : "N/A");
                productsTable.addCell(product.get("quantity") != null ? product.get("quantity").toString() : "0");
                productsTable.addCell(product.get("price") != null ? product.get("price").toString() : "0.00");
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




    @Override
    @Transactional(readOnly = true)
    public Long countAllOrders() {
        return orderRepository.countAllOrders();
    }
    @Override
    @Transactional(readOnly = true)
    public Long countSuccessfulOrders() {
        return orderRepository.countSuccessfulOrders();
    }
    @Override
    @Transactional(readOnly = true)
    public Long countUnsuccessfulOrders() {
        return orderRepository.countUnsuccessfulOrders();
    }
    @Override
    @Transactional(readOnly = true)
    public Long countAcceptedOrders() {
        return orderRepository.countAcceptedOrders();
    }
    @Override
    @Transactional(readOnly = true)
    public Long countSendetOrders() {
        return orderRepository.countSendetOrders();
    }
}