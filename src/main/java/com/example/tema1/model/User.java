package com.example.tema1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Setter
    @Column(name = "Name")
    private String name;
    @Setter
    @Column(name = "UserType")
    private Integer userType;
    @Setter
    @Column(name = "UserName")
    private String username;
    @Setter
    @Column(name = "Password")
    private String password;
}
