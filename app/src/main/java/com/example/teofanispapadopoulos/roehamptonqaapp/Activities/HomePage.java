package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.resetDialog;

import org.w3c.dom.Text;

import java.util.Random;


public class HomePage extends AppCompatActivity implements resetDialog.resetDialogListener{

    //Constants for Shared Preferences
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String userNamePref = "userEmail";
    public static final String passwordPref = "password";
    public static final String SWITCH1 = "switch1";
    public static String activeUser = "HomePage";
    public static String activeUserGroup = "Group";
    private EditText userName;
    private TextInputLayout password;
    private TextView information, forgottenPassword;
    private Button login, register;
    private Switch switch1;
    private int counter = 3;
    private String textUserName, textPassword;
    private boolean switchOnOff;
    //Declaring all variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Initializing the views
        Instanciate();
        //listening for clicks on the two buttons
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.getText().toString().trim().isEmpty()) {
                    userName.setError("This field is required !");
                } else if (password.getEditText().getText().toString().trim().isEmpty()) {
                    password.setError("Please enter your password ");
                } else {
                    ValidateLogin(userName.getText().toString().trim().toLowerCase(), password.getEditText().getText().toString().trim());
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistration = new Intent(HomePage.this, RegistrationPage.class);
                startActivity(intentRegistration);
            }
        });
        forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenResetPasswordDialog();
            }
        });


        //loadSavedUser and if the textUserName and textPassword are not empty updateViews
        loadSavedUser();
        if (!textUserName.isEmpty() && !textPassword.isEmpty()) {
            updateViews();
        }

    }

    private void Instanciate() {
        userName = findViewById(R.id.etLogin);
        password = findViewById(R.id.etPassword);
        information = findViewById(R.id.tvInfo);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);
        switch1 = findViewById(R.id.switch1);
        forgottenPassword = findViewById(R.id.forgottenPassword);
        information.setText("Number of attempts remaining: 3");
    }

    private void ValidateLogin(String username, String passwords) {
        /*This method retrieves user data from the db based on username and
         *checks the returned row to see if the passwords match.
         *If they do and switch remember me is checked it updates the sharedPreferences and goes to dashboard
         *along with all the user info gathered from the db and the activity finishes.
         * else if something is not right you get the corresponding feedback and the login attempts are monitored
         * and if they reach 0 than login is disabled and you'll have to restart the app.
         */
        SQLHELP connection = new SQLHELP(HomePage.this);
        SQLiteDatabase db = connection.getReadableDatabase();

        String _pw[] = {username};
        Cursor _cursor = connection.authenticate(db, _pw);

        if (_cursor.moveToFirst()) {
            _cursor.moveToFirst();

            String _emailToValidate = _cursor.getString(_cursor.getColumnIndex(Contract.User.EMAIL));
            String _passwordToValidate = _cursor.getString(_cursor.getColumnIndex(Contract.User.PASSWORD));
            String _activeUserGroup = _cursor.getString(_cursor.getColumnIndex(Contract.User.GROUP));
            String _studentId_ = _cursor.getString(_cursor.getColumnIndex(Contract.User.STUDENT_ID));
            String _profile_picture_ = _cursor.getString(_cursor.getColumnIndex(Contract.User.PROFILE_PICTURE));

            boolean hasCustomProfilePicture = false;

            if(!TextUtils.isEmpty(_profile_picture_)){
                hasCustomProfilePicture = true;
            }


            if (_emailToValidate.compareToIgnoreCase(username) == 0 &&
                    _passwordToValidate.compareToIgnoreCase(passwords) == 0) {
                Utils.makeText(HomePage.this, "Signed in successfully");

                if (switch1.isChecked()) {
                    saveUser();
                    Intent intentLogin = new Intent(HomePage.this, Dashboard.class);
                    intentLogin.putExtra(activeUser, username);
                    intentLogin.putExtra(activeUserGroup, _activeUserGroup);
                    intentLogin.putExtra("Student_ID", _studentId_);
                    intentLogin.putExtra("HasProfilePicture", hasCustomProfilePicture);
                    startActivity(intentLogin);
                    finish();
                } else {
                    Intent intentLogin = new Intent(HomePage.this, Dashboard.class);
                    intentLogin.putExtra(activeUser, username);
                    intentLogin.putExtra(activeUserGroup, _activeUserGroup);
                    intentLogin.putExtra("Student_ID", _studentId_);
                    intentLogin.putExtra("HasProfilePicture", hasCustomProfilePicture);
                    startActivity(intentLogin);
                    finish();
                }

            } else {
                counter--;
                userName.setText(null);
                password.getEditText().setText(null);
                Utils.makeText(HomePage.this, "Invalid credentials, please try again.");
                information.setText("Number of attempts remaining: " + String.valueOf(counter));
                if (counter == 0) {
                    login.setEnabled(false);
                    forgottenPassword.setVisibility(View.VISIBLE);
                    information.setText("Too many attempts please restart the application and try again");
                }
            }

        } else {
            counter--;
            userName.setText(null);
            password.getEditText().setText(null);
            Utils.makeText(HomePage.this, "Invalid credentials, please try again.");
            information.setText("Number of attempts remaining: " + String.valueOf(counter));
            if (counter == 0) {
                login.setEnabled(false);
                forgottenPassword.setVisibility(View.VISIBLE);
                information.setText("Too many attempts please restart the application and try again");
            }

        }


    }

    private void OpenResetPasswordDialog(){
        resetDialog resetUserPassword = new resetDialog();
        resetUserPassword.show(getSupportFragmentManager(), "Reset User Password");
    }

    //Android backButton is enabled here to exit the app.
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //Saves the user on shared preferences
    public void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(userNamePref, userName.getText().toString());
        editor.putString(passwordPref, password.getEditText().getText().toString());
        editor.putBoolean(SWITCH1, switch1.isChecked());

        editor.apply();

        Utils.makeText(HomePage.this, "You will now remain signed in ");
    }

    //Loads the saved User
    public void loadSavedUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        textUserName = sharedPreferences.getString(userNamePref, "");
        textPassword = sharedPreferences.getString(passwordPref, "");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1, false);

    }

    //update the fields
    public void updateViews() {
        ValidateLogin(textUserName, textPassword);
        switch1.setChecked(switchOnOff);
    }
    protected String generateTemporaryPassword() {
        String Characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder rndpw = new StringBuilder();
        Random rnd = new Random();
        while (rndpw.length() < 10) {
            int index = (int) (rnd.nextFloat() * Characters.length());
            rndpw.append(Characters.charAt(index));
        }
        String randomPassword = rndpw.toString();
        return randomPassword;
    }


        @Override
    public void sendEmail(String sendTo) {
        SQLHELP connect = new SQLHELP(this);
        SQLiteDatabase db = connect.getReadableDatabase();
        String columns[] = {Contract.User.EMAIL};
        String filter[] = {sendTo};
        Cursor cursor = db.query(Contract.User.TABLE_NAME, columns, Contract.User.EMAIL + "=?", filter, null,null,null,null);

        if(cursor.moveToFirst()){
            final String temporaryPassword = generateTemporaryPassword();
            connect.updatePassword(db, sendTo, temporaryPassword.trim());
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.satisfied_white_24dp)
                    .setTitle("Your password have been changed")
                    .setMessage("Your new temporary password is " + temporaryPassword + "\n" + "Once signed in enter the settings to change it.")
                    .setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Temporary Password",temporaryPassword);
                            clipboard.setPrimaryClip(clip);
                            Utils.makeText(HomePage.this, "Copied " + temporaryPassword);
                            counter = 3;
                            information.setText("Number of attempts remaining: " + String.valueOf(counter));
                            login.setEnabled(true);
                        }
                    })
                    .show();


        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.dialog_warning)
                    .setTitle("Reset password has failed")
                    .setMessage("The provided email does not belong to a registered user.")
                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })


                    .show();
        }

        cursor.close();
        connect.close();

    }
}