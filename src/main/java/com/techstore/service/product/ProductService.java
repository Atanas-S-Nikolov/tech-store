package com.techstore.service.product;

import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.model.response.PageResponse;
import com.techstore.model.response.ProductResponse;
import com.techstore.repository.IProductRepository;
import com.techstore.utils.converter.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.techstore.constants.FieldConstants.VALIDATED_PARAM_DEFAULT_VALUE;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
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
    public ProductResponse createProduct(ProductDto productDto, Collection<MultipartFile> images) {
        ProductEntity entity = toEntity(productDto);
        entity.setDateOfCreation(LocalDateTime.now());
        return tryToSaveProduct(images, entity);
    }

    @Override
    public ProductResponse getProduct(String productName) {
        return toResponse(findProductEntityByName(productName));
    }

    @Override
    public PageResponse<ProductResponse> getProducts(boolean earlyAccess, String category, String type, int page, int size) {
        boolean categoryIsPresent = !category.equals(VALIDATED_PARAM_DEFAULT_VALUE);
        boolean typeIsPresent = !type.equals(VALIDATED_PARAM_DEFAULT_VALUE);
        ProductCategory productCategory = ProductCategory.getKeyByValue(category);
        ProductType productType = ProductType.getKeyByValue(type);
        Pageable paging = PageRequest.of(page, size, Sort.by("earlyAccess").descending());

        Page<ProductEntity> productsPage;
        if (earlyAccess) {
            if (categoryIsPresent) {
                if (typeIsPresent) {
                    productsPage = repository.findAllByCategoryAndType(productCategory, productType, paging);
                } else {
                    productsPage = repository.findAllByCategory(productCategory, paging);
                }

            } else {
                productsPage = repository.findAll(paging);
            }
        } else {
            if (categoryIsPresent) {
                if (typeIsPresent) {
                    productsPage = repository.findAllByEarlyAccessAndCategoryAndType(false, productCategory, productType, paging);
                } else {
                    productsPage = repository.findAllByEarlyAccessAndCategory(false, productCategory, paging);
                }

            } else {
                productsPage = repository.findAllByEarlyAccess(false, paging);
            }
        }

        List<ProductResponse> products = productsPage.getContent().stream().map(ModelConverter::toResponse).collect(toList());
        return new PageResponse<>(productsPage.getTotalElements(), productsPage.getTotalPages(), productsPage.getNumber(), products);
    }

    @Override
    public ProductResponse updateProduct(ProductDto productDto, Collection<MultipartFile> images) {
        ProductEntity existingEntity = findProductEntityByName(productDto.getName());
        ProductEntity productEntity = toEntity(productDto);
        productEntity.setId(existingEntity.getId());
        productEntity.setImageUrls(existingEntity.getImageUrls());
        return tryToSaveProduct(images, productEntity);
    }

    @Transactional
    @Override
    public void deleteProduct(String productName) {
        ProductEntity entity = findProductEntityByName(productName);
        imageUploaderService.deleteImagesForProduct(entity.getImageUrls());
        repository.delete(entity);
    }

    private ProductResponse tryToSaveProduct(Collection<MultipartFile> images, ProductEntity entity) {
        try {
            images = nonNull(images) ? images : new HashSet<>();
            Collection<MultipartFile> imagesToRemove = findImagesToBeRemoved(images, entity.getImageUrls());
            images.removeAll(imagesToRemove);
            entity.setDateOfModification(LocalDateTime.now());
            return toResponse(repository.save(uploadImages(images, entity)));
        } catch (Exception exception) {
            imageUploaderService.deleteImagesForProduct(lastUploadedImageUrls);
            throw exception;
        }
    }

    private Collection<MultipartFile> findImagesToBeRemoved(Collection<MultipartFile> images, Set<String> existingImageUrls) {
        Collection<MultipartFile> imagesToRemove = new HashSet<>();
        if (!isEmpty(images)) {
            for (String url : existingImageUrls) {
                for (MultipartFile image : images) {
                    String imageName = nonNull(image.getOriginalFilename())
                            ? image.getOriginalFilename()
                            : EMPTY;
                    if (url.contains(imageName)) {
                        imagesToRemove.add(image);
                        break;
                    }
                }
            }
        }
        return imagesToRemove;
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
        return repository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with name '%s' is not found", name)));
    }
}
