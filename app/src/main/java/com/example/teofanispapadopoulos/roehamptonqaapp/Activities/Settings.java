package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

import java.io.ByteArrayOutputStream;

import static com.example.teofanispapadopoulos.roehamptonqaapp.Activities.HomePage.activeUser;
import static com.example.teofanispapadopoulos.roehamptonqaapp.Activities.HomePage.activeUserGroup;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button submit;
    private TextView studentID, Name, Email, Group;
    private EditText inputField, confirmPassword;
    private Spinner actionChoosen;
    private ImageView profilePicture;
    private static final int PICK_IMAGE = 100;
    Uri imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Instanciate();
        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                switch (actionChoosen.getSelectedItemPosition()) {
                    case 0:
                        Utils.makeText(Settings.this, "Select an option before submitting !");

                        break;

                    case 1:
                        changeUserEmail();
                        break;

                    case 2:
                        validateUserPassword();
                        break;

                }

            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }


    private void Instanciate() {
        submit = findViewById(R.id.UpdateInfo);
        studentID = findViewById(R.id.StudentId);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.userEmail);
        Group = findViewById(R.id.lblGroup);
        inputField = findViewById(R.id.inputField);
        actionChoosen = findViewById(R.id.optionSpinner);
        confirmPassword = findViewById(R.id.confirmPassword);
        profilePicture =  findViewById(R.id.Picture);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.settingOption, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionChoosen.setAdapter(adapter);
        actionChoosen.setOnItemSelectedListener(this);
        getUserInformation();
    }

    private void selectImage(){
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(openGallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageURL = data.getData();
            profilePicture.setImageURI(imageURL);

            Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,0,stream);
            byte[] byteImg = stream.toByteArray();
            String encodedImage = Base64.encodeToString(byteImg, Base64.DEFAULT);

            Utils.makeText(Settings.this, encodedImage);

            SQLHELP connect = new SQLHELP(Settings.this);
            SQLiteDatabase db = connect.getWritableDatabase();
            connect.uploadProfilePicture(db, studentID.getText().toString(), encodedImage);
            connect.close();
        }
    }

    private void validateUserPassword() {

        if (inputField.getText().toString().trim().isEmpty() || inputField.getText().toString().trim().length() < 8) {
            Utils.makeText(Settings.this, "Password must be at least 8 characters long ");
        } else {
            if (confirmPassword.getText().toString().trim().compareToIgnoreCase(inputField.getText().toString().trim()) != 0) {
                Utils.makeText(Settings.this, "Passwords don't match");
            } else {

                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.dialog_warning)
                        .setTitle("Account Information updates")
                        .setMessage("You will be singed out in order for the changes to take place."
                                + " Please Continue to set your new password to " + inputField.getText().toString().trim() + " or hit Cancel.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changePassword(inputField.getText().toString().trim());
                                Utils.makeText(Settings.this, "You have updated your password successfully !");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Utils().showDialog(Settings.this,
                                        "Account Information Updates Canceled",
                                        "No changes made on your account");
                                inputField.setText("");
                                confirmPassword.setText("");

                            }
                        })
                        .show();

            }

        }

    }

    private void changeUserEmail() {

        if (inputField.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(inputField.getText().toString()).matches()) {
            Utils.makeText(Settings.this, "This is not a valid Email");
        } else {
            Utils.makeText(Settings.this, "You have updated your email successfully!");

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.dialog_warning)
                    .setTitle("Account Information updates")
                    .setMessage("You will be signed out in order for the changes to take place. "
                            + " Please press Continue to set your new email to "
                            + inputField.getText().toString().trim() + " or hit Cancel.")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executeChanges();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Utils().showDialog(Settings.this, "Account Information Updates Canceled", "No changes made on your account");
                            inputField.setText("");
                        }
                    })
                    .show();


        }
    }


    private void executeChanges() {
        SQLHELP connect = new SQLHELP(this);
        SQLiteDatabase db = connect.getWritableDatabase();
        connect.updateUserEmail(db, studentID.getText().toString().trim(), inputField.getText().toString().trim());
        connect.close();
        Intent relog = new Intent(Settings.this, HomePage.class);
        startActivity(relog);

    }

    private void changePassword(String newPassword) {
        SQLHELP connect = new SQLHELP(this);
        SQLiteDatabase db = connect.getWritableDatabase();
        connect.updatePassword(db, Email.getText().toString().trim(), newPassword.trim());
        connect.close();
        Intent relog = new Intent(Settings.this, HomePage.class);
        startActivity(relog);
    }

    private void getUserInformation() {
        Intent userInformation = getIntent();
        String userEmail = userInformation.getExtras().getString(activeUser);
        String userId = userInformation.getExtras().getString("Student_ID");
        String userGroup = userInformation.getExtras().getString(activeUserGroup);
        studentID.setText(userId);
        Email.setText(userEmail);
        Group.setText(userGroup);
        Name.setText(getFullName(userId));


    }

    private String getFullName(String byStudentId) {

        SQLHELP connect = new SQLHELP(this);
        SQLiteDatabase db = connect.getReadableDatabase();
        String arguments[] = {byStudentId};
        Cursor _cursor = connect.getUserName(db, arguments);
        if (_cursor.moveToFirst()) {

            String firstName = _cursor.getString(_cursor.getColumnIndex(Contract.User.FIRST_NAME));
            String surname = _cursor.getString(_cursor.getColumnIndex(Contract.User.SURNAME));
            String fullName = firstName + " " + surname;
            return fullName;
        }
        _cursor.close();
        connect.close();
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            inputField.setTypeface(Typeface.DEFAULT);
            confirmPassword.setVisibility(View.GONE);
            confirmPassword.animate().translationY(500f);
            inputField.setText("");
            inputField.setHint("Select an option");
            ((TextView) actionChoosen.getSelectedView()).setTextColor(getResources().getColor(R.color.gray));
            Utils.makeText(this, "Select the option you want to change");
        } else if (position == 1) {
            inputField.setTypeface(Typeface.DEFAULT);
            confirmPassword.setVisibility(View.GONE);
            confirmPassword.animate().translationY(500f);
            inputField.setText("");
            inputField.setHint("Enter new Email");
            inputField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        } else if (position == 2) {
            confirmPassword.setVisibility(View.VISIBLE);
            inputField.setText("");
            inputField.setHint("Enter new password");
            inputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputField.setTypeface(Typeface.DEFAULT);
            confirmPassword.animate().translationYBy(-500f).setDuration(500);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
