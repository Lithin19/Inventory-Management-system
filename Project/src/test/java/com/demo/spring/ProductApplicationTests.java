package com.demo.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.spring.Entity.Product;
import com.demo.spring.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
class ProductApplicationTests {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ProductService productService;


    @Test
    void testAddProductSuccess() throws Exception {
        Product saved = productWithCommonFields(1, "Laptop", "Electronics", 55000.0, 10);

        when(productService.addProduct(any(Product.class))).thenReturn(saved);

        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Laptop",
                                  "category":"Electronics",
                                  "price":55000.0,
                                  "stock":10
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    // ✅ FIXED helper method (no constructor dependency)
    private Product productWithCommonFields(int id, String name, String category, Double price, int stock) {
        Product p = new Product();
        p.setId(id);           // matches your controller using Integer id
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        return p;




    }


    @Test

    void testAddProductServiceThrowsException_5xx() throws Exception {
        when(productService.addProduct(any(Product.class))).thenThrow(new RuntimeException("DB down"));

        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":"Laptop",
                                  "category":"Electronics",
                                  "price":55000.0,
                                  "stock":10
                                }
                                """))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    // =========================================================
    // GET /products  (getAll)
    // =========================================================

    @Test
    void testGetAllProductsSuccess() throws Exception {
        List<Product> list = new ArrayList<>();
        list.add(productWithCommonFields(1, "Laptop", "Electronics", 55000.0, 10));
        list.add(productWithCommonFields(2, "Mouse", "Electronics", 500.0, 50));

        when(productService.getAllProducts()).thenReturn(list);

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(productService, times(1)).getAllProducts();
    }

    @Test

    //“This test ensures the GET /products API returns 200 OK and an empty JSON array when no products are available.”
    void testGetAllProductsEmptyListSuccess() throws Exception {
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    //“This test verifies that when the service layer throws an exception while fetching products, the controller correctly returns a 5xx server error response.”

    void testGetAllProductsServiceThrowsException_5xx() throws Exception {
        when(productService.getAllProducts()).thenThrow(new RuntimeException("failure"));

        mvc.perform(get("/products"))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).getAllProducts();
    }

    // =========================================================
    // GET /products/{id}  (getById)
    // =========================================================

    @Test
    //“testGetProductByIdSuccess verifies that when a valid product ID is provided, the controller successfully returns the product with HTTP 200 OK and JSON response, while correctly invoking the service layer.”
    void testGetProductByIdSuccess() throws Exception {
        Product p = productWithCommonFields(1, "Laptop", "Electronics", 55000.0, 10);

        when(productService.getProductById(1)).thenReturn(p);

        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).getProductById(1);
    }



    @Test
    //“This test verifies that when the service layer throws an exception while fetching a product by ID, the controller correctly returns a 5xx server error response.”
    void testGetProductByIdServiceThrowsException_5xx() throws Exception {
        when(productService.getProductById(1)).thenThrow(new RuntimeException("not found"));

        mvc.perform(get("/products/1"))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).getProductById(1);
    }

    // =========================================================
    // PUT /products/{id}/stock?quantity=  (updateStock)
    // =========================================================



    @Test
    //“This test verifies that when the service layer fails while updating a product’s stock, the controller correctly responds with a 5xx server error.”

    void testUpdateStockServiceThrowsException_5xx() throws Exception {
        when(productService.updateStock(3, 10)).thenThrow(new RuntimeException("error"));

        mvc.perform(put("/products/3/stock")
                        .param("quantity", "10"))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).updateStock(3, 10);
    }

    // =========================================================
    // DELETE /products/{id}  (delete)
    // =========================================================

    @Test
    //“This controller test verifies that DELETE /products/2 successfully calls the service’s deleteProduct(2) method, returns HTTP 200 OK, and responds with the expected confirmation message.”
    //“When the controller calls productService.deleteProduct(2), do nothing (don’t throw any exception).”
    void testDeleteProductSuccess() throws Exception {
        doNothing().when(productService).deleteProduct(2);

        mvc.perform(delete("/products/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted product with id 2"));

        verify(productService, times(1)).deleteProduct(2);
    }



    @Test
    //“This test verifies that when the service layer throws an exception during product deletion, the controller correctly responds with a 5xx server error.”

    void testDeleteProductServiceThrowsException_5xx() throws Exception {
        doThrow(new RuntimeException("delete failed")).when(productService).deleteProduct(1);

        mvc.perform(delete("/products/1"))
                .andExpect(status().is5xxServerError());

        verify(productService, times(1)).deleteProduct(1);
    }

    // =========================================================
    // GET /products/low-stock?threshold=  (lowStock)
    // =========================================================

    @Test
    //“This test verifies that the low‑stock products API correctly returns products whose stock is below the given threshold, responding with HTTP 200 OK and the expected JSON result.”
    void testLowStockWithThresholdSuccess() throws Exception {
        List<Product> low = new ArrayList<>();
        low.add(productWithCommonFields(2, "Mouse", "Electronics", 500.0, 3));

        when(productService.getLowStockProducts(5)).thenReturn(low);

        mvc.perform(get("/products/low-stock")
                        .param("threshold", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(productService, times(1)).getLowStockProducts(5);
    }

    @Test

    //“This test verifies that the low‑stock products endpoint works correctly even when the optional threshold parameter is not provided, returning a successful response with the expected data.”
    void testLowStockWithoutThresholdSuccess() throws Exception {
        List<Product> low = new ArrayList<>();
        low.add(productWithCommonFields(3, "Keyboard", "Electronics", 1500.0, 2));

        when(productService.getLowStockProducts(null)).thenReturn(low);

        mvc.perform(get("/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(productService, times(1)).getLowStockProducts(null);
    }


}