package com.ritesh.clique;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ritesh.clique.Constant.Appconstant;
import com.ritesh.clique.Constant.HttpUrlPath;
import com.ritesh.clique.Constant.Utils;
import com.sdsmdg.tastytoast.TastyToast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 23/9/16.
 */
public class LoginSignupSelectAcitivity extends AppCompatActivity {


    @BindView(R.id.rl_pleaseselect)
    RelativeLayout PleaseSelectScreenLayout;
    @BindView(R.id.rl_login_main)
    RelativeLayout LoginScreenLayout;
    @BindView(R.id.rl_signup_main)
    RelativeLayout SignUpScreenLayout;

    /*select screen data (Start)*/
    @BindView(R.id.btn_login)
    Button btnlogin;
    @BindView(R.id.btn_signuppp)
    Button btnsignup;
    /*select screen data (End)*/


    /*login screen data (Start)*/
    @BindView(R.id.btn_loginn)
    Button btnloginn;
    @BindView(R.id.btn_facebook)
    Button btnfacebook;
    @BindView(R.id.btn_instagram)
    Button btninstagram;
    @BindView(R.id.et_emailiddd)
    EditText edtEmailid;
    @BindView(R.id.et_passworddd)
    EditText edtPassword;

    String
            result = " ",
            str_UserEmail = " ",
            str_Password = " ",
            userid = " ",
            useremail = " ",
            userpassword = " ",
            userfname = " ",
            userlname = " ",
            usermobile = " ";

    /*login screen data (End)*/


    /*Signup screen data (Start)*/
    @BindView(R.id.btn_alreadyaccount)
    Button btnalreadyaccount;
    @BindView(R.id.btn_signupp)
    Button btnsignupp;

    @BindView(R.id.et_fname)
    EditText edt_fname;
    @BindView(R.id.et_lname)
    EditText edt_lname;
    @BindView(R.id.et_emailidd)
    EditText edt_emailid;
    @BindView(R.id.et_mobilenumber)
    EditText edt_mobnumber;
    @BindView(R.id.et_passwordd)
    EditText edt_password;
    @BindView(R.id.et_retypepassword)
    EditText edt_repassword;

    boolean iserror = false;

    String str_fname = " ",
            str_lname = "",
            resultt = " ",
            str_email = " ",
            str_mobile = " ",
            str_password = " ",
            str_repassword = " ",
            firstname = "",
            lastname = "",
            emailid = "",
            error1 = "",
            islogin = "",
            useridd = "",
            savedFName = "",
            savedLName = "",
            savedEmailID = "",
            savedMobNumber = "",
            passwordpattern = "";

    Context context;

    /*Signup screen data (End)*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_select);
        ButterKnife.bind(this);
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        /* fetching the data from shared preference in order to make user login */
        /* data are saved in application through SplashActivity */
        /* only name and password is sufficient to make login */

