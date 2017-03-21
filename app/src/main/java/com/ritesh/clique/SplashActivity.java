package com.ritesh.clique;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ritesh.clique.Constant.Appconstant;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // here initializing the shared preference
        Appconstant.sh = getSharedPreferences("myprefe", 0);
        Appconstant.editor = Appconstant.sh.edit();

        // check here if user is login or not
        Appconstant.str_login_test = Appconstant.sh.getString("LoginSuccessfully", null);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        // METHOD 1

        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                /*
				 * if user login test is true on oncreate then redirect the user
				 * to result page
				 */

                if (Appconstant.str_login_test != null
                        && !Appconstant.str_login_test.toString().trim().equals("")) {

                    try {
                        // Thread will sleep for 2 seconds
                        sleep(2 * 1000);

                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                    Intent send = new Intent(getApplicationContext(),
                            CameraActivity.class);
                    startActivity(send);
                }
				/*
				 * if user login test is false on oncreate then redirect the
				 * user to login & registration page
				 */
                else {
                    try {
                        // Thread will sleep for 3 seconds
                        sleep(3 * 1000);

                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getApplicationContext(), LoginSignupSelectAcitivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();

                    } catch (Exception e) {

                    }
                }
            }
        };

        // start thread
        background.start();

    }
}
