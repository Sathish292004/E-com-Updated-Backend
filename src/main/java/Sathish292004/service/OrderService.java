package Sathish292004.service;

import Sathish292004.exception.OrderNotFoundException;
import Sathish292004.exception.UnauthorizedException;
import Sathish292004.model.Order;
import Sathish292004.model.OrderItem;
import Sathish292004.model.Product;
import Sathish292004.dto.request.OrderItemRequest;
import Sathish292004.dto.request.OrderRequest;
import Sathish292004.dto.response.OrderResponse;
import Sathish292004.dto.response.OrderItemResponse;
import Sathish292004.repository.OrderRepo;
import Sathish292004.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest request) {

        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : request.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (!product.isProductAvailable() || product.getStockQuantity() == 0) {
                throw new RuntimeException(product.getName() + " is out of stock");
            }

            if (itemReq.quantity() > product.getStockQuantity()) {
                throw new RuntimeException(
                        "Only " + product.getStockQuantity()
                                + " item(s) available for "
                                + product.getName()
                );
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());

            if (product.getStockQuantity() == 0) {
                product.setProductAvailable(false);
            }

            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();
            orderItems.add(orderItem);

        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
        );

        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {

        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();



        for(Order order : orders) {

            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for(OrderItem item : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponses.add(orderItemResponse);
            }
            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses


            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }
    public List<OrderResponse> getCustomerOrders(String email) {

        List<Order> orders = orderRepo.findByEmail(email);

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {

            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {

                itemResponses.add(
                        new OrderItemResponse(
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getTotalPrice()
                        )
                );
            }
            orderResponses.add(
                    new OrderResponse(
                            order.getOrderId(),
                            order.getCustomerName(),
                            order.getEmail(),
                            order.getStatus(),
                            order.getOrderDate(),
                            itemResponses
                    )
            );
        }

        return orderResponses;
    }
    public OrderResponse getCustomerOrder(String email, String orderId) {

        Order order = orderRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            itemResponses.add(
                    new OrderItemResponse(
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getTotalPrice()
                    )
            );
        }

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getStatus(),
                order.getOrderDate(),
                itemResponses
        );
    }
    public OrderResponse updateOrderStatus(String orderId, String status) {

        Order order = orderRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<String> validStatus = List.of(
                "PLACED",
                "CONFIRMED",
                "SHIPPED",
                "OUT_FOR_DELIVERY",
                "DELIVERED",
                "CANCELLED"
        );

        if (status == null || !validStatus.contains(status.toUpperCase())) {
            throw new RuntimeException("Invalid order status");
        }

        order.setStatus(status.toUpperCase());

        Order updatedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem item : updatedOrder.getOrderItems()) {

            itemResponses.add(
                    new OrderItemResponse(
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getTotalPrice()
                    )
            );
        }

        return new OrderResponse(
                updatedOrder.getOrderId(),
                updatedOrder.getCustomerName(),
                updatedOrder.getEmail(),
                updatedOrder.getStatus(),
                updatedOrder.getOrderDate(),
                itemResponses
        );
    }

    public String cancelOrder(String email, String orderId) {

        Order order = orderRepo.findByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (!order.getEmail().equals(email)) {
            throw new UnauthorizedException("Access denied");
        }

        switch (order.getStatus()) {

            case "PLACED":
            case "CONFIRMED":
                order.setStatus("CANCELLED");
                orderRepo.save(order);
                return "Order cancelled successfully";

            case "CANCELLED":
                throw new RuntimeException("Order is already cancelled");

            default:
                throw new RuntimeException(
                        "Order cannot be cancelled because it is " + order.getStatus()
                );
        }
    }
}
