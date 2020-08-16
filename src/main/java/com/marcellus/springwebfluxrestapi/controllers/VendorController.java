package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Vendor;
import com.marcellus.springwebfluxrestapi.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {

        return vendorRepository.saveAll(vendorStream).then();
    }
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<Vendor> updateVendor(@PathVariable String id,
                                     @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(returnedVendor -> {
                    vendor.setId(returnedVendor.getId());
                    return vendorRepository.save(vendor);
                }).switchIfEmpty(Mono.error(new Exception("Vendor not found")));
    }
    @PatchMapping("/{id}")
    public Mono<Vendor> patchVendor(@PathVariable String id,
                                    @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(returnedVendor -> {
                    Mono<Vendor> savedVendor = Mono.just(returnedVendor);
                    if(vendor.getFirstName() != null) {
                        returnedVendor.setFirstName(vendor.getFirstName());
                        savedVendor = vendorRepository.save(returnedVendor);
                    }
                    if(vendor.getLastName() != null) {
                        returnedVendor.setLastName(vendor.getLastName());
                        savedVendor = vendorRepository.save(returnedVendor);
                    }
                    return savedVendor;
                }).switchIfEmpty(Mono.error(new Exception("Vendor not found")));

    }
    @DeleteMapping("/{id}")
    public Mono<Void> deleteVendor(@PathVariable String id) {

        return vendorRepository.deleteById(id).then();
    }

}
