package com.techstore.constants;

import static com.techstore.constants.RoleConstants.ROLE_CUSTOMER;

public interface ApiConstants {
    String BASE_API_URL = "/api/v1";
    String BASE_API_REGEX = "\\/api\\/v1\\";
    String PRODUCT_URL = "/product";
    String USERS_URL = BASE_API_URL + "/user";
    String PRODUCTS_URL = BASE_API_URL + PRODUCT_URL;
    String PRODUCTS_URL_REGEX = BASE_API_REGEX + PRODUCT_URL;
    String PRODUCTS_EARLY_ACCESS_PARAM = "early_access";
    String PRODUCTS_CATEGORY_PARAM = "category";
    String PRODUCTS_TYPE_PARAM = "type";
    String PRODUCTS_WITH_PARAMS_REGEX = PRODUCTS_URL_REGEX + "\\?early_access=false.*";
    String ACCESS_CONTROL_URL = BASE_API_URL + "/auth";
    String LOGIN_URL = ACCESS_CONTROL_URL + "/login";
    String REGISTER_URL = "/register";
    String FULL_REGISTER_URL = ACCESS_CONTROL_URL + REGISTER_URL;
    String REFRESH_TOKEN_URL = "/refresh_token";
    String FULL_REFRESH_TOKEN_URL = ACCESS_CONTROL_URL + REFRESH_TOKEN_URL;
    String CARTS_URL = BASE_API_URL + "/cart";
    String CREATE_CUSTOMER_URL = USERS_URL + "/role/" + ROLE_CUSTOMER;
    String CLEAR_CART_URL = "/clear";
}
