package com.example.tema1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "Orders")
public class Order {
    @Id
    @Setter
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId")
    @Setter
    private User user;

    @Setter
    @Getter
    @OneToOne(mappedBy = "order")
    private OrderDetails orderDetails;

    @Setter
    @Getter
    @Column(name = "placementDate")
    private Date placementDate;

    @Setter
    @Column(name = "totalCost")
    private Float totalCost;

    @Setter
    @Column(name = "orderStatus")
    private String orderStatus;
}
