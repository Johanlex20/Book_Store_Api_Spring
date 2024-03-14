package com.bookstoreapi.bookstoreapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PaypalCaptureDto {

        private boolean completed;
        private Integer orderId;
}
