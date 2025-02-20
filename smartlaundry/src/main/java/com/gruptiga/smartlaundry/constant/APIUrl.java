package com.gruptiga.smartlaundry.constant;

public class APIUrl {
        public static final String BASE_API = "/api/v1";
        public static final String CUSTOMER = BASE_API + "/customers";
        public static final String PATH_VAR_CUSTOMER_ID = "/{customerId}";

        //Update
        public static final String UPDATE = "/update";

        // Account
        public static final String ACCOUNT = BASE_API + "/accounts";
        public static final String CUSTOMER_ACCOUNT = "/customers";
        public static final String SERVICETYPE_ACCOUNT =  "/servicestype";
        public static final String TRANSACTION_ACCOUNT = "/transaction";
        public static final String BYSTATUSPembayaran_ACCOUNT = TRANSACTION_ACCOUNT+ "/statusPembayaran";
        public static final String BYSTATUS_ACCOUNT = TRANSACTION_ACCOUNT+ "/status";
        public static final String BYSTATUS_Pembayaran_ACCOUNT = TRANSACTION_ACCOUNT+ "/status_pembayaran";
        public static final String PATH_VAR_ACCOUNT_ID = "/{accountId}";
        public static final String ACCOUNT_BY_EMAIL = "/by-email";
        public static final String BY_DATE_ACCOUNT = "/by-date-and-account";
        public static final String UPDATE_ACCOUNTS = UPDATE + "/accounts";

        // Service Type
        public static final String SERVICETYPE = BASE_API + "/servicestype";
        public static final String PATH_VAR_SERVICETYPE_ID = "/{serviceTypeId}";

        // Service
        public static final String SERVICE = BASE_API + "/services";
        public static final String SERVICE_ACCOUNT =  "/services";
        public static final String PATH_VAR_SERVICE_ID = "/{serviceId}";

        // Type
        public static final String TYPE = BASE_API + "/types";
        public static final String TYPE_ACCOUNT =  "/types";
        public static final String PATH_VAR_TYPE_ID = "/{typesId}";

        // Transaction
        public static final String TRANSACTION = BASE_API + "/transactions";
        public static final String PATH_VAR_TRANSACTION_ID = "/{trxId}";

        // Auth
        public static final String AUTH_API = BASE_API + "/auth";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String LOGOUT = "/logout";

        // User
        public static final String USER = BASE_API + "/security";
        public static final String USER_CONTEXT = "/currentuser";

        //Midtrans
        public static final String MIDTRANS = BASE_API + "/midtrans";

    public static final String IMAGE = BASE_API + "/images";
        public static final String PATH_VAR_IMAGE_ID = "/{imageId}";
}


