package com.demo.spring.service;

import com.demo.spring.Entity.Product;
import com.demo.spring.exception.ResourceNotFoundException;
import com.demo.spring.exception.BadRequestException;
import com.demo.spring.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    String s="Product not found with id";
    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product addProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new BadRequestException("Product name is required");
        }

        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new BadRequestException("Quantity must be 0 or more");
        }

        return repo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product getProductById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("s" + id));
    }

    @Override
    public Product updateStock(Integer id, Integer quantity) {

        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "s " + id));

        product.setQuantity(quantity);
        return repo.save(product);
    }

    @Override
    public void deleteProduct(Integer id) {

        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "s" + id));

        repo.delete(product);
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        if (threshold == null || threshold < 0) {
            threshold = 5;
        }
        return repo.findByQuantityLessThan(threshold);
    }
}
