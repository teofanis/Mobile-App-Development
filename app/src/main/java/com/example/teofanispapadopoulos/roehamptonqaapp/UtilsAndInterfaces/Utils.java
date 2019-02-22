package com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.teofanispapadopoulos.roehamptonqaapp.R;

public class Utils {

    public static void makeText(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(Context context, String Title, String Msg) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setIcon(R.drawable.satisfied_white_24dp)
                .setTitle(Title)
                .setMessage(Msg)
                .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}