package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

import static com.example.teofanispapadopoulos.roehamptonqaapp.Activities.HomePage.activeUser;
import static com.example.teofanispapadopoulos.roehamptonqaapp.Activities.HomePage.activeUserGroup;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout btnMoodle, btnLibrary, btnFloorMap, btnTimeTable, btnSettings, btnBookings;
    private TextView logged, logtext;
    private Button logout;
    private ImageView profilePicture, logotest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Forces the device into Portrait mode

        Intent validUser = getIntent();
        String vUser = validUser.getStringExtra(activeUser);


        boolean userProfile = validUser.getExtras().getBoolean("HasProfilePicture");
/*
        if(userProfile){
            SQLHELP connect = new SQLHELP(Dashboard.this);
            SQLiteDatabase db = connect.getReadableDatabase();
            String profile[] = {Contract.User.PROFILE_PICTURE};
            String arguments[] = {vUser};

            Cursor c = db.query(Contract.User.TABLE_NAME,
                    profile,
                    Contract.User.EMAIL + "=?",
                    arguments,
                    null,
                    null,
                    null,
                    null);

            if(c.moveToFirst()){
                c.moveToFirst();
                String profile_picture = c.getString(c.getColumnIndex(Contract.User.PROFILE_PICTURE));


                byte[] decodeString = Base64.decode(profile_picture, Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(decodeString, 0,  decodeString.length);
                logotest.setImageBitmap(image);
            }
            c.close();
            db.close();
        }

*/
        Instanciate();


        logged.setText(vUser);

        btnMoodle.setOnClickListener(this);
        btnTimeTable.setOnClickListener(this);
        btnFloorMap.setOnClickListener(this);
        btnLibrary.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnBookings.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    private void Instanciate() {


        btnMoodle = findViewById(R.id.Moodle);
        btnFloorMap = findViewById(R.id.Floor_plan);
        btnLibrary = findViewById(R.id.library);
        btnTimeTable = findViewById(R.id.timetable);
        btnSettings = findViewById(R.id.idSettings);
        btnBookings = findViewById(R.id.idBookings);
        profilePicture = findViewById(R.id.profilePicture);
        logotest = findViewById(R.id.logotest);

        logout = findViewById(R.id.btn_logout);
        logtext = findViewById(R.id.tvUser);
        logged = findViewById(R.id.tvLogged);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Moodle:
                Utils.makeText(Dashboard.this, "Redirecting to Moodle");
                Intent intentMoodle =
                        new Intent("android.intent.action.VIEW", Uri.parse("https://partnerships.moodle.roehampton.ac.uk/"));
                startActivity(intentMoodle);
                break;

            case R.id.library:
                Intent validUser = getIntent();
                String vUser = validUser.getStringExtra(activeUser);
                String UserId = validUser.getExtras().getString("Student_ID");
                Intent Library = new Intent(Dashboard.this, com.example.teofanispapadopoulos.roehamptonqaapp.Activities.Library.class);
                Library.putExtra(activeUser, vUser);
                Library.putExtra("Student_ID", UserId);
                startActivity(Library);
                Utils.makeText(Dashboard.this, "Retrieving library");
                break;

            case R.id.Floor_plan:
                Intent floorPlanIntent = new Intent(Dashboard.this, FloorPlan.class);
                startActivity(floorPlanIntent);
                Utils.makeText(Dashboard.this, "Displaying Floor Plan");
                break;

            case R.id.timetable:
                Intent validUserGroup = getIntent();
                String vGroup = validUserGroup.getStringExtra(activeUserGroup);
                Intent TimeTable = new Intent(Dashboard.this, com.example.teofanispapadopoulos.roehamptonqaapp.Activities.TimeTable.class);
                TimeTable.putExtra(activeUserGroup, vGroup);
                startActivity(TimeTable);
                Utils.makeText(Dashboard.this, "Displaying Timetable");
                break;

            case R.id.idSettings:
                Intent validUserInfo = getIntent();
                String validUserAccountData = validUserInfo.getStringExtra(activeUser);
                String vUserId = validUserInfo.getExtras().getString("Student_ID");
                String validUserGroupData = validUserInfo.getStringExtra(activeUserGroup);
                Intent settingsIntent = new Intent(Dashboard.this, Settings.class);
                settingsIntent.putExtra(activeUser, validUserAccountData);
                settingsIntent.putExtra("Student_ID", vUserId);
                settingsIntent.putExtra(activeUserGroup, validUserGroupData);
                startActivity(settingsIntent);

                Utils.makeText(Dashboard.this, "Displaying Account Settings ");
                break;

            case R.id.btn_logout:
                Utils.makeText(Dashboard.this, "Logged out");
                SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intentLogout = new Intent(Dashboard.this, HomePage.class);
                startActivity(intentLogout);
                break;

            case R.id.idBookings:
                Intent userInfo = getIntent();
                String userInfoData = userInfo.getExtras().getString("Student_ID");
                Intent manageBookingsIntent = new Intent(Dashboard.this, manage_bookings.class);
                manageBookingsIntent.putExtra("Student_ID", userInfoData);
                startActivity(manageBookingsIntent);

                Utils.makeText(Dashboard.this, "Displaying your bookings");


                break;
        }
    }

    //Android back button will exit the application and won't go to homepage.
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}
