package Sathish292004.controller;

import Sathish292004.dto.request.OrderRequest;
import Sathish292004.dto.request.OrderStatusUpdateRequest;
import Sathish292004.dto.response.OrderResponse;
import Sathish292004.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Orders", description = "Order Management APIs")
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest orderRequest) {

        System.out.println("Inside placeOrder");

        OrderResponse orderResponse = orderService.placeOrder(orderRequest);

        return new ResponseEntity<>(
                orderResponse,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orderResponseList =
                orderService.getAllOrderResponses();

        return new ResponseEntity<>(
                orderResponseList,
                HttpStatus.OK
        );
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderStatusUpdateRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        request.status()
                )
        );
    }
}