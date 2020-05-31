package com.example.cshare.utils;

import com.example.cshare.BuildConfig;

public class Constants {

    //public static final String BASE_URL_API = "http://10.59.33.8:8000/api/v1/";
    //public static final String BASE_URL_API = "http://192.168.1.29:8000/api/v1/";
    public static final String SERVER_URL = BuildConfig.SERVER_URL;

    public static final String TAG = "csharetag";

    /**
     * Page size for pagination
     */
    public static final int PAGE_SIZE = 12;
    /**
     * Fetch data from the first page which is 1
     */
    public static final int FIRST_PAGE = 1;



    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    // Different product statuses
    public static final String AVAILABLE = "Available";
    public static final String COLLECTED = "Collected";
    public static final String DELIVERED = "Delivered";

    // State of the productDialogFragment
    public static final String SHARED = "shared";
    public static final String ORDER = "order";
    public static final String INCART = "inCart";
    public static final String ARCHIVED = "archived";
}
