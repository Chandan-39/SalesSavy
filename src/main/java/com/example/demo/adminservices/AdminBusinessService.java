package com.example.demo.adminservices;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminBusinessService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public AdminBusinessService(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
        validateMonthYear(month, year);

        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByMonthAndYear(month, year);
        return calculateBusinessReport(successfulOrders);
    }

    public Map<String, Object> calculateDailyBusiness(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByDate(date);
        return calculateBusinessReport(successfulOrders);
    }

    public Map<String, Object> calculateYearlyBusiness(int year) {
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByYear(year);
        return calculateBusinessReport(successfulOrders);
    }

    public Map<String, Object> calculateOverallBusiness() {
        BigDecimal totalBusinessAmount = orderRepository.calculateOverallBusiness();
        List<Order> successfulOrders = orderRepository.findAllByStatus(OrderStatus.SUCCESS);
        Map<String, Integer> categorySales = aggregateCategorySales(successfulOrders);

        Map<String, Object> response = new HashMap<>();
        response.put("totalBusiness", totalBusinessAmount.doubleValue());
        response.put("categorySales", categorySales);
        return response;
    }

    // --- Helper methods ---

    private void validateMonthYear(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
    }

    private Map<String, Object> calculateBusinessReport(List<Order> successfulOrders) {
        double totalBusiness = 0.0;
        Map<String, Integer> categorySales = new HashMap<>();

        for (Order order : successfulOrders) {
            totalBusiness += order.getTotalAmount().doubleValue();

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
            for (OrderItem item : orderItems) {
                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
                if (categoryName == null) {
                    categoryName = "Unknown";
                }
                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
            }
        }

        Map<String, Object> report = new HashMap<>();
        report.put("totalBusiness", totalBusiness);
        report.put("categorySales", categorySales);
        return report;
    }

    private Map<String, Integer> aggregateCategorySales(List<Order> orders) {
        Map<String, Integer> categorySales = new HashMap<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
            for (OrderItem item : orderItems) {
                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
                if (categoryName == null) {
                    categoryName = "Unknown";
                }
                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
            }
        }
        return categorySales;
    }
}






