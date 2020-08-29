package io.github.blackfishlabs.forza.core.helper;

public final class ResourcePaths {

    ///////////////////////////////////////////////////////////////
    // ROOT PATH
    ///////////////////////////////////////////////////////////////
    public static final String ALL = "/**";
    private static final String API_PATH = "/api";
    private static final String API_PATH_V1 = API_PATH + "/v1";
    public static final String PUBLIC_PATH = "/public";

    ///////////////////////////////////////////////////////////////
    // PUBLIC PATHS
    ///////////////////////////////////////////////////////////////
    public static final String LOGIN_PATH = PUBLIC_PATH + "/login";
    public static final String LOGOUT_PATH = PUBLIC_PATH + "/logout";

    ///////////////////////////////////////////////////////////////
    // APPLICATION PATHS
    ///////////////////////////////////////////////////////////////
    // CRUD
    public static final String USER_PATH = API_PATH_V1 + "/user";
    public static final String PERMISSION_PATH = API_PATH_V1 + "/permission";
    public static final String COMPANY_PATH = API_PATH_V1 + "/company";
    public static final String ADDRESS_PATH = API_PATH_V1 + "/address";
    public static final String STATE_PATH = API_PATH_V1 + "/state";
    public static final String CITY_PATH = API_PATH_V1 + "/city";
    public static final String CONTACT_PATH = API_PATH_V1 + "/contact";
    public static final String PHONE_PATH = API_PATH_V1 + "/phone";

    // SALESMAN
    public static final String SALESMAN_PATH = API_PATH_V1 + "/salesman";
    // SYNC
    public static final String SERVER_PATH = API_PATH_V1 + "/server";
    // PAYMENT
    public static final String PAYMENT_PATH = API_PATH_V1 + "/payment";
    // PRODUCT
    public static final String PRODUCT_PATH = API_PATH_V1 + "/product";
    // PRICE TABLE
    public static final String PRICE_TABLE_PATH = API_PATH_V1 + "/pricetable";
    // CUSTOMER
    public static final String CUSTOMER_PATH = API_PATH_V1 + "/customer";
    // ORDER
    public static final String ORDER_PATH = API_PATH_V1 + "/order";
    // DASHBOARD
    public static final String DASHBOARD_PATH = API_PATH_V1 + "/dashboard";
    // VEHICLE
    public static final String VEHICLE_PATH = API_PATH_V1 + "/vehicle";
    // ROUTE
    public static final String ROUTE_PATH = API_PATH_V1 + "/route";
    // CHARGE
    public static final String CHARGE_PATH = API_PATH_V1 + "/charge";

    private ResourcePaths() {
    }
}

