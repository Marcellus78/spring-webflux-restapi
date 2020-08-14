package com.marcellus.springwebfluxrestapi.repositories;

import com.marcellus.springwebfluxrestapi.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
