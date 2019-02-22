package com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.teofanispapadopoulos.roehamptonqaapp.R;

public class resetDialog extends AppCompatDialogFragment {
    private resetDialogListener listener;
    private EditText userEmail;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reset_password_dialog, null);

        dialogBuilder.setView(view).setTitle("Reset your Password")
                .setIcon(R.mipmap.ic_launcher)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            String sendTo = userEmail.getText().toString().trim().toLowerCase();
                            listener.sendEmail(sendTo);
                    }
                });

        userEmail = view.findViewById(R.id.reset_email);

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (resetDialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "resetDialogListener has not been implemented");
        }

    }

    public interface resetDialogListener {
        void sendEmail(String sendTo);
    }

}
