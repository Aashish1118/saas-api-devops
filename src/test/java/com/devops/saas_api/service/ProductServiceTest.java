package com.devops.saas_api.service;

import com.devops.saas_api.entity.Product;
import com.devops.saas_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Laptop", "A powerful laptop", new BigDecimal("999.99"), 10);
        product.setId(1L);
    }

    @Test
    void getAllProducts_returnsListOfProducts() {
        Product secondProduct = new Product("Mouse", "Wireless mouse", new BigDecimal("19.99"), 50);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, secondProduct));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_returnsProduct_whenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_throwsException_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productService.getProductById(99L)
        );

        assertEquals("404 NOT_FOUND \"Product not found with id: 99\"", exception.getMessage());
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void createProduct_savesAndReturnsProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_updatesExistingProductAndSaves() {
        Product updatedDetails = new Product("Laptop Pro", "An upgraded laptop", new BigDecimal("1299.99"), 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, updatedDetails);

        assertEquals("Laptop Pro", result.getName());
        assertEquals("An upgraded laptop", result.getDescription());
        assertEquals(new BigDecimal("1299.99"), result.getPrice());
        assertEquals(5, result.getStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProduct_deletesExistingProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_throwsException_whenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> productService.deleteProduct(99L)
        );

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).delete(any(Product.class));
    }
}