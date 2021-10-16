package com.nsss.procurementmanagementsystembackend.controller;

import com.nsss.procurementmanagementsystembackend.model.Material;
import com.nsss.procurementmanagementsystembackend.repository.MaterialRepository;
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
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;

    @GetMapping("/materials")
    public ResponseEntity<List<Material>> getAllMaterials(@RequestParam(required = false) String materialName) {
        try {
            List<Material> materials = new ArrayList<Material>();

            if (materialName == null)
                materialRepository.findAll().forEach(materials::add);
            else
                materialRepository.findAllByName(materialName).forEach(materials::add);

            if (materials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(materials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/materials")
    public ResponseEntity<?> addMaterial(@Valid @RequestBody Material material){
        materialRepository.save(material);

        return ResponseEntity.ok(new MessageResponse("Material created successfully"));
    }
}
