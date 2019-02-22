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

import com.example.teofanispapadopoulos.roehamptonqaapp.Adapters.ItemAdapter;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

public class Library extends AppCompatActivity {

    public static String activeStudentID;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    private SQLiteDatabase db;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent = getIntent();
        String UserId = intent.getExtras().getString("Student_ID");
        activeStudentID = UserId;

        //These are the options that go in the spinner adapter.
        final String[] catTypes = {"All", "Programming", "Business", "Networking", "Design", "Analytics", "Mathematics"};

        categorySpinner = findViewById(R.id.categoryType);
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, catTypes));

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((position >= 0) && (position < catTypes.length)) {
                    ((TextView) categorySpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) categorySpinner.getSelectedView()).setTypeface(Typeface.DEFAULT_BOLD);
                    itemAdapter = new ItemAdapter(Library.this, getSelectedCategoryData(position));
                    recyclerView.setAdapter(itemAdapter);

                } else {
                    Utils.makeText(Library.this, "No such category !");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //DB connection
        SQLHELP connection = new SQLHELP(Library.this);
        db = connection.getReadableDatabase();


        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this, getAllItems());
        recyclerView.setAdapter(itemAdapter);


    }

    // Retrieve all records from Book table
    private Cursor getAllItems() {
        return db.query(
                Contract.Book.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

    }

    // The checks  the position of the spinner and runs a query based the category selected
    private Cursor getSelectedCategoryData(int position) {

        switch (position) {
            case 0:
                getAllItems();
                break;
            case 1:
                String first[] = {"Programming"};

                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        first,
                        null,
                        null,
                        null);

            case 2:
                String second[] = {"Business"};
                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        second,
                        null,
                        null,
                        null);


            case 3:
                String third[] = {"Networking"};
                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        third,
                        null,
                        null,
                        null);


            case 4:
                String fourth[] = {"Design"};
                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        fourth,
                        null,
                        null,
                        null);
            case 5:
                String fifth[] = {"Analytics"};
                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        fifth,
                        null,
                        null,
                        null);
            case 6:
                String sixth[] = {"Mathematics"};
                return db.query(Contract.Book.TABLE_NAME,
                        null,
                        Contract.Book.CATEGORY_TYPE + "=?",
                        sixth,
                        null,
                        null,
                        null);
        }

//Default it will return all categories.
        return getAllItems();


    }
}
