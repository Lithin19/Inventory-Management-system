package com.demo.spring.service;


import com.demo.spring.Entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> getAllProducts();
    Product updateStock(Integer id, Integer quantity);
    void deleteProduct(Integer id);
    List<Product> getLowStockProducts(Integer threshold);
}
