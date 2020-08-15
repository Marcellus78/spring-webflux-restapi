package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Category;
import com.marcellus.springwebfluxrestapi.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.marcellus.springwebfluxrestapi.controllers.CategoryController.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;


    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);

        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepository.findAll())
                .willReturn(
                        Flux.just(
                                Category.builder().name("Cat1").build(),
                                Category.builder().name("Cat2").build())
                );

        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().name("Cat1").build()));

        webTestClient.get().uri(BASE_URL + "/1")
                .exchange()
                .expectBody(Category.class)
                .value(Category::getName, equalTo("Cat1"));

    }
}