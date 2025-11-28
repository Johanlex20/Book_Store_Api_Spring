package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.ordenCompra;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Data
public class Amount {
    @JsonProperty("currency_code")
    private CurrencyCode currencyCode;

    private String value;
    private Breakdown breakdown;

    public enum CurrencyCode {
        USD
    }

    @Data
    @RequiredArgsConstructor
    public static class Breakdown {
        @NonNull
        @JsonProperty("item_total")
        private Amount itemTotal;
    }
}