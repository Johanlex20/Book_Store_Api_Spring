package com.bookstoreapi.bookstoreapi.web;
import com.bookstoreapi.bookstoreapi.domain.SalesOrder;
import com.bookstoreapi.bookstoreapi.repository.SalesOrderRepository;
import com.bookstoreapi.bookstoreapi.service.PaypalService;
import com.bookstoreapi.bookstoreapi.service.SalesOrderService;
import com.bookstoreapi.bookstoreapi.web.dto.PaypalCaptureDto;
import com.bookstoreapi.bookstoreapi.web.dto.PaypalOrderDto;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderCaptureResponse;
import com.bookstoreapi.bookstoreapi.web.dto.paypal.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutController {

    private final SalesOrderService salesOrderService;
    private final PaypalService paypalService;
    private final SalesOrderRepository salesOrderRepository;

    @PostMapping("/checkout/paypal/create")
    public PaypalOrderDto createPaypalCheckout(@RequestParam String returnUrl, @RequestBody List<Integer> bookIds){
        SalesOrder salesOrder = salesOrderService.create(bookIds);
        OrderResponse orderResponse = paypalService.createOrder(salesOrder, returnUrl, returnUrl);

        String approveUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();

        return new PaypalOrderDto(approveUrl);
    }

    @PostMapping("/checkout/paypal/capture")
    public PaypalCaptureDto capturePaypalCheckout(@RequestParam String token){
        OrderCaptureResponse orderCaptureResponse = paypalService.captureOrder(token);

        boolean completed = orderCaptureResponse != null && orderCaptureResponse.getStatus().equals("COMPLETED");
        int orderId = 0;

        if (completed){
            orderId = Integer.parseInt(orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId());
            salesOrderService.updateForPaymentCompleted(orderId);
        }
        return new PaypalCaptureDto(completed, orderId);
    }
}
