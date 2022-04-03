package com.tees.mad.w9538620;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.ownerInvitation)
    MaterialCardView ownerCard;
    @BindView(R.id.lockRequest)
    MaterialCardView lockRequestCard;
    @BindView(R.id.skillEnable)
    MaterialCardView skillEnableCard;
    @BindView(R.id.lockStatus)
    MaterialCardView lockStatusCard;
    @BindView(R.id.lockRelease)
    MaterialCardView lockReleaseCard;
    @BindView(R.id.lockAccessShare)
    MaterialCardView lockAccessShareCard;
    @BindView(R.id.otpSharing)
    MaterialCardView otpSharingCard;
    @BindView(R.id.accessdisable)
    MaterialCardView accessisableCard;
    @BindView(R.id.logsAccess)
    MaterialCardView logsAccessCard;
    @BindView(R.id.agentMail)
    LinearLayout agentMailCard;
    @BindView(R.id.agentPwd)
    LinearLayout agentPwdCard;
    @BindView(R.id.agentOtp)
    LinearLayout agentOtpCard;
    @BindView(R.id.agentName)
    LinearLayout agentNameCard;
    @BindView(R.id.nameLock)
    TextView nameView;
    String[] emailsOwner;
    String emailOwner;
    private String otpLock = "";
    private String mailLock;
    private String passLock;
    private String nameLock;
    private boolean verifyOwner;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor myEditor;
    private String userName, userEmail, userType, agentCode, userAddress, userLock;
    private ClipboardManager clipboard;
    private ClipData clip;
    private int checkedItem;
    private boolean isRelease;
    private BroadcastReceiver internetReceiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internetReceiver = new internetReceiver();
        broadcastIntent();

        ButterKnife.bind(this);

        sharedpreferences = getSharedPreferences("com.tees.mad.w9538620", Context.MODE_PRIVATE);
        myEditor = sharedpreferences.edit();
        getUserData();

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clip = ClipData.newPlainText("", "");

