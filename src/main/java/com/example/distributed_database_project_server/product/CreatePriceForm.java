package com.example.distributed_database_project_server.product;

import java.math.BigDecimal;

import com.example.distributed_database_project_server.domain.constant.Currency;
import com.example.distributed_database_project_server.domain.constant.Market;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

class CreatePriceForm {

    @NotNull(message = "Market should not be null")
    private Market market;

    @NotNull(message = "Price should not be null")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotNull(message = "Currency should not be null")
    private Currency currency;

    public Market getMarket() {
        return this.market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
