package com.ritesh.clique.Constant;

import android.content.SharedPreferences;


public class Appconstant {

    /*http://androidtutorialsrkt.blogspot.in/2014/12/sharedpreferences-example-in-android-in.html*/
    /*http://androidtutorialsrkt.blogspot.in/2014/12/sharedpreferences-example-in-android-in.html*/
    /*http://androidtutorialsrkt.blogspot.in/2014/12/sharedpreferences-example-in-android-in.html*/

    public static SharedPreferences mPrefs = null;
    public static final String islogin = "islogin";                // for saving the login status
    public static final int MODE = 0;                              //sharedpreference mode set to private
    public static SharedPreferences.Editor editor;                // editor
    public static SharedPreferences sh;             // sharedpreference variable
    public static String str_login_test;                         // for checking the value of islogin in splash screen
    public static String answer;                                 // for checkint the internet type WIFI/MOBILE
    public static final String MyPREFERENCES = "mypref";

}