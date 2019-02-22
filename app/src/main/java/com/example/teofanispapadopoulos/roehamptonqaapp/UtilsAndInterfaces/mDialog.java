package com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.R;

public class mDialog extends AppCompatDialogFragment {
    private EditText editTextReview;
    private mDialogListener listener;
    private TextView counter;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            counter.setText(String.valueOf(200 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Review").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setPositiveButton("Add Review", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String contentReview = editTextReview.getText().toString();
                        listener.applyTexts(contentReview);
                    }
                });

        counter = view.findViewById(R.id.tv_counter);
        editTextReview = view.findViewById(R.id.reviewEditText);
        editTextReview.addTextChangedListener(mTextEditorWatcher);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (mDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "implement mDialogListener");
        }

    }

    public interface mDialogListener {
        void applyTexts(String contentReview);
    }
}
