package com.marcellus.springwebfluxrestapi.controllers;

import com.marcellus.springwebfluxrestapi.domain.Vendor;
import com.marcellus.springwebfluxrestapi.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.marcellus.springwebfluxrestapi.controllers.VendorController.BASE_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class VendorControllerTest {

    VendorRepository vendorRepository;
    VendorController vendorController;
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }
    @Test
    public void listAll() {
        given(vendorRepository.findAll()).willReturn(
                Flux.just(
                        Vendor.builder().firstName("Joe").lastName("Black").build(),
                        Vendor.builder().firstName("Michael").lastName("Weston").build()
                )
        );

        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }
    @Test
    public void getById() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build()));

        webTestClient.get().uri(BASE_URL + "/1")
                .exchange()
                .expectBody(Vendor.class)
                .value(vendor -> vendor.getFirstName(), equalTo("Joe"));
    }
    @Test
    public void createVendorTest() {

        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> savedVendor =
                Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(savedVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }
    @Test
    public void updateVendorTest() {

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> savedVendor =
                Mono.just(Vendor.builder().firstName("Joe").lastName("Doe").build());

        webTestClient.put()
                .uri(BASE_URL + "/1")
                .body(savedVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

    }
    @Test
    public void patchVendorByFirstName() {

        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> patchedVendor = Mono.just(Vendor.builder().firstName("Joe").build());

        webTestClient.patch()
                .uri(BASE_URL + "/1")
                .body(patchedVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }
    @Test
    public void patchVendorByLastName() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> patchedVendor = Mono.just(Vendor.builder().lastName("Doe").build());

        webTestClient.patch()
                .uri(BASE_URL + "/1")
                .body(patchedVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }
    @Test
    public void patchVendorNoChange() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> patchedVendor = Mono.just(Vendor.builder().build());

        webTestClient.patch()
                .uri(BASE_URL + "/1")
                .body(patchedVendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository,never()).save(any());
    }
    @Test
    public void deleteVendor() {

        given(vendorRepository.deleteById(anyString()))
                .willReturn(Mono.empty());

        webTestClient.delete()
                .uri(BASE_URL + "/1")
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository).deleteById(anyString());
    }

}