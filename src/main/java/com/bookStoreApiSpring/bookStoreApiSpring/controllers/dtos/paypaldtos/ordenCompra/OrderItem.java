package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.ordenCompra;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderItem {
    private String name;
    private String sku;
    private String quantity;

    @JsonProperty("unit_amount")
    private Amount unitAmount;
}