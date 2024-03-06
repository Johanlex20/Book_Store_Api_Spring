package com.bookstoreapi.bookstoreapi.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float price;

    @Column(name = "downs_ava")
    private Integer downloadAvailable;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private SalesOrder order;



}
