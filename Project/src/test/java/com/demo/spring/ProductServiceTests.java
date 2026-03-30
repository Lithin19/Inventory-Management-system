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
    //“testAddProductSuccess is a service‑layer unit test that verifies the addProduct method successfully saves a product by calling the repository and returns the correct product data.”
    //we are actually following a pattern of AAA.(Arrange -is creating a dummy product to send it to the repo,and Act-is calling the service layer,A-assesing that the values are not alterated "

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
    //“This service‑layer test verifies that when a product with a null name is passed, the service throws a BadRequestException and prevents the product from being saved to the database.”
    //Arrange → create invalid product
        // Act + Assert → assert exception is thrown
        //Verify → repository interaction did not happen
    void testAddProductWithNullName() {
        Product product = buildProduct(null, "Electronics", 1000.0, 5);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    //“This service‑layer test verifies that a product with a blank name is rejected by the business logic, throwing a BadRequestException and preventing the product from being saved.”

    void testAddProductWithBlankName() {
        Product product = buildProduct("   ", "Electronics", 1000.0, 5);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    //“This test ensures products with null quantity are treated as invalid and never persisted.”
    void testAddProductWithNullQuantity() {
        Product product = buildProduct("Mouse", "Electronics", 500.0, null);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    //“This test ensures products with negative quantity are treated as invalid and never saved.”
    void testAddProductWithNegativeQuantity() {
        Product product = buildProduct("Mouse", "Electronics", 500.0, -2);

        Assertions.assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));

        verify(productRepository, never()).save(any(Product.class));
    }

    /* ---------------- getAllProducts() ---------------- */

    @Test
    //“This service‑layer test verifies that the getAllProducts method returns a list of products from the repository and correctly handles the data.”
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
    //“This service‑layer test verifies that when no products are available, the getAllProducts method returns an empty list without errors.”
    void testGetAllProductsReturnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAllProducts();

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    /* ---------------- getProductById() ---------------- */

    @Test
    //“This service‑layer test verifies that when a product exists for a given ID, the getProductById method correctly retrieves and returns the product from the repository.”
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
    //“This service‑layer test verifies that when a product is not found by ID, the service throws a ResourceNotFoundException instead of returning null.”
    void testGetProductByIdNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1));

        verify(productRepository, times(1)).findById(1);
    }

    /* ---------------- updateStock() ---------------- */

    @Test
    //This test verifies that when a product exists and a valid stock quantity is provided, the updateStock service method successfully updates the product’s quantity and saves it to the repository.


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
    //This test verifies that when a negative stock quantity is provided, the updateStock service method throws a BadRequestException and stops execution without calling the repository.
    void testUpdateStockWithNegativeQuantity() {
        Assertions.assertThrows(BadRequestException.class,
                () -> productService.updateStock(1, -10));

        verify(productRepository, never()).findById(anyInt());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    //This test verifies that when a product does not exist, the updateStock service method throws a ResourceNotFoundException and does not attempt to save any data.
    void testUpdateStockProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.updateStock(1, 10));

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    /* ---------------- deleteProduct() ---------------- */

    @Test
    //This test verifies that when a product exists, the deleteProduct service method successfully deletes the product after checking its existence.
    void testDeleteProductSuccess() {
        Product product = buildProduct("Keyboard", "Electronics", 1500.0, 5);
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    //This test verifies that when a product does not exist, the deleteProduct service method throws a ResourceNotFoundException and does not attempt to delete anything from the database.
    void testDeleteProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1));

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).delete(any(Product.class));
    }

    /* ---------------- getLowStockProducts() ---------------- */

    @Test
    //This test verifies that when a threshold is provided, the getLowStockProducts service method correctly returns products whose quantity is below the given threshold.
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
    //This test verifies that when the threshold parameter is null, the getLowStockProducts service method defaults the threshold to 5 and returns the correct result.
    void testGetLowStockProductsWithNullThresholdDefaultsTo5() {
        when(productRepository.findByQuantityLessThan(5)).thenReturn(List.of());

        List<Product> result = productService.getLowStockProducts(null);

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByQuantityLessThan(5);
    }

    @Test
    //This test verifies that when a negative threshold is provided, the getLowStockProducts service method defaults the threshold to 5 and returns the correct result.
    void testGetLowStockProductsWithNegativeThresholdDefaultsTo5() {
        when(productRepository.findByQuantityLessThan(5)).thenReturn(List.of());

        List<Product> result = productService.getLowStockProducts(-1);

        Assertions.assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByQuantityLessThan(5);
    }
}