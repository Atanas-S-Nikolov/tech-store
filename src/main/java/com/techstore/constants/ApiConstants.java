package com.techstore.constants;

import static com.techstore.constants.RoleConstants.ROLE_CUSTOMER;

public interface ApiConstants {
    String ALL_MATCHER = "/**";
    String BASE_API_URL = "/api/v1";
    String BASE_API_URL_MATCHER = BASE_API_URL + ALL_MATCHER;
    String BASE_API_REGEX = "\\/api\\/v1\\";
    String PRODUCT_URL = "/product";
    String USERS_URL = BASE_API_URL + "/user";
    String PRODUCTS_URL = BASE_API_URL + PRODUCT_URL;
    String PRODUCTS_URL_REGEX = BASE_API_REGEX + PRODUCT_URL;
    String PRODUCTS_EARLY_ACCESS_PARAM = "earlyAccess";
    String PRODUCTS_CATEGORY_PARAM = "category";
    String PRODUCTS_TYPE_PARAM = "type";
    String PRODUCTS_WITH_PARAMS_REGEX = PRODUCTS_URL_REGEX + ".*[?&]earlyAccess=false.*";
    String PRODUCT_WITH_NAME_PATH_VARIABLE = PRODUCTS_URL + "/{name:[A-z\\s\\d-]+}";
    String ACCESS_CONTROL_URL = BASE_API_URL + "/auth";
    String LOGIN_URL = ACCESS_CONTROL_URL + "/login";
    String REGISTER_URL = "/register";
    String FULL_REGISTER_URL = ACCESS_CONTROL_URL + REGISTER_URL;
    String REFRESH_TOKEN_URL = "/refreshToken";
    String FULL_REFRESH_TOKEN_URL = ACCESS_CONTROL_URL + REFRESH_TOKEN_URL;
    String CREATE_CUSTOMER_URL = USERS_URL + "/role/" + ROLE_CUSTOMER;
    String CARTS_URL = BASE_API_URL + "/cart";
    String CLEAR_CART_URL = "/clear";
    String ALL_CART_URLS_MATCHER = CARTS_URL + ALL_MATCHER;
    String FAVORITES_URL = BASE_API_URL + "/favorites";
    String ADD_URL = "/add";
    String REMOVE_URL = "/remove";
    String PURCHASE_URL = "/purchase";
    String ALL_FAVORITES_URLS_MATCHER = FAVORITES_URL + ALL_MATCHER;
    String PAGE_PARAM = "page";
    String SIZE_PARAM = "size";
}
