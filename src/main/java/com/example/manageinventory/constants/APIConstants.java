package com.example.manageinventory.constants;

public class APIConstants {
    public static final String ROOT_PATH = "/api/1/ims";

    //User Authentication
    public class UserAuthentication {
        public static final String USER_AUTHENTICATION_LOGIN = ROOT_PATH+"/login";
        public static final String USER_AUTHENTICATION_LOGOUT = ROOT_PATH+"/logout";
    }

    //Product APIs
    public class Product {
        public static final String PRODUCT_ROOT = ROOT_PATH+"/products";
        //public static final String PRODUCT_REGISTER = "/register";
        public static final String PRODUCT_GET_UPDATE_DELETE = "/{id}";

        public static final String PRODUCT_LOCATIONS = "{id}/locations";

        public static final String PRODUCT_FAST_MOVING = "/fastmoving";
        public static final String PRODUCT_SLOW_MOVING = "/slowmoving";
        public static final String PRODUCT_CRITICAL_STOCK = "/criticalstock";
    }

    public class Location {
        public static final String LOCATION_ROOT = ROOT_PATH+"/locations";
        public static final String LOCATION_GET_UPDATE_DELETE = "/{id}";
        public static final String LOCATION_PRODUCTS = "{id}/products";
        public static final String LOCATION_PLACE_PRODUCTS = "{id}/place-products";
        public static final String LOCATION_REMOVE_PRODUCTS = "{id}/remove-products";
        public static final String LOCATION_INDENTS = "{id}/indents";
    }

    public class Indent {
        public static final String INDENT_ROOT = ROOT_PATH+"/indents";
        public static final String INDENT_GET_UPDATE_DELETE = "/{id}";
        public static final String INDENT_LOCATIONS = "{id}/locations";
        public static final String INDENT_RETURN_CREATE = "{id}/returns";

    }

    public class User {
        public static final String USER_ROOT = ROOT_PATH+"/users";
        public static final String USER_GET_UPDATE_DELETE = "/{id}";
        public static final String USER_RESET_PASSWORD = "{id}/reset";
    }

    public class Manufacturer {
        public static final String MANUFACTURER_ROOT = ROOT_PATH+"/manufacturers";
        public static final String MANUFACTURER_GET_UPDATE_DELETE = "/{id}";
    }

}
