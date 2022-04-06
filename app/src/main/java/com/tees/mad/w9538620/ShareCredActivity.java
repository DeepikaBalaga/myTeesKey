package com.tees.mad.w9538620;

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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareCredActivity extends AppCompatActivity {

    @BindView(R.id.layout_email)
    TextInputLayout layoutEmail;
    @BindView(R.id.layout_pass)
    TextInputLayout layoutPass;
    @BindView(R.id.layout_otp)
    TextInputLayout layoutOtp;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.pass)
    TextInputEditText pass;
    @BindView(R.id.otp)
    TextInputEditText otp;

    private boolean isCred;
    private String emailOwner,ownerName;

    public String testlockmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_cred);
        ButterKnife.bind(this);

        isCred = getIntent().getBooleanExtra("cred", false);
        emailOwner = getIntent().getStringExtra("email");
        ownerName = getIntent().getStringExtra("name");
        testlockmail = getIntent().getStringExtra("testlockmail");

        ActionBar actionBar = getSupportActionBar();

        String title = isCred ? "Share Lock Access" : "Share Lock OTP";
        Objects.requireNonNull(actionBar).setTitle(title);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        updateUi();
    }

    private void updateUi() {
        if (isCred) {
            layoutEmail.setVisibility(View.VISIBLE);
            layoutPass.setVisibility(View.VISIBLE);
            layoutOtp.setVisibility(View.GONE);
        } else {
            layoutEmail.setVisibility(View.GONE);
            layoutPass.setVisibility(View.GONE);
            layoutOtp.setVisibility(View.VISIBLE);
        }
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

    public void share(View view) {
        if (isCred) {
            if (email.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter lock email ID", Toast.LENGTH_SHORT).show();
                return;
            } else if (!UserRegistration.isEmailValid(email.getText().toString())) {
                Toast.makeText(this, "Please enter valid lock email ID", Toast.LENGTH_SHORT).show();
                return;
            } else if (pass.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter lock password", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (otp.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter lock OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        shareCredData();
    }

    private void shareCredData() {

        Utils.showDialog(ShareCredActivity.this);

        String mail, pwd, otpTx;
        mail = email.getText().toString();
        pwd = pass.getText().toString();
        otpTx = otp.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(ShareCredActivity.this);
        String url;
        if (isCred) {
            url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/shareownerlockcreds?email=" + emailOwner
                    + "&mailLock=" + mail + "&pwd=" + pwd+ "&owname=" + ownerName;
            SharedPreferences.Editor editor = getSharedPreferences("com.tees.mad.w9538620", MODE_PRIVATE).edit();
            editor.putString("templockmail", mail);
            editor.apply();
            //checked whether templockmail updated or not
//            SharedPreferences prefs = getSharedPreferences("com.tees.mad.w9538620", MODE_PRIVATE);
//            String templockmail = prefs.getString("templockmail", "mail");
//            Log.d("ShareCredActivity", "templockmail if: " + templockmail);

        } else {
            SharedPreferences prefs = getSharedPreferences("com.tees.mad.w9538620", MODE_PRIVATE);
            String templockmail = prefs.getString("templockmail", "mail");
            Log.d("ShareCredActivity", "templockmail else: " + templockmail);

            url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/shareotp?email=" + emailOwner + "&mailLock=" +templockmail
                    + "&OTP=" + otpTx;
        }
        StringRequest
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("ShareCredActivity", "success " + response);
                Toast.makeText(ShareCredActivity.this, "Details shared successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("ShareCredActivity", "error " + error);
            }
        });
        requestQueue.add(stringRequest);
    }
}