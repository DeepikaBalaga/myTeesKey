package com.tees.mad.w9538620;


import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class internetReceiver extends BroadcastReceiver {
    Dialog dialog;
    TextView nettext;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = checkInternet.getConnectivityStatusString(context);
        dialog = new Dialog(context, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.activity_data);
        Button restartapp = (Button) dialog.findViewById(R.id.restartapp);
        nettext = (TextView) dialog.findViewById(R.id.nettext);

        restartapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
                Log.d("clickedbutton", "yes");
                Intent i = new Intent(context, MainActivity.class);
                context.startActivity(i);
            }
        });
        Log.d("network", status);
        if (status.isEmpty() || status.equals("No internet is available") || status.equals("No Internet Connection")) {
            status = "No Internet Connection";
            dialog.show();
        }
        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}