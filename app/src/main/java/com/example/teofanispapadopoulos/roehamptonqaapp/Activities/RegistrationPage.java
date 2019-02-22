package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

// Implementing button click listener
public class RegistrationPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public boolean isValid;
    private EditText email, firstName, surname;
    private TextInputLayout password, confirmPassword;
    private Spinner group;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_registration_page);
        // Initialize the views
        Instanciate();

        //setting button click listener
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Checking if fields are empty and password is empty or less than 8 characters long
                if (password.getEditText().toString().isEmpty() || password.getEditText().length() < 8) {
                    password.setError("Password must be at least 8 character long");
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("This field is required ");
                } else if (firstName.getText().toString().isEmpty()) {
                    firstName.setError("This field is required");
                } else if (surname.getText().toString().isEmpty()) {
                    surname.setError("This field is required");
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    Utils.makeText(RegistrationPage.this, "This is not a valid email !");
                }
                 else {
                    //if the above are true than call validate method
                    validatePasswords(password.getEditText().getText().toString(), confirmPassword.getEditText().getText().toString());
                    //if that method returns true and there is a group selected proceed with adding the record in the database
                    //and start HomePage activity

                    if (isValid && group.getSelectedItemPosition() != 0) {
                        if (SignUp(firstName.getText().toString(), surname.getText().toString(), email.getText().toString().trim(),
                                group.getSelectedItem().toString(), password.getEditText().getText().toString())) {
                            Utils.makeText(RegistrationPage.this, "Creating account...");
                            Intent intentRegister = new Intent(RegistrationPage.this, HomePage.class);
                            startActivity(intentRegister);
                        } else {
                            Utils.makeText(RegistrationPage.this, "Email is already taken !!!");
                        }

                    } else {
                        Utils.makeText(RegistrationPage.this, "You must select a group !");
                    }

                }

            }
        });

    }


    private boolean SignUp(String firstname, String surname, String mail, String Group, String password) {
        //While adding the record to the database it checks if the email exists
        //if the email does not exists it will proceed, else it will display Email is already taken

        SQLHELP conObject = new SQLHELP(RegistrationPage.this);
        SQLiteDatabase db = conObject.getWritableDatabase();
        String[] selectionArgs = {mail};
        Cursor cursorz = db.query(Contract.User.TABLE_NAME,
                null,
                Contract.User.EMAIL + "=?",
                selectionArgs,
                null,
                null,
                null
        );
        if (cursorz.moveToFirst()) {
            return false;
        } else {
            String fn = firstname;
            String sn = surname;
            String em = mail;
            String gp = Group;
            String pw = password;

            conObject.Register(db, fn, sn, em, Group, pw);
            conObject.close();
            return true;
        }

    }

    private void Instanciate() {

        password = findViewById(R.id.etRpassword);
        confirmPassword = findViewById(R.id.etConfirmPasswordord);
        email = findViewById(R.id.etEmail);
        register = findViewById(R.id.btnRegister);
        firstName = findViewById(R.id.etFirstName);
        surname = findViewById(R.id.etSurname);
        group = findViewById(R.id.groupSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groups, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group.setAdapter(adapter);
        group.setOnItemSelectedListener(this);


    }

    private boolean validatePasswords(String passwords, String confirmPasswords) {

        if (passwords.equals(confirmPasswords)) {
            isValid = true;
            return isValid;
        } else {
            isValid = false;
            Utils.makeText(RegistrationPage.this, "The entered passwords do not match");
            return isValid;
        }


    }

    //This overides the onItemSelected for the spinner and sets the item text on position 0 to gray
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            ((TextView) group.getSelectedView()).setTextColor(getResources().getColor(R.color.gray));

            Utils.makeText(RegistrationPage.this, "Please select a group");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
