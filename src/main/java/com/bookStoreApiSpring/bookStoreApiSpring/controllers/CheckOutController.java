package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.OrderCaptureResponse;
import com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos.paypaldtos.OrderResponse;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iSalesOrderRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.services.PaypalService;
import com.bookStoreApiSpring.bookStoreApiSpring.services.SalesOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/checkout")
@AllArgsConstructor
public class CheckOutController {

    private final SalesOrderService salesOrderService;
    private final PaypalService paypalService;
    private final iSalesOrderRepository salesOrderRepository;


    @PostMapping(value = "/paypal/create")
    public Map<String,String> createPaypalCheckOut(@RequestParam String returnUrl, @RequestBody List<Integer>bookIds){
        SalesOrder salesOrder = salesOrderService.create(bookIds);
        OrderResponse orderResponse = paypalService.createOrder(salesOrder,returnUrl,returnUrl);

        String approveUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst().orElseThrow(ResourceNotFoundException::new)
                .getHref();
        return Map.of("approveUrl",approveUrl);
    }

    @PostMapping("/paypal/capture")
    public Map<String,Object> capturePaypalCheckOut(@RequestParam String token){
        OrderCaptureResponse orderCaptureResponse = paypalService.captureOrder(token);

        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");
        int orderId = 0;

        if (completed){
            orderId = Integer.parseInt(orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId());

            SalesOrder salesOrder = salesOrderRepository
                    .findById(orderId)
                    .orElseThrow(RuntimeException::new);

            salesOrder.setPaymentStatus(SalesOrder.PaymentStatus.PAID);
            salesOrderRepository.save(salesOrder);

        }
        return Map.of("completed", completed, "orderId",orderId);

    }

}
