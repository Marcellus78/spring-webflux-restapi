package com.marcellus.springwebfluxrestapi.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Autowired
    Category category;

    @Test
    public void noArgsConstructorTest() {

        category = new Category();

        assertNull(category.getId());
        assertNull(category.getName());
    }
    @Test
    public void allArgsConstructorTest() {

        category = new Category("1", "first category");

        assertEquals("1", category.getId());
        assertEquals("first category", category.getName());
    }
    @Test
    public  void settersTest() {

        category = new Category();
        category.setId("1");
        category.setName("first category");

        assertThat(category.getId(), equalTo("1"));
        assertThat(category.getName(), equalTo("first category"));
    }
}