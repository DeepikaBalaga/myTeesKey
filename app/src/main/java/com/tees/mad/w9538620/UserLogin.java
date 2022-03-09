package com.tees.mad.w9538620;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserLogin extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.pass)
    TextInputEditText pass;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private boolean verifyOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedpreferences = getSharedPreferences("com.tees.mad.w9538620", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        verifyOwner = getIntent().getBooleanExtra("verifyOwner", false);
        Log.d(TAG, "verifyOwner = " + verifyOwner);

        String title = (verifyOwner ? "Owner" : "Listing Agent") + " - Login";
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(title);

        Objects.requireNonNull(actionBar).setTitle(title);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    public void login(View view) {
        if (verifyInputs()) {
            loginUser();
        }
    }


    private boolean verifyInputs() {
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your email ID.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(email.getText().toString())) {
            Toast.makeText(this, "Please enter valid email ID.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void loginUser() {
        Utils.showDialog(UserLogin.this);

        String mail, pwd;
        mail = email.getText().toString();
        pwd = pass.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(UserLogin.this);
        String url = "https://ehx4lj0yi4.execute-api.us-east-1.amazonaws.com/v1/userlogin-get?emailId=" + mail
                + "&password=" + pwd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("LoginActivity", "success " + response);
                if (response.contains("Username doesn't exist")) {
                    Toast.makeText(UserLogin.this, "User doesn't exist.\nPlease register first.", Toast.LENGTH_SHORT).show();
                } else if (response.contains("User name password doesnot match")) {
                    Toast.makeText(UserLogin.this, "User name password does not match.\nPlease try again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserLogin.this, "Login success", Toast.LENGTH_SHORT).show();
                    saveUserData(response);
                    startActivity(new Intent(UserLogin.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("LoginActivity", "error " + error);

                if (error.networkResponse.statusCode == 404) {
                    Toast.makeText(UserLogin.this, "User doesn't exist.\nPlease register first.", Toast.LENGTH_SHORT).show();
                } else if (error.networkResponse.statusCode == 401) {
                    Toast.makeText(UserLogin.this, "User name password does not match.\nPlease try again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserLogin.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    private void saveUserData(String response) {
        try {
            JSONObject sys = new JSONObject(response);
            String username = sys.getString("username");
            String emailId = sys.getString("emailId");

            String verifyOwnerOrAgent = sys.getString("verifyOwner");
            String address = "", lockdetails = "", agentCode = "";
            if (verifyOwner) {
                address = sys.getString("address");
                lockdetails = sys.getString("lockdetails");
            } else {
                agentCode = sys.getString("agentCode");
            }

            editor.putString("username", username);
            editor.putString("emailId", emailId);
            editor.putString("address", address);
            editor.putString("lockdetails", lockdetails);
            editor.putBoolean("verifyOwner", !verifyOwnerOrAgent.equals("0"));
            editor.putString("agentCode", agentCode);
            editor.putBoolean("isLoggedIn", true);
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}