package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Category;
import com.marcellus.springwebfluxrestapi.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.marcellus.springwebfluxrestapi.controllers.CategoryController.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    @Test
    void createNewCategoryStream() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().name("Some cat").build()));

        Mono<Category> savedCategory = Mono.just(Category.builder().name("Cat1").build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(savedCategory, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
    @Test
    void updateCategory() {

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> returnedCategory = Mono.just(Category.builder().name("Cat1").build());

        webTestClient.put()
                .uri(BASE_URL + "/1")
                .body(returnedCategory, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

    }
    @Test
    void patchCategoryTest() {

        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> returnedCategory = Mono.just(Category.builder().name("Cat1").build());

        webTestClient.patch()
                .uri(BASE_URL + "/1")
                .body(returnedCategory, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository).save(any());
    }
    @Test
    void patchCategoryNoChangeTest() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> returnedCategory = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri(BASE_URL + "/1")
                .body(returnedCategory, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository, never()).save(any());
    }
    @Test
    void deleteCategoryTest() {

        given(categoryRepository.deleteById(anyString()))
                .willReturn(Mono.empty());

        webTestClient.delete()
                .uri(BASE_URL + "/1")
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).deleteById(anyString());

    }
}