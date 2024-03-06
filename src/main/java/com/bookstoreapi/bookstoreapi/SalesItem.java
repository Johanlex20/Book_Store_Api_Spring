package com.bookstoreapi.bookstoreapi;

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
    private Book item;

    @OneToOne
    private SalesOrder order;



}
