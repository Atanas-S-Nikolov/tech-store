package com.techstore.service.product;

import com.techstore.exception.product.ProductNotFoundException;
import com.techstore.model.enums.ProductCategory;
import com.techstore.model.enums.ProductType;
import com.techstore.utils.converter.ModelConverter;
import com.techstore.model.Product;
import com.techstore.model.entity.ProductEntity;
import com.techstore.repository.IProductRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techstore.constants.FieldConstants.VALIDATED_PARAM_DEFAULT_VALUE;
import static com.techstore.utils.converter.ModelConverter.toEntity;
import static com.techstore.utils.converter.ModelConverter.toModel;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.CollectionUtils.isEmpty;

public class ProductService implements IProductService {
    private final IProductRepository repository;
    private final IProductImageUploaderService imageUploaderService;

    @PersistenceContext
    private EntityManager entityManager;
    private Set<String> lastUploadedImageUrls = new HashSet<>();

    @Autowired
    public ProductService(IProductRepository repository, IProductImageUploaderService imageUploaderService) {
        this.repository = repository;
        this.imageUploaderService = imageUploaderService;
    }

    @Override
    public Product createProduct(Product product, Collection<MultipartFile> images) {
        ProductEntity entity = toEntity(product);
        entity.setDateOfCreation(LocalDateTime.now());
        return tryToSaveProduct(images, entity);
    }

    @Override
    public Product getProduct(String productName) {
        return toModel(findProductEntityByName(productName));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Product> getProducts(boolean earlyAccess, String category, String type) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ProductEntity.class);
        if (!earlyAccess) {
            criteria.add(Restrictions.eq("earlyAccess", false));
        }
        if (!category.equals(VALIDATED_PARAM_DEFAULT_VALUE)) {
            criteria.add(Restrictions.eq("category", ProductCategory.getKeyByValue(category)));
        }
        if (!type.equals(VALIDATED_PARAM_DEFAULT_VALUE)) {
            criteria.add(Restrictions.eq("type", ProductType.getKeyByValue(type)));
        }
        List<ProductEntity> entities = criteria.list();
        return entities.stream().map(ModelConverter::toModel).collect(Collectors.toList());
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
        repository.delete(entity);
    }

    private Product tryToSaveProduct(Collection<MultipartFile> images, ProductEntity entity) {
        try {
            Collection<MultipartFile> imagesToRemove = findImagesToBeRemoved(images, entity.getImageUrls());
            images.removeAll(imagesToRemove);
            entity.setDateOfModification(LocalDateTime.now());
            return toModel(repository.save(uploadImages(images, entity)));
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
