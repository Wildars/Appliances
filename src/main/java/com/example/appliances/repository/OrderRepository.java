package com.example.appliances.repository;

import com.example.appliances.entity.Order;
import com.example.appliances.entity.SaleStatus;
import com.example.appliances.entity.User;
import com.example.appliances.specification.OrderSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> , JpaSpecificationExecutor<Order> {
    default List<Order> findAllFiltered(LocalDateTime startDate, LocalDateTime endDate, Long status) {
        Specification<Order> specification = OrderSpecifications.filterOrders( startDate, endDate, status);
        return findAll(specification);
    }

    @Query("SELECT s.numberNakladnoy FROM Order s WHERE s.numberNakladnoy LIKE CONCAT(:datePrefix, '%')")
    List<String> findNakladnoyNumbersByDate(String datePrefix);

    Page<Order> findByUser(User currentUser, Pageable paging);


    @Query("SELECT COUNT(o) FROM Order o")
    Long countAllOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.id = 4")
    Long countSuccessfulOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.id = 3")
    Long countUnsuccessfulOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.id = 1")
    Long countAcceptedOrders();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status.id = 2")
    Long countSendetOrders();


    @Query("SELECT m.name, m.surname, SUM(o.totalAmount) as revenue " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "JOIN oi.filialItem fi " +
            "JOIN fi.filial f " +
            "JOIN o.manager m " +
            "WHERE f.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate " +
            "GROUP BY m.id, m.name, m.surname " +
            "ORDER BY revenue DESC")
    List<Object[]> findTopSellingManagersByFilialIdAndDateRange(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT AVG(o.totalAmount) FROM Order o JOIN o.orderItems oi JOIN oi.filialItem fi WHERE fi.filial.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate")
    Double findAverageCheckByFilialIdAndDateRange(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.orderItems oi JOIN oi.filialItem fi WHERE fi.filial.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate")
    Long findTransactionCountByFilialIdAndDateRange(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT o.client.id) FROM Order o JOIN o.orderItems oi JOIN oi.filialItem fi WHERE fi.filial.id = :filialId AND o.schedule BETWEEN :startDate AND :endDate")
    Long findVisitorCountByFilialIdAndDateRange(@Param("filialId") Long filialId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    Page<Order> findByDateDeliveryAndCreationDateAndManagerIdAndStatus(Date date, Date date1, Long aLong, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByDateDeliveryAndCreationDateAndManagerId(Date date, Date date1, Long aLong, Pageable paging);

    Page<Order> findByDateDeliveryAndCreationDateAndStatus(Date date, Date date1, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByDateDeliveryAndManagerIdAndStatus(Date date, Long aLong, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByCreationDateAndManagerIdAndStatus(Date date, Long aLong, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByDateDeliveryAndCreationDate(Date date, Date date1, Pageable paging);

    Page<Order> findByDateDeliveryAndManagerId(Date date, Long aLong, Pageable paging);

    Page<Order> findByDateDeliveryAndStatus(Date date, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByCreationDateAndManagerId(Date date, Long aLong, Pageable paging);

    Page<Order> findByCreationDateAndStatus(Date date, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByManagerIdAndStatus(Long aLong, SaleStatus saleStatus, Pageable paging);

    Page<Order> findByDateDelivery(Date date, Pageable paging);

    Page<Order> findByCreationDate(Date date, Pageable paging);

    Page<Order> findByManagerId(Long aLong, Pageable paging);

    Page<Order> findByStatus(SaleStatus saleStatus, Pageable paging);
}
