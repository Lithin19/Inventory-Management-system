package com.demo.spring.Entity;

import jakarta.persistence.*;


import java.sql.Timestamp;

@Entity
@Table(name="products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private Double price;

    @Column
    private Integer quantity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private java.sql.Timestamp createdAt;

    public Product() {
    }

    public Product(String name, String category, Double price, Integer quantity, Timestamp createdAt) {

        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}