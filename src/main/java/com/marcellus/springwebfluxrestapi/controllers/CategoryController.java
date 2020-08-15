package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Category;
import com.marcellus.springwebfluxrestapi.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public Flux<Category> list() {

        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> getById(@PathVariable String id) {

        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Void> createNewCategory(@RequestBody Publisher<Category> categoryStream) {

        return categoryRepository.saveAll(categoryStream).then();
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> updateCategory(@PathVariable String id,
                                         @RequestBody Category category) {

        return categoryRepository.findById(id)
                .flatMap(returnedCategory -> {
                    category.setId(returnedCategory.getId());
                    return categoryRepository.save(category);
                }).switchIfEmpty(Mono.error(new Exception("Category not found")));
    }
}
