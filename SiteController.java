package com.nsss.procurementmanagementsystembackend.controller;

import com.nsss.procurementmanagementsystembackend.model.Material;
import com.nsss.procurementmanagementsystembackend.model.Site;
import com.nsss.procurementmanagementsystembackend.model.Site;
import com.nsss.procurementmanagementsystembackend.repository.SiteRepository;
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
public class SiteController {
    @Autowired
    SiteRepository siteRepository;

    @GetMapping("/sites")
    public ResponseEntity<List<Site>> getAllSites(@RequestParam(required = false) String siteName) {
        try {
            List<Site> sites = new ArrayList<Site>();

            if (siteName == null)
                siteRepository.findAll().forEach(sites::add);
            else
                siteRepository.findAllByName(siteName).forEach(sites::add);

            if (sites.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(sites, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sites")
    public ResponseEntity<?> addSite(@Valid @RequestBody Site site){
        siteRepository.save(site);

        return ResponseEntity.ok(new MessageResponse("Site created successfully"));
    }
}
