package com.example.distributed_database_project_server.order;

import java.util.List;
import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

class CreateOrderForm {

    @NotNull(message = "Customer ID should not be null")
    private UUID customerId;

    @NotNull(message = "Address ID should not be null")
    private UUID addressId;

    @NotNull(message = "Payment method should not be null")
    private PaymentMethod paymentMethod;

    @Valid
    @NotNull(message = "Order items should not be null")
    @NotEmpty(message = "Order items should not be empty")
    private List<FormOrderItem> orderItems;

    static class FormOrderItem {

        @NotNull(message = "Product ID should not be null")
        private UUID productId;

        @NotNull(message = "Price ID should not be null")
        private UUID priceId;

        @NotNull(message = "Quantity should not be null")
        @Positive(message = "Quantity should be greater than 0")
        private Integer quantity;

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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<FormOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<FormOrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
