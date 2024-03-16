package com.example.tema1.repositories;

import com.example.tema1.model.Meals;
import org.springframework.data.repository.CrudRepository;

public interface MealsRepository extends CrudRepository<Meals,Integer> {
}
