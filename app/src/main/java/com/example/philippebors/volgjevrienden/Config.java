package com.example.philippebors.volgjevrienden;


/* This class contains some urls and tags to be used in requests and JSON strings */
class Config {
    /* The URLS used */
    public static final String GETFRIENDS_URL = "http://nolden.biz/Android/getFriends.php";
    public static final String LOGINACCOUNT_URL = "http://nolden.biz/Android/loginAccount.php";
    public static final String LOGIN_STATUS_URL = "http://nolden.biz/Android/loginStatus.txt";
    public static final String REGISTER_URL = "http://nolden.biz/Android/registerAccount.php";
    public static final String FRIEND_URL = "http://nolden.biz/Android/addFriendship.php";
    public static final String FRIENDS_LOCATIONS_URL = "http://nolden.biz/Android/getLocations.txt";
    public static final String FRIENDS_LOCATIONS_SEND_URL = "http://nolden.biz/Android/getLocations.php";
    public static final String CHECK_FRIENDS_URL = "http://nolden.biz/Android/getFriends.txt";
    public static final String UPDATE_LOCATIONS_URL = "http://nolden.biz/Android/updateLocation.php";

    public static String MY_NUMBER = "";


    /* Tags used in JSON strings */
    public static final String TAG_NAME = "NAME";
    public static final String TAG_LONGITUDE = "LONGITUDE";
    public static final String TAG_LATITUDE = "LATITUDE";

    /* Name of the JSON array */
    public static final String JSON_ARRAY = "result";
}
