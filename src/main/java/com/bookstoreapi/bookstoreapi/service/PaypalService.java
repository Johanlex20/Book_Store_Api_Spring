package com.bookstoreapi.bookstoreapi.service;

import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderCaptureResponse;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderResponse;

public interface PaypalService {

    OrderResponse createOrder(SalesOrder salesOrder, String returnUrl, String cancelUrl);

    OrderCaptureResponse captureOrder(String orderId);

}
