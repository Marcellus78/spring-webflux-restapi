package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Vendor;
import com.marcellus.springwebfluxrestapi.repositories.VendorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.marcellus.springwebfluxrestapi.controllers.VendorController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> getAllVendorList() {

        return vendorRepository.findAll();
    }
    @GetMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {

        return vendorRepository.findById(id);
    }
}
