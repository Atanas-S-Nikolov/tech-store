package com.techstore.constants;

public interface ApiConstants {
    String BASE_API_URL = "/api/v1";
    String USERS_URL = BASE_API_URL + "/user";
    String PRODUCTS_URL = BASE_API_URL + "/product";
    String PRODUCTS_URL_REGEX = "\\/api\\/v1\\/product";
    String PRODUCTS_EARLY_ACCESS_PARAM = "early_access";
    String PRODUCTS_EARLY_ACCESS_FALSE_REGEX = PRODUCTS_URL_REGEX + "\\?early_access=false";
    String PRODUCT_WITH_PATH_VARIABLE_REGEX = PRODUCTS_URL_REGEX + "\\/\\{name\\}";
    String LOGIN_URL = BASE_API_URL + "/login";
    String REFRESH_TOKEN_URL = "/refresh_token";
    String FULL_REFRESH_TOKEN_URL = USERS_URL + REFRESH_TOKEN_URL;
}
