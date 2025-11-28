package com.bookStoreApiSpring.bookStoreApiSpring.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id",referencedColumnName = "id")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private SalesOrder order;

    private Float price;

    @Column(name = "downs_ava")
    private Integer downloadAvailable;

}
