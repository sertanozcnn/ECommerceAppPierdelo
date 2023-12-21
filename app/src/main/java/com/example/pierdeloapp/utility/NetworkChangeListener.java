package com.example.pierdeloapp.utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.pierdeloapp.R;

public class NetworkChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.isConnectedToInternet(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layoutDialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog,null);
            builder.setView(layoutDialog);

            AppCompatButton btnRetry = layoutDialog.findViewById(R.id.btnRetry);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(v -> {
                dialog.dismiss();
                onReceive(context,intent);
            });
        }
    }
}
