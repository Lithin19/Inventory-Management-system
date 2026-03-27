package com.demo.spring.controller;

import com.demo.spring.Entity.Product;
import com.demo.spring.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:8085")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    //  Add product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    //  View inventory (all products)
    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    // Update stock
    @PutMapping("/{id}/stock")
    public Product updateStock(@PathVariable Integer id, @RequestParam Integer quantity) {
        return service.updateStock(id, quantity);
    }

    //  Delete product
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteProduct(id);
        return "Deleted product with id " + id;
    }

    //  Low stock alert
    // Example: /products/low-stock?threshold=5
    @GetMapping("/low-stock")
    public List<Product> lowStock(@RequestParam(required = false) Integer threshold) {
        return service.getLowStockProducts(threshold);
    }
}
