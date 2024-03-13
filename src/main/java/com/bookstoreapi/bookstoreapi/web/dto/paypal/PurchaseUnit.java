package com.bookstoreapi.bookstoreapi.web.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseUnit {

    // aqui enviaremos el ID de un objetobo SalesOrder que representa una venta persisitida en la base de datos,
    // servira para saber a que venta corresponde el pago de PAYPAL

    @JsonProperty("reference_id")
    private String referenceId;

    private Amount amount;
    private List<OrderItem> items;
}
