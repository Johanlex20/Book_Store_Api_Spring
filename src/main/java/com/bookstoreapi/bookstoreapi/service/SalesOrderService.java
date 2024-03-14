package com.bookstoreapi.bookstoreapi.service;

import com.bookstoreapi.bookstoreapi.domain.SalesOrder;

import java.util.List;

public interface SalesOrderService {

    SalesOrder create(List<Integer> bookIds);

    SalesOrder updateForPaymentCompleted(Integer id);

}
