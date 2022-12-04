package com.techstore.controller;

import com.techstore.model.Product;
import com.techstore.model.dto.ProductDto;
import com.techstore.service.product.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

import static com.techstore.constants.ApiConstants.PRODUCTS_URL;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController(value = "product-controller")
@RequestMapping(value = PRODUCTS_URL)
public class ProductController {
    private final IProductService service;

    @Autowired
    public ProductController(IProductService service) {
        this.service = service;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) Collection<MultipartFile> images) {
        return ResponseEntity.status(CREATED).body(service.createProduct(toModel(productDto), images));
    }

    @GetMapping(path = "/{name}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable String name) {
        return ResponseEntity.status(OK).body(service.getProduct(name));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Product>> getAllProducts() {
        return ResponseEntity.status(OK).body(service.getAllProducts());
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "images", required = false) Collection<MultipartFile> images) {
        return ResponseEntity.status(OK).body(service.updateProduct(toModel(productDto), images));
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity<?> deleteProduct(@PathVariable String name) {
        service.deleteProduct(name);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
