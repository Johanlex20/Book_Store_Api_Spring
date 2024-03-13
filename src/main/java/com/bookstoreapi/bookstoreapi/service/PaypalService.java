package com.bookstoreapi.bookstoreapi.service;

import com.bookstoreapi.bookstoreapi.web.dto.paypal.*;
import com.bookstoreapi.bookstoreapi.domain.Book;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class PaypalService {

    private final static String PAYPAL_API_BASE = "https://api-m.sandbox.paypal.com";
    private final static String PAYPAL_CLIENT_ID = "ATDaL4aLTNUscvJlFhb5nsqpN9ggvAeOQbnPcI5rBhbzVMDFCIymkHbq21Vm1tI481wy2wyW7DUc0FrA";
    private final static String PAYPAL_CLIENT_SECRET = "EKKbeRYDJsFTd9w46Ls75qcZptrZkT326Xjj_34YBw9cqkC-1azLgwgfYKG6DQtOQlasH4dVxVFoc19V";


    private String getAccessToken() {
        String url = String.format("%s/v1/oauth2/token", PAYPAL_API_BASE);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(PAYPAL_CLIENT_ID, PAYPAL_CLIENT_SECRET);

        // FormHttpMessageConverter is configured by default
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(url, entity, TokenResponse.class);

        return response.getBody().getAccessToken();
    }


    //crear orden de compra PAYPAL

    public OrderResponse createOrder(SalesOrder salesOrder, String returnUrl, String cancelUrl) {
        String url = String.format("%s/v2/checkout/orders", PAYPAL_API_BASE);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIntent(OrderRequest.Intent.CAPTURE);

        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setBrandName("TODO TIC");
        applicationContext.setReturnUrl(returnUrl);
        applicationContext.setCancelUrl(cancelUrl);

        orderRequest.setApplicationContext(applicationContext);

        // create single purchase unit of venta
        PurchaseUnit purchaseUnit = new PurchaseUnit();
        purchaseUnit.setReferenceId(salesOrder.getId().toString());

        Amount purchaseAmount = new Amount();
        purchaseAmount.setCurrencyCode(Amount.CurrencyCode.USD);
        purchaseAmount.setValue(salesOrder.getTotal().toString());

        Amount itemsAmount = new Amount();
        itemsAmount.setCurrencyCode(Amount.CurrencyCode.USD);
        itemsAmount.setValue(salesOrder.getTotal().toString());

        purchaseAmount.setBreakdown(new Amount.Breakdown(itemsAmount));

        purchaseUnit.setAmount(purchaseAmount);
        purchaseUnit.setItems(new ArrayList<>());

        // add items of single purchase unit
        salesOrder.getItems().forEach(salesItem -> {
            Book book = salesItem.getBook();

            OrderItem orderItem = new OrderItem();
            orderItem.setName(book.getTitle());
            orderItem.setSku(book.getId().toString());
            orderItem.setQuantity("1");

            Amount unitAmount = new Amount();
            unitAmount.setCurrencyCode(Amount.CurrencyCode.USD);
            unitAmount.setValue(salesItem.getPrice().toString());

            orderItem.setUnitAmount(unitAmount);
            purchaseUnit.getItems().add(orderItem);
        });
        // set single purchase unit to order request
        orderRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));

        // get access token
        String accessToken = getAccessToken();

        // create http request
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<OrderRequest> entity = new HttpEntity<>(orderRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OrderResponse> response = restTemplate.postForEntity(url, entity, OrderResponse.class);

        return response.getBody();
    }

    //METODO PARA CAPTURAR UNA ORDEN DE PAGO

    public OrderCaptureResponse captureOrder(String orderId) {
        String url = String.format("%s/v2/checkout/orders/%s/capture", PAYPAL_API_BASE, orderId);

        // get access token
        String accessToken = getAccessToken();

        // create http request
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            HttpEntity<Object> entity = new HttpEntity<>(null, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OrderCaptureResponse> response = restTemplate.postForEntity(url, entity, OrderCaptureResponse.class);

            return response.getBody();
        }catch (HttpClientErrorException e){
            return null;
        }


    }




}
