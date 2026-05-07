package com.example.distributed_database_project_server.order;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.OrderStatus;
import com.example.distributed_database_project_server.domain.constant.PaymentMethod;
import com.example.distributed_database_project_server.domain.entity.OrderEntity;
import com.example.distributed_database_project_server.domain.entity.OrderItemEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class OrderResponse {

    private UUID orderId;
    private UUID customerId;
    private UUID addressId;
    private OffsetDateTime orderDate;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String shipper;
    private String awb;
    private List<OrderItemResponse> orderItems;

    private OrderResponse(
            UUID orderId,
            UUID customerId,
            UUID addressId,
            OffsetDateTime orderDate,
            OrderStatus orderStatus,
            PaymentMethod paymentMethod,
            String shipper,
            String awb,
            List<OrderItemResponse> orderItems
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.addressId = addressId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shipper = shipper;
        this.awb = awb;
        this.orderItems = orderItems;
    }

    public static OrderResponse fromEntity(OrderEntity order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getDelivery().getAddress().getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getDelivery().getShippedBy(),
                order.getDelivery().getAwb(),
                order.getOrderItems().stream().map(OrderItemResponse::fromEntity).toList()
        );
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public OffsetDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(OffsetDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class OrderItemResponse {

        private UUID orderItemId;
        private UUID productId;
        private UUID priceId;
        private Integer quantity;

        private OrderItemResponse(UUID orderItemId, UUID productId, UUID priceId, Integer quantity) {
            this.orderItemId = orderItemId;
            this.productId = productId;
            this.priceId = priceId;
            this.quantity = quantity;
        }

        public static OrderItemResponse fromEntity(OrderItemEntity orderItem) {
            return new OrderItemResponse(
                    orderItem.getId(),
                    orderItem.getProduct().getId(),
                    orderItem.getPrice().getId(),
                    orderItem.getQuantity()
            );
        }

        public UUID getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(UUID orderItemId) {
            this.orderItemId = orderItemId;
        }

        public UUID getProductId() {
            return productId;
        }

        public void setProductId(UUID productId) {
            this.productId = productId;
        }

        public UUID getPriceId() {
            return priceId;
        }

        public void setPriceId(UUID priceId) {
            this.priceId = priceId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
