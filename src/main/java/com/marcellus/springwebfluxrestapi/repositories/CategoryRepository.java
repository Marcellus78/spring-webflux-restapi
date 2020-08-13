package com.marcellus.springwebfluxrestapi.repositories;

import com.marcellus.springwebfluxrestapi.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
