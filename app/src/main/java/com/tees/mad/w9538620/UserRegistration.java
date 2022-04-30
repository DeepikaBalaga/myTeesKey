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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRegistration extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    @BindView(R.id.name)
    TextInputEditText name;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.pass)
    TextInputEditText pass;
    @BindView(R.id.confirm_pass)
    TextInputEditText confirm_pass;
    @BindView(R.id.address)
    TextInputEditText address;
    @BindView(R.id.lock)
    TextInputEditText lock;
    @BindView(R.id.code)
    TextInputEditText code;
    @BindView(R.id.code_layout)
    TextInputLayout code_layout;
    @BindView(R.id.address_layout)
    TextInputLayout address_layout;
    @BindView(R.id.lock_layout)
    TextInputLayout lock_layout;

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private boolean verifyOwner;

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        sharedpreferences = getSharedPreferences("com.tees.mad.w9538620", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        verifyOwner = getIntent().getBooleanExtra("verifyOwner", false);
        Log.d(TAG, "verifyOwner : UserRegistration = " + verifyOwner);

        if (verifyOwner) {
            code_layout.setVisibility(View.GONE);
            address_layout.setVisibility(View.VISIBLE);
            lock_layout.setVisibility(View.VISIBLE);
        } else {
            code_layout.setVisibility(View.VISIBLE);
            address_layout.setVisibility(View.GONE);
            lock_layout.setVisibility(View.GONE);
        }

        String title = (verifyOwner ? "Owner" : "Listing Agent") + " - Registration";
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(title);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
    }

    public void register(View view) {
        if (verifyInputs()) {
            registerUser();
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

    private boolean verifyInputs() {

        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your email ID.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(email.getText().toString())) {
            Toast.makeText(this, "Please enter valid email ID.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirm_pass.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!confirm_pass.getText().toString().equals(pass.getText().toString())) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!verifyOwner) {
            if (code.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter your agent registration code.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (address.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter your home address.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (lock.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter your smart lock name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void registerUser() {
        Utils.showDialog(UserRegistration.this);

        String mail, username, pwd, agentcode, add, nameLock;
        mail = email.getText().toString();
        username = name.getText().toString();
        pwd = pass.getText().toString();
        agentcode = code.getText().toString();
        add = address.getText().toString();
        nameLock = lock.getText().toString();


        RequestQueue requestQueue = Volley.newRequestQueue(UserRegistration.this);
        String url;
        if (verifyOwner) {
            url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/register?emailed=" + mail
                    + "&password=" + pwd
                    + "&username=" + username
                    + "&address=" + add
                    + "&lockdetails=" + nameLock;
        } else {
            url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/registeragent?emailed=" + mail
                    + "&password=" + pwd
                    + "&username=" + username
                    + "&code=" + agentcode;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("RegisterActivity", "success " + response);
                if (response.contains("User already exists")) {
                    Toast.makeText(UserRegistration.this, "User already exists.\nPlease login using same email ID", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserRegistration.this, "Registration success", Toast.LENGTH_SHORT).show();
                    saveUserData(response);
                    startActivity(new Intent(UserRegistration.this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("RegisterActivity", "error " + error);
                Toast.makeText(UserRegistration.this, "Failed to register!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);


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