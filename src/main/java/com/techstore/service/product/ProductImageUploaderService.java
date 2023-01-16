package com.techstore.service.product;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.springframework.web.multipart.MultipartFile;

import com.techstore.exception.product.ProductImageUploaderServiceException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.techstore.utils.FileUtils.generateFileName;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class ProductImageUploaderService implements IProductImageUploaderService {
    private final static String METADATA_DOWNLOAD_TOKENS_KEY = "firebaseStorageDownloadTokens";
    private final static String SERVICE_ACCOUNT_JSON_URL = "C:/Users/J/Downloads/tech-store-2e9a6-firebase-adminsdk-oew5e-cdbb73a8c2.json";
    private final static String IMAGE_URL_FORMAT = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media&token=%s";

    private static final String DASH = "-";
    private static final String WHITESPACE = "\\s+";
    private final static String FORWARD_SLASH =  "/";
    private final static String URL_ENCODED_FORWARD_SLASH =  "%2F";

    private final String bucketName;

    public ProductImageUploaderService(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public Set<String> upload(Collection<MultipartFile> images, String productName) {
        HashSet<BlobId> failedBloIds = new HashSet<>();
        final Storage[] storage = {StorageOptions.newBuilder().build().getService()};
        final Exception[] occurredException = {null};

        Set<String> imageUrls = images.stream().map(imageFile -> {
            String filePath = (productName.replaceAll(WHITESPACE, DASH) + "/" + generateFileName(imageFile)).replace("?", "");
            BlobId blobId = BlobId.of(bucketName, filePath);
            Map<String, String> metadata = new HashMap<>();
            metadata.put(METADATA_DOWNLOAD_TOKENS_KEY, filePath);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(imageFile.getContentType())
                    .setMetadata(metadata)
                    .build();

            String token = "";
            try {
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_URL));
                storage[0] = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
                storage[0].create(blobInfo, imageFile.getBytes());
                token = storage[0].get(bucketName, filePath).getMetadata().get(METADATA_DOWNLOAD_TOKENS_KEY);
            } catch (IOException ioe) {
                failedBloIds.add(blobId);
                occurredException[0] = ioe;
            }

            return String.format(IMAGE_URL_FORMAT, bucketName, formatUrl(filePath), formatUrl(token));
        }).collect(Collectors.toSet());

        if (!failedBloIds.isEmpty()) {
            storage[0].delete(failedBloIds);
            throw new ProductImageUploaderServiceException(buildFailedImageUploadsMessage(failedBloIds), occurredException[0]);
        }

        return imageUrls;
    }

    @Override
    public void deleteImagesForProduct(Collection<String> imageUrls) {
        if (isNotEmpty(imageUrls)) {
            Set<BlobId> blobIds = imageUrls.stream()
                    .map(url -> {
                        int startIndex = url.indexOf("/o/") + 3;
                        int endIndex = url.indexOf("?");
                        String urlToDelete = formatUrl(url.substring(startIndex, endIndex));
                        return BlobId.of(bucketName, urlToDelete);
                    })
                    .collect(Collectors.toSet());

            GoogleCredentials credentials;
            try {
                credentials = GoogleCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_URL));
            } catch (IOException e) {
                throw new ProductImageUploaderServiceException("Failed to load google credentials", e);
            }

            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            storage.delete(blobIds);
        }
    }

    private String buildFailedImageUploadsMessage(Collection<BlobId> failedBloIds) {
        StringBuilder builder = new StringBuilder("Failed images:{");
        failedBloIds.stream().map(BlobId::getName).forEach(name -> builder.append(name).append(", "));
        builder.append("}");
        return builder.toString();
    }


    private String formatUrl(String str) {
        return str.replace(FORWARD_SLASH, URL_ENCODED_FORWARD_SLASH);
    }
}
