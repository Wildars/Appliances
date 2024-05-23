package com.example.appliances.specification;

import com.example.appliances.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> filterOrders(LocalDateTime startDate, LocalDateTime endDate, Long status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

//            // Фильтр по дате создания
//            if (startDate != null) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
//            }
//            if (endDate != null) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
//            }
//
//            // Фильтр по статусу
//            if (status != null) {
//                Join<Order, SystemStatus> statusJoin = root.join("status", JoinType.INNER);
//                predicates.add(criteriaBuilder.equal(statusJoin.get("id"), status));
//            }
//
//            // Фильтр по типу заказа (по умолчанию Order)
//            predicates.add(criteriaBuilder.equal(root.get("orderType"), OrderType.ORDER));
//
//            // Преобразование сущности Order в модель OrderResponse
//            query.multiselect(root.get("id"), root.get("createdAt"), root.get("status"), root.get("orderType"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