        /* **********  ********* **********  ********* select screen data (Start) **********  ********* **********  ********* */
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginScreenLayout.setVisibility(View.VISIBLE);
                PleaseSelectScreenLayout.setVisibility(View.GONE);
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpScreenLayout.setVisibility(View.VISIBLE);
                PleaseSelectScreenLayout.setVisibility(View.GONE);
            }
        });
        /* **********  ********* **********  ********* select screen data (End) **********  ********* **********  ********* */




        /* **********  ********* **********  ********* login screen data (Start) **********  ********* **********  ********* */
        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        btnloginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean iserror = false;

                str_UserEmail = edtEmailid.getText().toString().trim();
                str_Password = edtPassword.getText().toString().trim();

        /* make edittext condition for empty, input etc match */

                if (str_UserEmail.equals("")) {
                    iserror = true;
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edtEmailid);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Please enter email address.", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(str_UserEmail)) {
                    iserror = true;
                    edtEmailid.requestFocus();

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(edtEmailid);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Enter valid email.", Toast.LENGTH_SHORT).show();

                } else if (str_Password.equals("")) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edtPassword);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_LONG).show();
                }
                if (!iserror) {

                    if (Utils.isConnected(getApplicationContext())) {
                        Loginjsontask task = new Loginjsontask();
                        task.execute();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Please Cheeck network conection..", Toast.LENGTH_SHORT).show();
                        //  TODO Auto-generated method stub

                        LayoutInflater layoutInflater = LayoutInflater.from(LoginSignupSelectAcitivity.this);
                        View promptView = layoutInflater.inflate(R.layout.activity_wificheck_customalert, null);
                        final AlertDialog alertD = new AlertDialog.Builder(LoginSignupSelectAcitivity.this).create();
            /*prevent alert dialog from outside click and back button click (Star)*/
                        alertD.setCanceledOnTouchOutside(false);
                        alertD.setCancelable(false);
            /*prevent alert dialog from outside click and back button click (Star)*/
                        Button Tryagain = (Button) promptView.findViewById(R.id.btn_tryagain);
                        Tryagain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getIntent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });
                        alertD.setView(promptView);
                        alertD.show();

                    }
                }
            }
        });

        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intentresume = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentresume);*/
            }
        });

        btninstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intentresume = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentresume);*/
            }
        });

        /* **********  ********* **********  ********* login screen data (End) **********  ********* **********  ********* */










        /* **********  ********* **********  ********* Signup screen data (Start) **********  ********* **********  ********* */
        context = getApplicationContext();

        if (savedInstanceState != null) {
            savedInstanceState.getString(savedFName);
            savedInstanceState.getString(savedLName);
            savedInstanceState.getString(savedEmailID);
            savedInstanceState.getString(savedMobNumber);
            edt_fname.setText(savedFName);
            edt_lname.setText(savedLName);
            edt_emailid.setText(savedEmailID);
            edt_mobnumber.setText(savedMobNumber);
        }/* else {
            edt_fname.setText(" ");
            edt_lname.setText(" ");
            edt_emailid.setText(" ");
            edt_mobnumber.setText(" ");
        }*/

        btnsignupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Signup Click", Toast.LENGTH_SHORT).show();
                boolean iserror = false;
                str_fname = edt_fname.getText().toString().trim();
                str_lname = edt_lname.getText().toString().trim();
                str_email = edt_emailid.getText().toString().trim();
                str_mobile = edt_mobnumber.getText().toString().trim();
                str_password = edt_password.getText().toString().trim();
                str_repassword = edt_repassword.getText().toString().trim();


                Log.e(" ********* str_name ********* ", "" + str_fname);
                Log.e(" ********* str_address ********* ", "" + str_lname);
                Log.e(" ********* str_email ********* ", "" + str_email);
                Log.e(" ********* str_mobile ********* ", "" + str_mobile);
                Log.e(" ********* str_password ********* ", "" + str_password);

                if (str_fname.equals("") || str_fname.isEmpty() || str_fname.length() == 0 || str_fname == null) {
                    iserror = true;

                    /**************** Start Animation ****************/
        /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edt_fname);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Please enter your First Name",
                            Toast.LENGTH_SHORT).show();
                } else if (str_lname.equals("")) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edt_lname);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please enter your Last Name", Toast.LENGTH_SHORT).show();
                } else if (str_email.equals("")) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edt_emailid);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();

                } else if (!isValidEmail(str_email)) {
                    iserror = true;
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(edt_emailid);
                    /**************** End Animation ****************/
                    Toast.makeText(getApplicationContext(),
                            "Please enter valid Email address.", Toast.LENGTH_SHORT).show();

                } else if (str_mobile.equals("")) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edt_mobnumber);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();

                } else if (str_password.equals("")) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(edt_password);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();

                } else if (str_password.length() < 5) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(edt_password);
                    /**************** End Animation ****************/

                    Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.", Toast.LENGTH_SHORT).show();
                } else if (!str_repassword.equals(str_password) || str_repassword.length() < 5 || str_password.length() < 5) {
                    iserror = true;

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(edt_password);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(edt_repassword);
                    /**************** End Animation ****************/
                    Toast.makeText(getApplicationContext(),
                            "oopsss....\n Password not Match Please try again", Toast.LENGTH_SHORT).show();
                }

                if (!iserror) {
                    if (Utils.isConnected(getApplicationContext())) {
                        Registrationjsontask task = new Registrationjsontask();
                        task.execute();
                    } else {
                        // Toast.makeText(getApplicationContext(), "Please Cheeck network conection..", Toast.LENGTH_SHORT).show();

                        LayoutInflater layoutInflater = LayoutInflater.from(LoginSignupSelectAcitivity.this);
                        View promptView = layoutInflater.inflate(R.layout.activity_wificheck_customalert, null);
                        final AlertDialog alertD = new AlertDialog.Builder(LoginSignupSelectAcitivity.this).create();
            /*prevent alert dialog from outside click and back button click (Star)*/
                        alertD.setCanceledOnTouchOutside(false);
                        alertD.setCancelable(false);
            /*prevent alert dialog from outside click and back button click (Star)*/
                        Button Tryagain = (Button) promptView.findViewById(R.id.btn_tryagain);
                        Tryagain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = getIntent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        });
                        alertD.setView(promptView);
                        alertD.show();

                    }
                }

            }
        });

        btnalreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intentresume = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intentresume);*/
                LoginScreenLayout.setVisibility(View.VISIBLE);
                PleaseSelectScreenLayout.setVisibility(View.GONE);
            }
        });

        /* **********  ********* **********  ********* Signup screen data (End) **********  ********* **********  ********* */



        /* **********  ********* **********  ********* Main screen data (Start) **********  ********* **********  ********* */
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "You have successfully logout",
//                        Toast.LENGTH_LONG).show();
//                Appconstant.editor.clear();
//                Appconstant.editor.commit();
//
//                Intent sendToLoginandRegistration = new Intent(getApplicationContext(),
//                        LoginSignupSelectAcitivity.class);
//
//                startActivity(sendToLoginandRegistration);
//            }
//        });
        /* **********  ********* **********  ********* Main screen data (Start) **********  ********* **********  ********* */

    }

    /* **********  ********* **********  ********* login screen data (Start) **********  ********* **********  ********* */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    /* **********  ********* **********  ********* login screen data (End) **********  ********* **********  ********* */

    /* **********  ********* **********  ********* login screen data (End) **********  ********* **********  ********* */
    public class Loginjsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW LOGIN WEB SERVICE IS RUNNING *******", "YES");
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            Log.e("******* NOW LOGIN BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPath + "login?");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("email", str_UserEmail));
                nameValuePairs.add(new BasicNameValuePair("password", str_Password));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("****************object*******************=", "" + object);
                System.out.println("****************object*******************=" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successful")) {
                    userid = jobect.getString("id");
                    Log.e("******* ID RETURN BY SERVER *******", "" + userid);

                    useremail = jobect.getString("email");
                    Log.e("******* EMAIL RETURN BY SERVER *******", "" + useremail);

                    userpassword = jobect.getString("password");
                    Log.e("******* PASSWORD RETURN BY SERVER *******", "" + userpassword);
                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            if (!iserror) {
                if (result.equalsIgnoreCase("successful")) {

                    //  loginprogressbar.setVisibility(View.GONE);

//                    Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
//                    Appconstant.editor = Appconstant.sh.edit();
                    Appconstant.editor.putString("LoginSuccessfully", Appconstant.islogin);
//                    Appconstant.editor.putString("id", userid);
//                    Appconstant.editor.putString("loginTest", "true");
//                    Appconstant.editor.putString("email", str_UserEmail );
//                    Appconstant.editor.putString("password", str_Password );

                    Log.e("********* SHARED PREFERENCE USERID *********", "email" + "" + str_UserEmail);


                    Log.e("********* SHARED PREFERENCE PASSWORD *********", "password" + "" + str_Password);

                    Log.e("********* GET USER ID *********", "" + userid + "\nSuccess USERNAME PASSWORD MATCH");
                    Appconstant.editor.commit();


                    Log.e("********* Login Detail SEND *********", "Success USERNAME PASSWORD MATCH GOTO MAINPAGE");

                    Toast.makeText(LoginSignupSelectAcitivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LoginSignupSelectAcitivity.this, CameraActivity.class);
                    in.putExtra("EXIT", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginSignupSelectAcitivity.this.startActivity(in);
                    finish();

                } else {

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(edtEmailid);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(edtPassword);
                    /**************** End Animation ****************/

                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");

                    Toast.makeText(getApplicationContext(), "Username or Password is wrong",
                            Toast.LENGTH_SHORT).show();
//									// TODO Auto-generated method stub
                }
            } else {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();
//								// TODO Auto-generated method stub
            }
        }
    }
     /* **********  ********* **********  ********* login screen data (End) **********  ********* **********  ********* */

    /* **********  ********* **********  ********* Signup screen data (Start) **********  ********* **********  ********* */
    public class Registrationjsontask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                str_fname = URLEncoder.encode(str_fname,
                        "UTF-8");
                str_lname = URLEncoder.encode(str_lname,
                        "UTF-8");
                str_mobile = URLEncoder.encode(str_mobile,
                        "UTF-8");
                str_email = URLEncoder.encode(str_email,
                        "UTF-8");
                str_password = URLEncoder.encode(str_password,
                        "UTF-8");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPath + "signup?");
            System.out.println("******************** object ********************="
                    + HttpUrlPath.urlPath + "signup?firstname=" + str_fname + "lastname=" + str_lname +
                    "&register_id=123" + "&mobile=" + str_mobile + "&email=" + str_email +
                    "&password=" + str_password);

            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        7);
                nameValuePairs.add(new BasicNameValuePair("firstname",
                        str_fname));
                nameValuePairs.add(new BasicNameValuePair("lastname",
                        str_lname));
                nameValuePairs.add(new BasicNameValuePair("email",
                        str_email));
                nameValuePairs.add(new BasicNameValuePair("password",
                        str_password));
                nameValuePairs.add(new BasicNameValuePair("mobile",
                        str_mobile));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                System.out.println("************ object holds our data ************ =" + object);
                Log.e("************ object holds our data ************ =", "" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successful")) {
                    firstname = jobect.getString("firstname");
                    lastname = jobect.getString("lastname");
                    emailid = jobect.getString("email");
                } else {
                    if (result.equalsIgnoreCase("unsuccessfully")) {
                        error1 = jobect.getString("error");
                    }
                }
            } catch (Exception e) {
                Log.e(" ******** Exception **********", "************ Exception ************" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            //if (!iserror == false)
            if (!iserror) {
                if (result.equalsIgnoreCase("successful")) {
                  /*  Appconstant.editorclique.putString("LoginSuccessfully", islogin);
                    Appconstant.editorclique.putString("id", userid);
                    Appconstant.editorclique.putString("email", emailid);
                    Appconstant.editorclique.commit();*/
                    TastyToast.makeText(getApplicationContext(), "Signup Successfully ", TastyToast.LENGTH_LONG,
                            TastyToast.SUCCESS);

                    Intent in = new Intent(LoginSignupSelectAcitivity.this, LoginActivity.class);
                    //in.putExtra("aa", "0");

                    //clear all previous running activity using FLAG_ACTIVITY_CLEAR_TOP
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginSignupSelectAcitivity.this.startActivity(in);
                    finish();

                } else {
                    TastyToast.makeText(getApplicationContext(), "User Already Exist with this Email\n" +
                                    " Please try again", TastyToast.LENGTH_LONG,
                            TastyToast.WARNING);

//                    Toast.makeText(getApplicationContext(), " User Already Exist with this Email\n Please try again ", Toast.LENGTH_SHORT).show();
                }
            } else {
                TastyToast.makeText(getApplicationContext(), "Oops!! Please check server connection", TastyToast.LENGTH_LONG,
                        TastyToast.ERROR);
//
            }
        }
    }
    /* **********  ********* **********  ********* Signup screen data (End) **********  ********* **********  ********* */


    @Override
    public void onBackPressed() {
        SignUpScreenLayout.setVisibility(View.GONE);
        LoginScreenLayout.setVisibility(View.GONE);
        PleaseSelectScreenLayout.setVisibility(View.VISIBLE);

    }


}
