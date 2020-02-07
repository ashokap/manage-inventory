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
            public static final String PRODUCT_REGISTER = "/register";
            public static final String PRODUCT_GET = "/{sku}";
            public static final String PRODUCT_LIST = "/list";
            public static final String PRODUCT_UPDATE = "{sku}/edit";
            public static final String PRODUCT_DELETE = "{sku}/delete";
        }


}
