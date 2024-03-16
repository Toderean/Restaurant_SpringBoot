package com.example.tema1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "OrderDetails")
public class OrderDetails {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailsId;

    @OneToOne
    @JoinColumn(name = "orderId")
    @Setter
    private Order order;

    @ManyToOne
    @JoinColumn(name = "mealId")
    @Setter
    private Meals meal;

    @Setter
    @Column(name = "quantity")
    private Integer quantity;
}
