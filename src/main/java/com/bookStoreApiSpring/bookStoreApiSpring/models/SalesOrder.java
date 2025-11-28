package com.bookStoreApiSpring.bookStoreApiSpring.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "sales_order")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<SalesItem> items;

    public enum PaymentStatus{
        PAID,
        PENDING
    }

}
