package com.bookstoreapi.bookstoreapi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data

public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //varios pedidos pueden ser de un usuario
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    private Float total;

    @Column(name = "paymentstatus")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    //relacion inversa asia SalesItem order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<SalesItem>items;

    public enum PaymentStatus{
        PENDING,
        PAID
    }

}
