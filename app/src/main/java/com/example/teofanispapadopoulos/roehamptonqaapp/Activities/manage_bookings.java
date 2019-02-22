package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Adapters.BookingAdapter;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;


public class manage_bookings extends AppCompatActivity {


    public String ArgumentConstant = "UserID";
    SQLiteDatabase db;
    private RecyclerView bookingRecyclerView;
    private RecyclerView.Adapter bookingAdapter;
    private Spinner orderBySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Instanciate();

        final String[] orderBy = {"Ascending", "Descending"};

        orderBySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderBy));

        orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((position >= 0) && (position < orderBy.length)) {
                    ((TextView) orderBySpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) orderBySpinner.getSelectedView()).setTypeface(Typeface.DEFAULT_BOLD);
                    bookingAdapter = new BookingAdapter(manage_bookings.this, getReservedBooksOrder(position));
                    bookingRecyclerView.setAdapter(bookingAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        SQLHELP connection = new SQLHELP(manage_bookings.this);
        db = connection.getReadableDatabase();

        bookingRecyclerView.setHasFixedSize(true);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(this, getAllReservedBooks());

        bookingRecyclerView.setAdapter(bookingAdapter);

    }

    private Cursor getReservedBooksOrder(int position) {
        switch (position) {
            case 0:
                String selectionsArgs[] = {ArgumentConstant};
                return db.query(Contract.Reservations.TABLE_NAME,
                        null,
                        Contract.Reservations.USER_ID + "=?",
                        selectionsArgs,
                        null,
                        null,
                        Contract.Reservations.BOOKED_ON + " ASC");

            case 1:
                String selectionArg[] = {ArgumentConstant};
                return db.query(Contract.Reservations.TABLE_NAME,
                        null,
                        Contract.Reservations.USER_ID + "=?",
                        selectionArg,
                        null,
                        null,
                        Contract.Reservations.BOOKED_ON + " DESC");

        }
        return getAllReservedBooks();
    }

    private Cursor getAllReservedBooks() {
        String id[] = {ArgumentConstant};
        return db.query(Contract.Reservations.TABLE_NAME,
                null,
                Contract.Reservations.USER_ID + "=?",
                id,
                null,
                null,
                null);
    }


    private void Instanciate() {

        bookingRecyclerView = findViewById(R.id.BookingsRecyclerView);
        orderBySpinner = findViewById(R.id.orderBySpinner);
        Intent getStudentID = getIntent();

        if (getStudentID.hasExtra("Reserved")) {
            String studentIdBookings = Library.activeStudentID;
            ArgumentConstant = studentIdBookings;
        } else {
            String StudentIdbooking = getStudentID.getExtras().getString("Student_ID");
            ArgumentConstant = StudentIdbooking;
        }

    }

    //BackPressed will go to dashboards last instance, without making a new instance of the activity.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToDashboard = new Intent(manage_bookings.this, Dashboard.class);
        backToDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(backToDashboard);
    }
}
