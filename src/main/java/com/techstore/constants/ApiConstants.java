package com.techstore.constants;

public interface ApiConstants {
    String BASE_API_URL = "/api/v1";
    String USERS_URL = BASE_API_URL + "/user";
    String PRODUCTS_URL = BASE_API_URL + "/product";
    String LOGIN_URL = BASE_API_URL + "/login";
    String REFRESH_TOKEN_URL = "/refresh_token";
    String FULL_REFRESH_TOKEN_URL = USERS_URL + REFRESH_TOKEN_URL;
}
