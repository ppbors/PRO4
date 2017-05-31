package com.example.philippebors.volgjevrienden;


/* This class contains some urls and tags to be used in requests and JSON strings */
public class Config {
    /* The URLS used */
    public static final String DATA_URL = "http://nolden.biz/Android/json.php";
    public static final String DATA_URL2 = "http://nolden.biz/Android/json2.php";
    public static final String DATA_PARSE_URL = "http://nolden.biz/Android/loginAccount.php";
    public static final String LOGIN_STATUS_URL = "http://nolden.biz/Android/loginStatus.txt";
    public static final String REGISTER_URL = "http://nolden.biz/Android/registerAccount.php";

    public static String MY_NUMBER = "";


    /* Tags used in JSON strings */
    public static final String TAG_NAME = "NAME";
    public static final String TAG_NUMBER = "NUMBER";
    public static final String TAG_LONGITUDE = "LONGITUDE";
    public static final String TAG_LATITUDE = "LATITUDE";

    /* Name of the JSON array */
    public static final String JSON_ARRAY = "result";
}