//        verifyOwner = getIntent().getBooleanExtra("verifyOwner", false);
        verifyOwner = sharedpreferences.getBoolean("verifyOwner", false);
        Log.d(TAG, "verifyOwner : MainActivity = " + verifyOwner);

        String title = (verifyOwner ? "Owner" : "Listing Agent") + " - Home";

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(title);
        Objects.requireNonNull(actionBar)
                .setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purple_700)),
                0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home);

        init();
        updateUi();

    }

    public void broadcastIntent() {
        registerReceiver(internetReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
       // Toast.makeText(MainActivity.this, "internetReciever called", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(internetReceiver);
    }

    private void getUserData() {
        verifyOwner = sharedpreferences.getBoolean("verifyOwner", false);
        userName = sharedpreferences.getString("username", "");
        userEmail = sharedpreferences.getString("emailId", "");
        userType = verifyOwner ? "Home Owner" : "Listing Agent";
        userAddress = sharedpreferences.getString("address", "");
        agentCode = sharedpreferences.getString("agentCode", "");
        userLock = sharedpreferences.getString("lockdetails", "");
    }

    private void init() {
        lockRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRelease = false;
                requestOwnerList();
            }
        });

        skillEnableCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://alexa.amazon.com";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_700));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
            }
        });

        lockStatusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://alexa.amazon.com";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_700));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
            }
        });

        ownerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n\nThis is Listing Agent from mytees App. " +
                        "Please download the mytees app from Playstore and share door lock access. \n\nThank you.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });

        lockAccessShareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShareCredActivity.class)
                        .putExtra("cred", true)
                        .putExtra("name", userName)
                        .putExtra("email", userEmail));
            }
        });

        otpSharingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShareCredActivity.class)
                        .putExtra("cred", false)
                        .putExtra("name", userName)
                        .putExtra("email", userEmail));
            }
        });

        accessisableCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessdisableCard();
            }
        });

        lockReleaseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isRelease = true;
                releaseAccess();
            }
        });

        logsAccessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccessHistory();
            }
        });
    }

    private void getAccessHistory() {
        Utils.showDialog(MainActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "" + userEmail;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("MainActivity", "success " + response);
                saveAccessData(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("MainActivity", "requestOwnerList error " + error);
            }
        });
        requestQueue.add(stringRequest);

    }

    private void saveAccessData(String response) {
        JSONArray jArray;
        ArrayList<String> history = new ArrayList<>();

        try {
            jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject oneObject = jArray.getJSONObject(i);
                String log = "\nListing Agent : " + oneObject.getString("LAname") + "\nAgent Email : "
                        + oneObject.getString("LAmail") + "\nDate & Time : "
                        + oneObject.getString("date") + "\n";
                history.add(log);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (history.size() == 0) {
            Toast.makeText(MainActivity.this, "No access history available", Toast.LENGTH_SHORT).show();
            return;
        }

        ContextThemeWrapper themedContext = new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(themedContext);
        builder.setTitle("Lock Access History");
        builder.setItems(history.toArray(new String[history.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Close", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void requestOwnerList() {
        Utils.showDialog(MainActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/listofowners";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("MainActivity", "success " + response);
                saveOwnerListData(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("MainActivity", "requestOwnerList error " + error);
            }
        });
        requestQueue.add(stringRequest);

    }

    private void saveOwnerListData(String response) {
        JSONArray jArray;
        ArrayList<String> owners = new ArrayList<>();

        try {
            jArray = new JSONArray(response);
            emailsOwner = new String[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject oneObject = jArray.getJSONObject(i);
                if (oneObject.getString("owname").isEmpty() ||
                        oneObject.getString("lockdetails").isEmpty()) {
                    continue;
                }
                String owner = oneObject.getString("owname") + " (" + oneObject.getString("lockdetails") + ")";
                owners.add(owner);
                emailsOwner[i] = oneObject.getString("email");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // setup the alert builder
        checkedItem = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose an owner");

        builder.setSingleChoiceItems(owners.toArray(new String[owners.size()]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem = which;
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                emailOwner = emailsOwner[checkedItem];
                if (!isRelease) {
                    lockRequestCardAccess();
                } else {
                    deleteLockAccess(emailOwner);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void lockRequestCardAccess() {
        Utils.showDialog(MainActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/ownerlockcreds?email="
                + emailOwner + "&LAmail=" + userEmail
                + "&LAname=" + userName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("MainActivity", "lockRequestCardAccess success " + response);
                saveLockData(response);
                agentMailCard.setVisibility(View.VISIBLE);
                agentPwdCard.setVisibility(View.VISIBLE);
                agentOtpCard.setVisibility(View.VISIBLE);
                agentNameCard.setVisibility(View.VISIBLE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("MainActivity", "lockRequestCardAccess : error " + error);
            }
        });
        requestQueue.add(stringRequest);
    }

    private void saveLockData(String response) {
        try {
            JSONObject sys = new JSONObject(response);
            mailLock = sys.getString("email");
            nameLock = sys.getString("lockdetails");
            passLock = sys.getString("password");

            nameView.setText(nameLock);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //fixed issue :: logout when clicked back
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Log.d("MainActivity", "onBackPressed, app running in background");
    }

    private void releaseAccess() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Are you sure you want to release the lock access?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        lockReleaseCardAccess();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void lockReleaseCardAccess() {
        Log.d("MainActivity", "userEmail: " + userEmail);

        deleteLockAccess(emailOwner);
        //requestOwnerList();
        agentMailCard.setVisibility(View.GONE);
        agentPwdCard.setVisibility(View.GONE);
        agentOtpCard.setVisibility(View.GONE);
        agentNameCard.setVisibility(View.GONE);
    }

    private void accessdisableCard() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Are you sure you disable lock access to mytees?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLockAccess(userEmail);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void deleteLockAccess(String mail) {
        Utils.showDialog(MainActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        String url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/removelockdetails?email=" + mail;

        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.dismissDialog();
                        Log.d("MainActivity", "Lock access disabled " + response);
                        Toast.makeText(MainActivity.this, "Lock access disabled", Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.dismissDialog();
                        Log.d("MainActivity", "Failed to disable lock access " + error);
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void updateUi() {
        if (!verifyOwner) {
            ownerCard.setVisibility(View.VISIBLE);
            lockRequestCard.setVisibility(View.VISIBLE);
            skillEnableCard.setVisibility(View.VISIBLE);
            lockStatusCard.setVisibility(View.VISIBLE);
            lockReleaseCard.setVisibility(View.VISIBLE);

            lockAccessShareCard.setVisibility(View.GONE);
            otpSharingCard.setVisibility(View.GONE);
            accessisableCard.setVisibility(View.GONE);
            logsAccessCard.setVisibility(View.GONE);
        } else {
            ownerCard.setVisibility(View.GONE);
            lockRequestCard.setVisibility(View.GONE);
            skillEnableCard.setVisibility(View.GONE);
            lockStatusCard.setVisibility(View.GONE);
            lockReleaseCard.setVisibility(View.GONE);

            lockAccessShareCard.setVisibility(View.VISIBLE);
            otpSharingCard.setVisibility(View.VISIBLE);
            accessisableCard.setVisibility(View.VISIBLE);
            logsAccessCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                showProfile();
                return true;
            case R.id.help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class)
                        .putExtra("verifyOwner", verifyOwner));
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        myEditor.putBoolean("isLoggedIn", false);
                        myEditor.commit();
                         Boolean testCommit = getIntent().getBooleanExtra("isLoggedIn", false);
                        Log.d("MainActivity", "isLoggedIn : after" + testCommit);
                        startActivity(new Intent(MainActivity.this, OptionsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private void showProfile() {
        String message;
        if (!verifyOwner) {
            message = "Name : " + userName +
                    "\nEmail ID : " + userEmail +
                    "\nUser Type : " + userType +
                    "\nAgent Code : " + agentCode;
        } else {
            message = "Name : " + userName +
                    "\nEmail ID : " + userEmail +
                    "\nUser Type : " + userType +
                    "\nLock Name : " + userLock +
                    "\nAddress : " + userAddress;
        }
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setTitle("User Profile Details")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void copyEmail(View view) {
        clip = ClipData.newPlainText(mailLock, mailLock);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "email copied, paste it in email field.", Toast.LENGTH_LONG).show();
    }

    public void copyPassword(View view) {
        clip = ClipData.newPlainText(passLock, passLock);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "password copied, paste it in password field.", Toast.LENGTH_LONG).show();
    }

    public void copyOtp(View view) {
        if (otpLock.isEmpty()) {
            lockRequestCardOtp();
        } else {
            clip = ClipData.newPlainText(otpLock, otpLock);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "OTP copied, paste it in OTP field.", Toast.LENGTH_LONG).show();
        }
    }

    private void lockRequestCardOtp() {
        Utils.showDialog(MainActivity.this);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://2k4ie3stjg.execute-api.us-east-1.amazonaws.com/v1/ownerlockcreds?email="
                + emailOwner + "&LAmail=" + userEmail
                + "&LAname=" + userName;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.dismissDialog();
                Log.d("MainActivity", "success " + response);
                saveotpLock(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissDialog();
                Log.d("MainActivity", "lockRequestCardAccess error: " + error);
            }
        });
        requestQueue.add(stringRequest);
    }


    private void saveotpLock(String response) {
        try {
            JSONObject sys = new JSONObject(response);
            otpLock = sys.getString("otp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (otpLock.isEmpty()) {
            Toast.makeText(getApplicationContext(), "OTP not yet received.", Toast.LENGTH_LONG).show();
        } else {
            ((ImageView) findViewById(R.id.otp_icon)).setImageResource(R.drawable.ic_copy);
        }
    }
}