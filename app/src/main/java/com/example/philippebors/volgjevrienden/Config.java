package com.example.philippebors.volgjevrienden;


/* This class contains some urls and tags to be used in requests and JSON strings */
class Config {
    /* PHP URLS */
    public static final String GET_FRIENDS_URL = "http://nolden.biz/Android/getFriends.php";
    public static final String LOGIN_ACCOUNT_URL = "http://nolden.biz/Android/loginAccount.php";
    public static final String REGISTER_URL = "http://nolden.biz/Android/registerAccount.php";
    public static final String FRIEND_URL = "http://nolden.biz/Android/addFriendship.php";
    public static final String FRIENDS_LOCATIONS_SEND_URL = "http://nolden.biz/Android/getLocations.php";
    public static final String UPDATE_LOCATIONS_URL = "http://nolden.biz/Android/updateLocation.php";

    /* TXT URLS */
    public static final String LOGIN_STATUS_URL = "http://nolden.biz/Android/loginStatus.txt";
    public static final String FRIENDS_LOCATIONS_URL = "http://nolden.biz/Android/getLocations.txt";
    public static final String CHECK_FRIENDS_URL = "http://nolden.biz/Android/getFriends.txt";
    public static final String REGISTER_STATUS_URL = "http://nolden.biz/Android/regStatus.txt";

    /* Number of the user that logged in */
    public static String MY_NUMBER = "";


    /* Tags used in JSON strings */
    public static final String TAG_NAME = "NAME";
    public static final String TAG_LONGITUDE = "LONGITUDE";
    public static final String TAG_LATITUDE = "LATITUDE";

    /* Name of the JSON array */
    public static final String JSON_ARRAY = "result";
}