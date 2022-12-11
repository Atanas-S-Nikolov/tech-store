package com.techstore.constants;

public interface ApiConstants {
    String BASE_API_URL = "/api/v1";
    String PRODUCT_URL = "/product";
    String USERS_URL = BASE_API_URL + "/user";
    String PRODUCTS_URL = BASE_API_URL + PRODUCT_URL;
    String PRODUCTS_URL_REGEX = "\\/api\\/v1\\" + PRODUCT_URL;
    String PRODUCTS_EARLY_ACCESS_PARAM = "early_access";
    String PRODUCTS_EARLY_ACCESS_FALSE_REGEX = PRODUCTS_URL_REGEX + "\\?early_access=false";
    String PRODUCT_WITH_PATH_VARIABLE_REGEX = PRODUCTS_URL_REGEX + "\\/\\{name\\}";
    String ACCESS_CONTROL_URL = BASE_API_URL + "/auth";
    String LOGIN_URL = ACCESS_CONTROL_URL + "/login";
    String REFRESH_TOKEN_URL = "/refresh_token";
    String FULL_REFRESH_TOKEN_URL = ACCESS_CONTROL_URL + REFRESH_TOKEN_URL;
    String CARTS_URL = BASE_API_URL + "/cart";
    String FULL_ADD_PRODUCT_TO_CART_URL = CARTS_URL + PRODUCT_URL;
}
