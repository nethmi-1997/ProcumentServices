package com.nsss.procurementmanagementsystembackend.controller;

import com.nsss.procurementmanagementsystembackend.model.Supplier;
import com.nsss.procurementmanagementsystembackend.repository.SupplierRepository;
import com.nsss.procurementmanagementsystembackend.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/access")
public class SupplierController {
    @Autowired
    SupplierRepository supplierRepository;

    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getAllSuppliers(@RequestParam(required = false) String supplierName) {
        try {
            List<Supplier> suppliers = new ArrayList<Supplier>();

            if (supplierName == null)
                supplierRepository.findAll().forEach(suppliers::add);
            else
                supplierRepository.findAllByName(supplierName).forEach(suppliers::add);

            if (suppliers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(suppliers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/suppliers")
    public ResponseEntity<?> addSupplier(@Valid @RequestBody Supplier supplier){
        supplierRepository.save(supplier);

        return ResponseEntity.ok(new MessageResponse("Supplier created successfully"));
    }
}
