package com.ritesh.clique;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ritesh.clique.Constant.Appconstant;
import com.ritesh.clique.Constant.HttpUrlPath;
import com.ritesh.clique.Constant.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 23/9/16.
 */

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnlogin;
    @BindView(R.id.btn_facebook)
    Button btnfacebook;
    @BindView(R.id.btn_instagram)
    Button btninstagram;

    @BindView(R.id.et_emailid)
    EditText edtEmailid;
    @BindView(R.id.et_password)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
        /* fetching the data from shared preference in order to make user login */
		/* data are saved in application through SplashActivity */
		/* only name and password is sufficient to make login */

        btnlogin.setOnClickListener(new View.OnClickListener() {
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

                        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
                        View promptView = layoutInflater.inflate(R.layout.activity_wificheck_customalert, null);
                        final AlertDialog alertD = new AlertDialog.Builder(LoginActivity.this).create();
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
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public class Loginjsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW LOGIN WEB SERVICE IS RUNNING *******", "YES");
        }

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
                    Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);
                    Appconstant.editor = Appconstant.sh.edit();
                    Appconstant.editor.putString("LoginSuccessfully", Appconstant.islogin);
                    Appconstant.editor.putString("id", userid);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.putString("email", str_UserEmail );
                    Appconstant.editor.putString("password", str_Password );

                    Log.e("********* SHARED PREFERENCE USERID *********", "email :" +  "" + str_UserEmail);


                    Log.e("********* SHARED PREFERENCE PASSWORD *********", "password" +  "" + str_Password);

                    Log.e("********* GET USER ID *********", "" + userid + "\nSuccess USERNAME PASSWORD MATCH");
                    Appconstant.editor.commit();


                    Log.e("********* Login Detail SEND *********", "Success USERNAME PASSWORD MATCH GOTO MAINPAGE");

                    Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LoginActivity.this, CameraActivity.class);
                    in.putExtra("EXIT", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginActivity.this.startActivity(in);
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
}
