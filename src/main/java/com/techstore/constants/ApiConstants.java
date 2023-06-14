package com.techstore.constants;

public interface ApiConstants {
    String PAGE_PARAM = "page";
    String SIZE_PARAM = "size";
    String TOKEN_PARAM = "token";
    String PRODUCTS_EARLY_ACCESS_PARAM = "earlyAccess";
    String PRODUCTS_CATEGORY_PARAM = "category";
    String PRODUCTS_TYPE_PARAM = "type";
    String PRODUCTS_SEARCH_KEYWORD_PARAM = "keyword";
    String ORDER_START_DATE_PARAM = "startDate";
    String ORDER_END_DATE_PARAM = "endDate";

    String PAGE_PARAM_DEFAULT_VALUE = "0";
    String SIZE_PARAM_DEFAULT_VALUE = "10";

    String ALL_MATCHER = "/**";
    String BASE_API_URL = "/api/v1";
    String BASE_API_URL_MATCHER = BASE_API_URL + ALL_MATCHER;
    String BASE_API_REGEX = "\\/api\\/v1\\";

    String PRODUCT_URL = "/product";
    String PRODUCTS_URL = BASE_API_URL + PRODUCT_URL;
    String PRODUCTS_URL_REGEX = BASE_API_REGEX + PRODUCT_URL;
    String PRODUCTS_WITH_PARAMS_REGEX = PRODUCTS_URL_REGEX + ".*[?&]earlyAccess=false.*";
    String PRODUCT_WITH_NAME_PATH_VARIABLE = PRODUCTS_URL + "/{name:[A-z\\s\\d-]+}";
    String SEARCH_URL = "/search";
    String SEARCH_QUERY_URL = SEARCH_URL + "/query";
    String USERS_URL = BASE_API_URL + "/user";
    String ACCESS_CONTROL_URL = BASE_API_URL + "/auth";
    String LOGIN_URL = ACCESS_CONTROL_URL + "/login";
    String REGISTER_URL = "/register";
    String FULL_REGISTER_URL = ACCESS_CONTROL_URL + REGISTER_URL;
    String CONFIRM_URL = "/confirm";
    String CONFIRM_REGISTER_URL = REGISTER_URL + CONFIRM_URL;
    String FULL_CONFIRM_REGISTER_URL = ACCESS_CONTROL_URL + CONFIRM_REGISTER_URL;
    String FORGOT_PASSWORD_URL = "/forgotPassword";
    String FULL_FORGOT_PASSWORD_URL = USERS_URL + FORGOT_PASSWORD_URL;
    String REFRESH_TOKEN_URL = "/refreshToken";
    String RESET_PASSWORD_URL = "/resetPassword";
    String FULL_RESET_PASSWORD_URL = ACCESS_CONTROL_URL + RESET_PASSWORD_URL;
    String FULL_REFRESH_TOKEN_URL = ACCESS_CONTROL_URL + REFRESH_TOKEN_URL;
    String CARTS_URL = BASE_API_URL + "/cart";
    String ALL_CART_URLS_MATCHER = CARTS_URL + ALL_MATCHER;
    String FAVORITES_URL = BASE_API_URL + "/favorites";
    String ALL_FAVORITES_URLS_MATCHER = FAVORITES_URL + ALL_MATCHER;
    String ADD_URL = "/add";
    String REMOVE_URL = "/remove";
    String PURCHASE_URL = "/purchase";
    String ORDER_URL = "/order";
    String ORDERS_URL = BASE_API_URL + ORDER_URL;
    String ALL_ORDERS_URLS_MATCHER = ORDERS_URL + ALL_MATCHER;
    String DELIVER_URL = "/deliver";
    String RETURN_URL = "/return";
    String CANCEL_URL = "/cancel";
    String FINALIZE_URL = "/finalize";
    String GET_URL = "/get";
    String ALL_URL = "/all";
    String VALIDATE_URL = "/validate";
    String USER_GET_URL = USERS_URL + GET_URL;
}
