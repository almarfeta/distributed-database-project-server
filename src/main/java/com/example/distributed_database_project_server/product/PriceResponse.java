package com.example.distributed_database_project_server.product;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.distributed_database_project_server.domain.constant.Currency;
import com.example.distributed_database_project_server.domain.constant.Market;
import com.example.distributed_database_project_server.domain.constant.PriceStatus;
import com.example.distributed_database_project_server.domain.entity.PriceEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class PriceResponse {

    private UUID productId;
    private UUID priceId;
    private Market market;
    private BigDecimal price;
    private Currency currency;
    private PriceStatus status;

    private PriceResponse(UUID productId, UUID priceId, Market market, BigDecimal price, Currency currency, PriceStatus status) {
        this.productId = productId;
        this.priceId = priceId;
        this.market = market;
        this.price = price;
        this.currency = currency;
        this.status = status;
    }

    public static PriceResponse fromEntity(PriceEntity price) {
        return new PriceResponse(
                price.getProduct().getId(),
                price.getId(),
                price.getMarket(),
                price.getValue(),
                price.getCurrency(),
                price.getStatus()
        );
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

    public PriceStatus getStatus() {
        return status;
    }

    public void setStatus(PriceStatus status) {
        this.status = status;
    }
}
