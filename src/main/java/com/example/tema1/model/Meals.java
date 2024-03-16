package com.example.tema1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Meals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Meals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MealId")
    private Integer id;

    @Setter
    @Column(name = "Name")
    private String name;

    @Setter
    @Column(name = "Price")
    private Float price;

    @Setter
    @Column(name = "Stock")
    private Integer stock;

    @OneToMany(mappedBy = "meal")
    private List<OrderDetails> orderDetails;
}
