package com.example.tema1.controller;

import com.example.tema1.model.Meals;
import com.example.tema1.model.OrderDetails;
import com.example.tema1.repositories.MealsRepository;
import com.example.tema1.repositories.OrderDetailsRepository;
import com.example.tema1.repositories.OrderRepository;
import com.example.tema1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orderDetails")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MealsRepository mealsRepository;

    @GetMapping("/find/{id}")
    private Optional<OrderDetails> findOrderDetails (@PathVariable Integer id){
        Optional<OrderDetails> order = orderDetailsRepository.findById(id);
        return order;
    }

    @PostMapping("/update")
    private ResponseEntity<OrderDetails> updateOrderDetails(@RequestParam Integer id,
                                                            @RequestParam String field,
                                                            @RequestParam String val){
        Optional<OrderDetails> updatedOrderDetails = orderDetailsRepository.findById(id);
        if (updatedOrderDetails.isPresent()){
            switch (field){
                case "order": updatedOrderDetails.get().setOrder(orderRepository.findById(Integer.parseInt(val)).orElse(null));
                    break;
                case "meal": updatedOrderDetails.get().setMeal(mealsRepository.findById(Integer.parseInt(val)).orElse(null));
                    break;
                case "quantity": updatedOrderDetails.get().setQuantity(Integer.parseInt(val));
                    break;
                default: return new ResponseEntity<>(null,HttpStatus.CONFLICT);
            }
            orderDetailsRepository.save(updatedOrderDetails.get());
            return new ResponseEntity<>(updatedOrderDetails.get(),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }

    @PostMapping("/addMeal")
    private ResponseEntity<Meals> addMealToOrder(@RequestParam Integer idOrder,
                                                 @RequestParam Integer idMeal,
                                                 @RequestParam Integer amount){
        Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(idOrder);
        Optional<Meals> meal = mealsRepository.findById(idMeal);
        if (orderDetails.isPresent() & meal.isPresent()){
            if (amount==null){
                meal.get().setStock(meal.get().getStock()-1);
                orderDetails.get().setQuantity(orderDetails.get().getQuantity()+1);
                orderDetails.get().getOrder().setTotalCost(orderDetails.get().getOrder().getTotalCost() + meal.get().getPrice());
                orderDetails.get().setMeal(meal.get());
                orderDetails.get().setOrder(orderDetails.get().getOrder());
                mealsRepository.save(meal.get());
                orderDetailsRepository.save(orderDetails.get());
                return new ResponseEntity<>(meal.get(),HttpStatus.CONFLICT);
            }else {
                meal.get().setStock(meal.get().getStock()-amount);
                orderDetails.get().setQuantity(orderDetails.get().getQuantity()+amount);
                orderDetails.get().getOrder().setTotalCost(orderDetails.get().getOrder().getTotalCost() + meal.get().getPrice()*amount);
                orderDetails.get().setMeal(meal.get());
                orderDetails.get().setOrder(orderDetails.get().getOrder());
                mealsRepository.save(meal.get());
                orderDetailsRepository.save(orderDetails.get());
                return new ResponseEntity<>(meal.get(),HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteUser(@PathVariable Integer id){
        if(orderDetailsRepository.findById(id).isPresent()){
            orderDetailsRepository.deleteById(id);
            return new ResponseEntity<>("OrderDetails Deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("OrderDetails deleted",HttpStatus.CONFLICT);
    }
}
