package com.marcellus.springwebfluxrestapi.bootstrap;

import com.marcellus.springwebfluxrestapi.domain.Category;
import com.marcellus.springwebfluxrestapi.domain.Vendor;
import com.marcellus.springwebfluxrestapi.repositories.CategoryRepository;
import com.marcellus.springwebfluxrestapi.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) {
        loadCategories();
        loadVendors();
    }
    private void loadCategories() {

        categoryRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .just("Fruits", "Nuts", "Breads", "Meats", "Eggs")
                                .map(name -> new Category(null, name))
                                .flatMap(categoryRepository::save))
                .then(categoryRepository.count())
                .subscribe(categories -> System.out.println(categories + " categories saved"));

    }
    private void loadVendors() {

        vendorRepository
                .deleteAll()
                .thenMany(
                        Flux.just(
                                Vendor.builder().firstName("Joe").lastName("Buck").build(),
                                Vendor.builder().firstName("Michael").lastName("Weston").build(),
                                Vendor.builder().firstName("Jessie").lastName("Waters").build(),
                                Vendor.builder().firstName("Jimmy").lastName("Buffet").build()
                        )
                        .flatMap(vendorRepository::save)
                )
                .then(vendorRepository.count())
                .subscribe(vendors -> System.out.println(vendors + " vendors saved"));

    }
}
