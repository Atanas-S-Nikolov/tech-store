package com.techstore.service;

import com.techstore.exception.ProductConstraintViolationException;
import com.techstore.exception.ProductNotFoundException;
import com.techstore.exception.ProductServiceException;
import com.techstore.model.ModelConverter;
import com.techstore.model.Product;
import com.techstore.model.entity.ProductEntity;
import com.techstore.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.techstore.model.ModelConverter.toEntity;
import static com.techstore.model.ModelConverter.toModel;
import static org.springframework.util.CollectionUtils.isEmpty;

public class ProductService implements IProductService {
    private final IProductRepository repository;
    private final IProductImageUploaderService imageUploaderService;
    private Set<String> lastUploadedImageUrls = new HashSet<>();

    @Autowired
    public ProductService(IProductRepository repository, IProductImageUploaderService imageUploaderService) {
        this.repository = repository;
        this.imageUploaderService = imageUploaderService;
    }

    @Override
    public Product createProduct(Product product, Collection<MultipartFile> images) {
        return tryToSaveProduct(images, toEntity(product));
    }

    @Override
    public Product getProduct(String productName) {
        return toModel(findProductEntityByName(productName));
    }

    @Override
    public Collection<Product> getAllProducts() {
        return executeDBCall(() -> repository.findAll().stream()
                .map(ModelConverter::toModel)
                .collect(Collectors.toList()));
    }

    @Override
    public Product updateProduct(Product product, Collection<MultipartFile> images) {
        ProductEntity existingEntity = findProductEntityByName(product.getName());
        ProductEntity productEntity = toEntity(product);
        productEntity.setId(existingEntity.getId());
        productEntity.setImageUrls(existingEntity.getImageUrls());
        return tryToSaveProduct(images, productEntity);
    }

    @Transactional
    @Override
    public void deleteProduct(String productName) {
        ProductEntity entity = findProductEntityByName(productName);
        imageUploaderService.deleteImagesForProduct(entity.getImageUrls());
        executeDBCall(() -> repository.delete(entity));
    }

    private Product tryToSaveProduct(Collection<MultipartFile> images, ProductEntity entity) {
        try {
            return toModel(executeDBCall(() -> repository.save(uploadImages(images, entity))));
        } catch (Exception exception) {
            imageUploaderService.deleteImagesForProduct(lastUploadedImageUrls);
            throw exception;
        }
    }

    private ProductEntity uploadImages(Collection<MultipartFile> images, ProductEntity entity) {
        if (!isEmpty(images)) {
            lastUploadedImageUrls = imageUploaderService.upload(images, entity.getName());
            Set<String> existingImageUrls = entity.getImageUrls();
            existingImageUrls.addAll(lastUploadedImageUrls);
            entity.setImageUrls(existingImageUrls);
        }
        return entity;
    }

    private ProductEntity findProductEntityByName(String name) {
        return executeDBCall(() -> repository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with name '%s' is not found", name))));
    }

    public <T> T executeDBCall(Supplier<T> supplier) {
        try{
            return supplier.get();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ProductConstraintViolationException("Product constraint violation", dataIntegrityViolationException);
        } catch (DataAccessException dataAccessException) {
            throw new ProductServiceException("Error while connecting the database", dataAccessException);
        }
    }

    public void executeDBCall(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ProductConstraintViolationException("Product constraint violation", dataIntegrityViolationException);
        } catch (DataAccessException dataAccessException) {
            throw new ProductServiceException("Error while connecting the database", dataAccessException);
        }
    }
}
