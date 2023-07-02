package com.techstore.service.product;

import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.dto.ImageDto;
import com.techstore.model.dto.ProductDto;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.model.response.ImageResponse;
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
import java.util.UUID;

import static com.techstore.constants.FieldConstants.VALIDATED_PARAM_DEFAULT_VALUE;
import static com.techstore.utils.auth.AuthUtils.checkCurrentAuthentication;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
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
    public ProductResponse createProduct(ProductDto productDto, Collection<MultipartFile> images, MultipartFile mainImage) {
        ProductEntity entity = toEntity(productDto);
        entity.setImagesDirectory(UUID.randomUUID().toString());
        entity.setDateOfCreation(LocalDateTime.now());
        return tryToSaveProduct(images, mainImage, entity);
    }

    @Override
    public ProductResponse getProduct(String productName) {
        ProductEntity entity = findProductEntityByName(productName);
        if (entity.isEarlyAccess()) {
            checkCurrentAuthentication();
        }
        return toResponse(entity);
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
    public List<ProductResponse> search(String keyword, boolean earlyAccess) {
        List<ProductEntity> products = earlyAccess ? repository.search(keyword) : repository.searchWithoutEarlyAccess(keyword);
        return products.stream().map(ModelConverter::toResponse).collect(toList());
    }

    @Override
    public PageResponse<ProductResponse> searchQuery(String keyword, boolean earlyAccess, int page, int size) {
        PageRequest paging = PageRequest.of(page, size);
        Page<ProductEntity> productsPage = earlyAccess
                ? repository.searchQuery(keyword, paging)
                : repository.searchQueryWithoutEarlyAccess(keyword, paging);
        List<ProductResponse> products = productsPage.getContent().stream().map(ModelConverter::toResponse).collect(toList());
        return new PageResponse<>(productsPage.getTotalElements(), productsPage.getTotalPages(), productsPage.getNumber(), products);
    }

    @Override
    public ProductResponse updateProduct(String productName, ProductDto productDto, Collection<MultipartFile> images, MultipartFile mainImage,
                                         Collection<String> deleteImageUrls) {
        ProductEntity existingEntity = findProductEntityByName(productName);
        String imagesDirectory = existingEntity.getImagesDirectory();
        ProductEntity productEntity = toEntity(productDto);
        productEntity.setId(existingEntity.getId());
        productEntity.setDateOfCreation(existingEntity.getDateOfCreation());
        productEntity.setImagesDirectory(imagesDirectory);
        productEntity.setMainImageUrl(existingEntity.getMainImageUrl());
        imageUploaderService.deleteImagesForProduct(deleteImageUrls);
        productEntity.setImageUrls(imageUploaderService.getImageUrlsForProduct(imagesDirectory));
        return tryToSaveProduct(images, mainImage, productEntity);
    }

    @Transactional
    @Override
    public void deleteProduct(String productName) {
        ProductEntity entity = findProductEntityByName(productName);
        Set<String> imageUrls = entity.getImageUrls();
        String mainImageUrl = entity.getMainImageUrl();
        if (isNotEmpty(imageUrls) && nonNull(mainImageUrl)) {
            imageUrls.add(mainImageUrl);
            imageUploaderService.deleteImagesForProduct(imageUrls);
        }
        repository.delete(entity);
    }

    private ProductResponse tryToSaveProduct(Collection<MultipartFile> images, MultipartFile mainImage, ProductEntity entity) {
        try {
            images = nonNull(images) ? images : new HashSet<>();
            entity.setDateOfModification(LocalDateTime.now());
            List<ImageDto> imageDtos = images.stream().map(image -> new ImageDto(image, false)).collect(toList());
            if (nonNull(mainImage)) {
                imageDtos.add(new ImageDto(mainImage, true));
            }
            return toResponse(repository.save(uploadImages(imageDtos, entity)));
        } catch (Exception exception) {
            imageUploaderService.deleteImagesForProduct(lastUploadedImageUrls);
            throw exception;
        }
    }

    private ProductEntity uploadImages(Collection<ImageDto> images, ProductEntity entity) {
        if (!isEmpty(images)) {
            Set<ImageResponse> uploadedImages = imageUploaderService.upload(images, entity.getImagesDirectory());
            Set<String> uploadedImageUrls = uploadedImages.stream().map(ImageResponse::getUrl).collect(toSet());
            lastUploadedImageUrls = Set.copyOf(uploadedImageUrls);
            Set<String> existingImageUrls = entity.getImageUrls();
            ImageResponse mainImage = new ImageResponse();
            for (ImageResponse image : uploadedImages) {
                if (image.isMain()) {
                    mainImage = image;
                    uploadedImageUrls.remove(image.getUrl());
                }
            }
            existingImageUrls.addAll(uploadedImageUrls);
            entity.setImageUrls(existingImageUrls);
            entity.setMainImageUrl(mainImage.getUrl());
        }
        return entity;
    }

    private ProductEntity findProductEntityByName(String name) {
        return repository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with name '%s' is not found", name)));
    }
}
