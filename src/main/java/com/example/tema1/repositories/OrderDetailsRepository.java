package com.example.tema1.repositories;

import com.example.tema1.model.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails,Integer> {
}
