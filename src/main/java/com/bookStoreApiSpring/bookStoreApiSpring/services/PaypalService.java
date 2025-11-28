package com.bookStoreApiSpring.bookStoreApiSpring.services;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.OrderCaptureResponse;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.OrderResponse;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.ordenCompra.*;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.paypal.api-base}")
    private String paypalApiBase;

    @Value("${app.paypal.client.id}")
    private String paypalClientId;
    @Value("${app.paypal.client.secret}")
    private String paypalClientSecret;

    private String getAccessToken() {
        String url = String.format("%s/v1/oauth2/token", paypalApiBase);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(paypalClientId, paypalClientSecret);

        // FormHttpMessageConverter is configured by default
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);
        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(url, entity, TokenResponse.class);

        return response.getBody().getAccessToken();
    }

    public OrderResponse createOrder(SalesOrder salesOrder, String returnUrl, String cancelUrl) {
        String url = String.format("%s/v2/checkout/orders", paypalApiBase);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIntent(OrderRequest.Intent.CAPTURE);

        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.setBrandName("Good Company");
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
        salesOrder.getItems().forEach(itemVenta -> {
            Book book = itemVenta.getBook();

            OrderItem orderItem = new OrderItem();
            orderItem.setName(book.getTitle());
            orderItem.setSku(book.getId().toString());
            orderItem.setQuantity("1");

            Amount unitAmount = new Amount();
            unitAmount.setCurrencyCode(Amount.CurrencyCode.USD);
            unitAmount.setValue(itemVenta.getPrice().toString());

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

    public OrderCaptureResponse captureOrder(String orderId) {
        String url = String.format("%s/v2/checkout/orders/%s/capture", paypalApiBase, orderId);

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
