package com.techstore.service.favorites;

import com.techstore.exception.favorites.FavoritesNotFoundException;
import com.techstore.model.dto.FavoritesDto;
import com.techstore.model.entity.FavoritesEntity;
import com.techstore.model.entity.ProductEntity;
import com.techstore.model.entity.UserEntity;
import com.techstore.model.response.FavoritesResponse;
import com.techstore.repository.IFavoritesRepository;
import com.techstore.repository.IProductRepository;
import com.techstore.repository.IUserRepository;

import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.techstore.utils.converter.ModelConverter.toResponse;
import static java.lang.String.format;

public class FavoritesService implements IFavoritesService {
    private final IFavoritesRepository repository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public FavoritesService(IFavoritesRepository repository, IUserRepository userRepository, IProductRepository productRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public FavoritesEntity createDefaultFavorites() {
        return repository.save(new FavoritesEntity(null, null, new HashSet<>()));
    }

    @Override
    public FavoritesResponse getFavorites(String username) {
        return toResponse(findFavorites(username));
    }

    @Transactional
    @Override
    public FavoritesResponse addFavorite(FavoritesDto favoritesDto) {
        FavoritesEntity entity = findFavorites(favoritesDto.getUsername());
        Set<ProductEntity> products = addProductIfPresent(favoritesDto, entity);
        entity.setProducts(products);
        return toResponse(repository.save(entity));
    }

    @Transactional
    @Override
    public FavoritesResponse removeFavorite(FavoritesDto favoritesDto) {
        FavoritesEntity entity = findFavorites(favoritesDto.getUsername());
        Set<ProductEntity> products = removeProductIfPresent(favoritesDto, entity);
        entity.setProducts(products);
        return toResponse(repository.save(entity));
    }

    @Transactional
    @Override
    public void deleteFavorites(String username) {
        repository.delete(findFavorites(username));
    }

    private FavoritesEntity findFavorites(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findUserByUsername(username);
        FavoritesEntity entity = new FavoritesEntity();
        if (userEntityOptional.isPresent()) {
            entity = repository.findByUser(userEntityOptional.get())
                    .orElseThrow(() -> new FavoritesNotFoundException(format("Favorites for user with username: '%s' are not found", username)));
        }
        return entity;
    }

    private Set<ProductEntity> addProductIfPresent(FavoritesDto dto, FavoritesEntity entity) {
        Set<ProductEntity> entities = entity.getProducts();
        Set<String> productNames = dto.getProductNames();
        Optional<ProductEntity> optionalProduct = productRepository.findProductByName(getProductNameFromSet(productNames));
        optionalProduct.ifPresent(productEntity -> entities.add(persistProductEntity(productEntity, entity)));
        return entities;
    }

    private Set<ProductEntity> removeProductIfPresent(FavoritesDto dto, FavoritesEntity entity) {
        Set<ProductEntity> entities = entity.getProducts();
        Set<String> productNames = dto.getProductNames();
        Optional<ProductEntity> optionalProduct = productRepository.findProductByName(getProductNameFromSet(productNames));
        optionalProduct.ifPresent(productEntity -> entities.remove(persistProductEntity(productEntity, entity)));
        return entities;
    }

    private String getProductNameFromSet(Set<String> productNames) {
        String productName = StringUtils.EMPTY;
        if (productNames.iterator().hasNext()) {
            productName = productNames.iterator().next();
        }
        return productName;
    }

    private ProductEntity persistProductEntity(ProductEntity productEntity, FavoritesEntity favoritesEntity) {
        Set<FavoritesEntity> favoritesToSave = productEntity.getFavorites();
        favoritesToSave.add(favoritesEntity);
        productEntity.setFavorites(favoritesToSave);
        return productRepository.save(productEntity);
    }
}
