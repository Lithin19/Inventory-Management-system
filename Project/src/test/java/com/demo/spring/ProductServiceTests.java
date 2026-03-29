package com.demo.spring;

import com.demo.spring.Entity.Product;
import com.demo.spring.exception.BadRequestException;
import com.demo.spring.exception.ResourceNotFoundException;
import com.demo.spring.repository.ProductRepository;
import com.demo.spring.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTests {

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    // Helper to build products using your entity constructor
    private Product buildProduct(String name, String category, Double price, Integer quantity) {
        return new Product(name, category, price, quantity, null);
        // createdAt is DB-managed (insertable=false, updatable=false), so null is fine in tests
    }

    /* ---------------- addProduct() ---------------- */

    @Test
    void testAddProductSuccess() {
        Product product = buildProduct("Laptop", "Electronics", 65000.0, 10);

        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.addProduct(product);

        Assertions.assertEquals("Laptop", saved.getName());
        Assertions.assertEquals("Electronics", saved.getCategory());
        Assertions.assertEquals(65000.0, saved.getPrice());
        Assertions.assertEquals(10, saved.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testAddProductWithNullName() {
        Product product = buildProduct(null, "Electronics", 1000.0, 5);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProductWithBlankName() {
        Product product = buildProduct("   ", "Electronics", 1000.0, 5);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProductWithNullQuantity() {
        Product product = buildProduct("Mouse", "Electronics", 500.0, null);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testAddProductWithNegativeQuantity() {
        Product product = buildProduct("Mouse", "Electronics", 500.0, -2);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    /* ---------------- getAllProducts() ---------------- */

    @Test
    void testGetAllProductsReturnsList() {
        List<Product> products = List.of(
                buildProduct("Pen", "Stationery", 10.0, 50),
                buildProduct("Book", "Stationery", 200.0, 20)
        );

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Pen", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductsReturnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAllProducts();

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    /* ---------------- getProductById() ---------------- */

    @Test
    void testGetProductByIdSuccess() {
        Product product = buildProduct("Tablet", "Electronics", 25000.0, 15);
        product.setId(1); // id is generated in DB, but in tests we can set manually

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1);

        Assertions.assertEquals("Tablet", result.getName());
        Assertions.assertEquals(15, result.getQuantity());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1));

        verify(productRepository, times(1)).findById(1);
    }

    /* ---------------- updateStock() ---------------- */

    @Test
    void testUpdateStockSuccess() {
        Product product = buildProduct("Monitor", "Electronics", 12000.0, 10);
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.updateStock(1, 25);

        Assertions.assertEquals(25, updated.getQuantity());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateStockWithNegativeQuantity() {
        Assertions.assertThrows(BadRequestException.class,
                () -> productService.updateStock(1, -10));

        verify(productRepository, never()).findById(anyInt());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdateStockProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.updateStock(1, 10));

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    /* ---------------- deleteProduct() ---------------- */

    @Test
    void testDeleteProductSuccess() {
        Product product = buildProduct("Keyboard", "Electronics", 1500.0, 5);
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1));

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).delete(any(Product.class));
    }

    /* ---------------- getLowStockProducts() ---------------- */

    @Test
    void testGetLowStockProductsWithThreshold() {
        List<Product> lowStock = List.of(
                buildProduct("Charger", "Electronics", 800.0, 2)
        );

        when(productRepository.findByQuantityLessThan(3)).thenReturn(lowStock);

        List<Product> result = productService.getLowStockProducts(3);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getQuantity());
        verify(productRepository, times(1)).findByQuantityLessThan(3);
    }

    @Test
    void testGetLowStockProductsWithNullThresholdDefaultsTo5() {
        when(productRepository.findByQuantityLessThan(5)).thenReturn(List.of());

        List<Product> result = productService.getLowStockProducts(null);

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByQuantityLessThan(5);
    }

    @Test
    void testGetLowStockProductsWithNegativeThresholdDefaultsTo5() {
        when(productRepository.findByQuantityLessThan(5)).thenReturn(List.of());

        List<Product> result = productService.getLowStockProducts(-1);

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByQuantityLessThan(5);
    }
}