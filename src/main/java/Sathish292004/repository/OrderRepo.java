package Sathish292004.repository;

import Sathish292004.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    @Query("""
            SELECT DISTINCT o
            FROM orders o
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.product
            WHERE o.orderId = :orderId
            """)
    Optional<Order> findByOrderId(@Param("orderId") String orderId);

    @Query("""
            SELECT DISTINCT o
            FROM orders o
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.product
            WHERE o.email = :email
            ORDER BY o.orderDate DESC
            """)
    List<Order> findByEmail(@Param("email") String email);

    @Override
    @Query("""
            SELECT DISTINCT o
            FROM orders o
            LEFT JOIN FETCH o.orderItems oi
            LEFT JOIN FETCH oi.product
            ORDER BY o.orderDate DESC
            """)
    List<Order> findAll();
}