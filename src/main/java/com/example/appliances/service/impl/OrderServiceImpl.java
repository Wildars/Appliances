package com.example.appliances.service.impl;


import com.example.appliances.entity.Order;
import com.example.appliances.entity.OrderItem;
import com.example.appliances.entity.Product;
import com.example.appliances.entity.User;
import com.example.appliances.model.request.OrderItemRequest;
import com.example.appliances.model.request.OrderRequest;
import com.example.appliances.model.response.OrderResponse;
import com.example.appliances.repository.OrderRepository;
import com.example.appliances.repository.UserRepository;
import com.example.appliances.service.OrderService;
import com.example.appliances.service.ProductService;
import com.example.appliances.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    ProductService productService;
    UserService userService;

    UserRepository userRepository;

    SystemStatusRepository systemStatusRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductService productService, UserService userService, UserRepository userRepository, SystemStatusRepository systemStatusRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productService = productService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.systemStatusRepository = systemStatusRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderMapper.requestToEntity(orderRequest);
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);

        // Устанавливаем начальный статус заказа
        SystemStatus status = systemStatusRepository.findById(StatusSystemEnum.BLANK.getId()).orElseThrow(() -> new RuntimeException("SystemStatus not found"));
        order.setStatus(status);

        // Перебираем элементы заказа из OrderRequest и устанавливаем им ссылку на заказ
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        // Вычисляем общую сумму заказа
        Double totalAmount = calculateTotalAmount(orderRequest);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.entityToResponse(savedOrder);
    }


    @Override
    public void doneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Находим статус VERIFIED в базе данных
        SystemStatus verifiedStatus = systemStatusRepository.findById(StatusSystemEnum.VERIFICATED.getId()).orElseThrow(() -> new RuntimeException("SystemStatus not found"));

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
}