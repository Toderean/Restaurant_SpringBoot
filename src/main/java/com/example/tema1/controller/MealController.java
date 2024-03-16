package com.example.tema1.controller;

import com.example.tema1.model.Meals;
import com.example.tema1.model.OrderDetails;
import com.example.tema1.repositories.MealsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meals")
public class MealController {
    @Autowired
    private MealsRepository mealsRepository;
    @GetMapping("/find/{id}")
    private Optional<Meals> findMeals (@PathVariable Integer id){
        Optional<Meals> meals = mealsRepository.findById(id);
        return meals;
    }

    @PostMapping("/create")
    private ResponseEntity<Meals> createMeals(@RequestBody Meals meals){
        List<OrderDetails> orderDetailsList = null;
        Meals newMeals = new Meals(meals.getId(),
                                   meals.getName(),
                                   meals.getPrice(),
                                    meals.getStock(),
                                    orderDetailsList);
        mealsRepository.save(newMeals);
        return new ResponseEntity<>(newMeals, HttpStatus.CREATED);
    }

    @PostMapping("/update/{id}")
    private ResponseEntity<Meals> updateMeals(@PathVariable Integer id,
                                              @RequestBody Meals meals){
        Meals updatedMeals = mealsRepository.findById(id).orElse(null);
        if(updatedMeals != null){
            updatedMeals.setName(meals.getName());
            updatedMeals.setStock(meals.getStock());
            updatedMeals.setPrice(meals.getPrice());
            mealsRepository.save(updatedMeals);
            return new ResponseEntity<>(updatedMeals,HttpStatus.OK);
        }
        return new ResponseEntity<>(updatedMeals,HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteUser(@PathVariable Integer id){
        if(mealsRepository.findById(id).isPresent()){
            mealsRepository.deleteById(id);
            return new ResponseEntity<>("Meals Deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Meals deleted",HttpStatus.CONFLICT);
    }

}
