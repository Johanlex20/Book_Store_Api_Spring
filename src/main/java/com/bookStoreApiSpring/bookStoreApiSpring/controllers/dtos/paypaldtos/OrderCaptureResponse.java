package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.ordenCompra.PurchaseUnit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class OrderCaptureResponse {
    private String id;
    private String status;

    @JsonProperty("purchase_units")
    private List<PurchaseUnit> purchaseUnits;
}