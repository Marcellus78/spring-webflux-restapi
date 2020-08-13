package com.marcellus.springwebfluxrestapi.repositories;

import com.marcellus.springwebfluxrestapi.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class CategoryRepositoryTestIT {

    @Autowired
    CategoryRepository categoryRepository;

    private final Category category1 = new Category(null, "first category");
    private final Category category2 = new Category(null, "second category");

    @Test
    public void findAll() {

        Flux<Category> categoriesFlux = categoryRepository.deleteAll()
                .thenMany(Flux.just(category1, category2))
                .flatMap(category -> categoryRepository.save(category))
                .thenMany(categoryRepository.findAll());


        StepVerifier
                .create(categoriesFlux)
                .expectNext(this.category1, this.category2)
                .expectComplete()
                .verify();
    }
    @Test
    public void findFirstByName() {
        categoryRepository.save(category1).block();

        Mono<Category> monoCategory = categoryRepository.findFirstByName("first category");

        StepVerifier
                .create(monoCategory)
                .assertNext(category -> {
                    assertEquals("first category", category.getName());
                    assertNotNull(category.getId());
                })
                .expectComplete()
                .verify();
    }
    @Test
    public void saveCategory() {

        Mono<Category> categoryMono = categoryRepository.save(category1);

        StepVerifier
                .create(categoryMono)
                .assertNext(category -> assertNotNull(category.getId()))
                .expectComplete()
                .verify();
    }
}