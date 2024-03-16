package com.example.tema1.controller;

import com.example.tema1.model.OrderDetails;
import com.example.tema1.model.User;
import com.example.tema1.repositories.OrderDetailsRepository;
import com.example.tema1.repositories.OrderRepository;
import com.example.tema1.model.Order;
import com.example.tema1.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;



    @GetMapping("/find/{id}")
    private Optional<Order> findOrder (@PathVariable Integer id){
        Optional<Order> order = orderRepository.findById(id);
        return order;
    }

    ///yyyy-mm-dd
    @PostMapping("/create")
    private ResponseEntity<Order> createOrder(@RequestParam Date placementDate,
                                              @RequestParam Float totalCost){
        HttpSession session= getSession();
        String username = (String) session.getAttribute("username");
        User user = userRepository.findUserByUsername(username).orElse(null);

        Order newOrder = new Order();
        newOrder.setId((int) (orderRepository.count()+1));
        newOrder.setUser(user);
        newOrder.setPlacementDate(placementDate);
        newOrder.setTotalCost(totalCost);
        newOrder.setOrderStatus("New Command");

        orderRepository.save(newOrder);

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setQuantity(0); // Assuming initial quantity is 0
        orderDetails.setMeal(null);
        orderDetails.setOrder(newOrder);

        orderDetailsRepository.save(orderDetails);
        orderRepository.save(newOrder);

        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);

    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true for creating a new session if not exists
    }
    @PostMapping("/update/{id}")
    private ResponseEntity<Order> updateOrder(@PathVariable Integer id,
                                              @RequestParam String field,
                                              @RequestParam String val){

        Optional<Order> updatedOrder = orderRepository.findById(id);
        if (updatedOrder.isPresent()){
         switch (field){
             case "quantity":
                 updatedOrder.get().getOrderDetails().setQuantity(Integer.parseInt(val));
                break;
             case "user":
                 updatedOrder.get().setUser(userRepository.findUserById(Integer.parseInt(val)).orElse(null));
                 break;
             case "date":
                 updatedOrder.get().setPlacementDate(Date.valueOf(val));
                 break;
             case "cost":
                 updatedOrder.get().setTotalCost(Float.parseFloat(val));
                 break;
             case "status":
                 updatedOrder.get().setOrderStatus(val);
                 break;
             default:
                 return new ResponseEntity<>(null,HttpStatus.CONFLICT);
         }
         orderRepository.save(updatedOrder.get());
         return new ResponseEntity<>(updatedOrder.get(),HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(null,HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteUser(@PathVariable Integer id){
        if(orderRepository.findById(id).isPresent()){
            orderRepository.deleteById(id);
            return new ResponseEntity<>("Order Deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("Order deleted",HttpStatus.CONFLICT);
    }
}
