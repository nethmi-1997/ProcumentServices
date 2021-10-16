package com.nsss.procurementmanagementsystembackend.controller;

import com.nsss.procurementmanagementsystembackend.model.Material;
import com.nsss.procurementmanagementsystembackend.model.Order;
import com.nsss.procurementmanagementsystembackend.model.OrderItem;
import com.nsss.procurementmanagementsystembackend.model.Site;
import com.nsss.procurementmanagementsystembackend.repository.MaterialRepository;
import com.nsss.procurementmanagementsystembackend.repository.OrderRepository;
import com.nsss.procurementmanagementsystembackend.repository.SiteRepository;
import com.nsss.procurementmanagementsystembackend.repository.SupplierRepository;
import com.nsss.procurementmanagementsystembackend.request.OrderRequest;
import com.nsss.procurementmanagementsystembackend.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/access")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SiteRepository siteRepository;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = new ArrayList<Order>();

            orderRepository.findAll().forEach(orders::add);

            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/pending")
    public ResponseEntity<List<Order>> getAllPendingOrders() {
        try {
            List<Order> orders = new ArrayList<>(orderRepository.findAllByStatusEquals("Pending"));

            if (orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderRequest orderRequest){
        Material material = materialRepository.findMaterialByName(orderRequest.getItem()).get();
        double quantity = orderRequest.getQuantity();

        OrderItem orderItem = new OrderItem(material, quantity);
        double total = material.getPrice()*quantity;

        Order order = new Order(
                orderItem,
                supplierRepository.findSupplierByName(orderRequest.getSupplier()).get(),
                "Pending",
                total,
                orderRequest.getComment(),
                new Date(),
                orderRequest.getDeliveryDate(),
                siteRepository.findSiteByName(orderRequest.getSite()).get()
        );

        orderRepository.save(order);

        return ResponseEntity.ok(new MessageResponse("Order created successfully"));
    }

    @PutMapping("/orders/approved/{id}")
    public ResponseEntity<Order> updateOrderApprovedStatus(@PathVariable("id") String id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if(orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus("Approved");

            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/orders/hold/{id}")
    public ResponseEntity<Order> updateOrderHoldStatus(@PathVariable("id") String id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if(orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus("Hold");

            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/orders/declined/{id}")
    public ResponseEntity<Order> updateOrderDeclinedStatus(@PathVariable("id") String id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if(orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus("Declined");

            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/orders/referred/{id}")
    public ResponseEntity<Order> updateOrderReferredStatus(@PathVariable("id") String id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if(orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus("Referred");

            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/orders/delivered/{id}")
    public ResponseEntity<Order> updateOrderDeliveredStatus(@PathVariable("id") String id) {
        Optional<Order> orderData = orderRepository.findById(id);

        if(orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus("Delivered");

            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/orders/{id}")
//    public ResponseEntity<Order> updateOrderDeliveredStatus(@PathVariable("id") String id) {
//        Optional<Order> orderData = orderRepository.findById(id);
//
//        if(orderData.isPresent()) {
//            Order _order = orderData.get();
//            _order.setStatus("Delivered");
//
//            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
