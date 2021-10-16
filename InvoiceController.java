package com.nsss.procurementmanagementsystembackend.controller;

import com.nsss.procurementmanagementsystembackend.model.Invoice;
import com.nsss.procurementmanagementsystembackend.model.Order;
import com.nsss.procurementmanagementsystembackend.model.Site;
import com.nsss.procurementmanagementsystembackend.repository.InvoiceRepository;
import com.nsss.procurementmanagementsystembackend.repository.OrderRepository;
import com.nsss.procurementmanagementsystembackend.request.InvoiceRequest;
import com.nsss.procurementmanagementsystembackend.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/access")
public class InvoiceController {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = new ArrayList<Invoice>();

            invoiceRepository.findAll().forEach(invoices::add);

            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/invoices")
    public ResponseEntity<?> addInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest){
        Order order = orderRepository.findById(invoiceRequest.getOrderId()).get();

        Invoice invoice = new Invoice(
                order,
                new Date(),
                invoiceRequest.getUser()
        );

        invoiceRepository.save(invoice);

        return ResponseEntity.ok(new MessageResponse("Invoice created successfully"));
    }
}
