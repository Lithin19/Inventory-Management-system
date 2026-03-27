package com.demo.spring;

import com.demo.spring.Entity.Product;
import com.demo.spring.service.ProductService;
import com.demo.spring.service.ProductServiceImpl;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTests {

     @Autowired
     MockMvc mvc;

     @MockitoBean
     ProductServiceImpl productServiceImpl;

          @Test
          void testGetAllProducts() throws Exception {
               List<Product> productList=new ArrayList<>();
               productList.add(new Product("Laptop","Electronics",55000.0, 10, new Timestamp(System.currentTimeMillis())));
               productList.add(new Product("Mouse","Electronics",120000.0,20,new Timestamp(System.currentTimeMillis())));
               when(productServiceImpl.getAllProducts()).thenReturn(productList);
               mvc.perform(get("/products/"))
                       .andExpect(status().isOk())
                       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                       .andExpect(jsonPath("$.length()").value(2));
          }


}
