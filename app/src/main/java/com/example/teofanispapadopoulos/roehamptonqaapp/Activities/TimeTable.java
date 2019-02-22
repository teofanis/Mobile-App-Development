package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.teofanispapadopoulos.roehamptonqaapp.Activities.HomePage.activeUserGroup;

public class TimeTable extends AppCompatActivity {
    Calendar cal;
    boolean earlyMorning, morning, noon, afternoon = false;
    private TextView TimeTable, PlaceHolderGroup,
            t1d1, t1d2, t1d3, t1d4, t1d5, t1d6, t1d7,
            t2d1, t2d2, t2d3, t2d4, t2d5, t2d6, t2d7,
            t3d1, t3d2, t3d3, t3d4, t3d5, t3d6, t3d7,
            t4d1, t4d2, t4d3, t4d4, t4d5, t4d6, t4d7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Forces device into landscape mode
        setContentView(R.layout.activity_time_table);

        Instanciate();

        Intent validUserGroup = getIntent();
        final String vGroup = validUserGroup.getStringExtra(activeUserGroup);
        PlaceHolderGroup.setText(vGroup);

        SQLHELP connection = new SQLHELP(TimeTable.this);
        SQLiteDatabase db = connection.getReadableDatabase();
        String detail[] = {vGroup};
        Cursor _cursor = connection.getTimeTable(db, detail);

        if (_cursor.moveToFirst()) {


            while (!_cursor.isAfterLast()) {

                String _dates_ = _cursor.getString(_cursor.getColumnIndex(Contract.Classes.DATE));
                String _times_ = _cursor.getString(_cursor.getColumnIndex(Contract.Classes.TIME));
                String _groups_ = _cursor.getString(_cursor.getColumnIndex(Contract.Classes.GROUP_ID));
                String _module_code_ = _cursor.getString(_cursor.getColumnIndex(Contract.Classes.MODULE_CODE));

                cal = getDateOftheWeek(_dates_);

                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);
                day++;
                month++;

                if (_groups_.compareToIgnoreCase(vGroup) == 0) {

                    String code[] = {_module_code_};
                    Cursor cursorz = connection.getModuleName(db, code);
                    if (cursorz.moveToNext()) {
                        while (!cursorz.isAfterLast()) {
                            boolean stop = false;
                            while (!stop) {
                                String _module_name_ = cursorz.getString(cursorz.getColumnIndex(Contract.Module.MODULE_NAME));

                                switch (day) {

                                    case 0:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d7.setText(_module_name_);
                                            stop = true;
                                            break;
                                        } else if (morning == true) {
                                            t2d7.setText(_module_name_);
                                            stop = true;
                                            break;

                                        } else if (noon == true) {
                                            t3d7.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d7.setText(_module_name_);
                                            stop = true;
                                            break;

                                        } else {

                                            stop = true;
                                            break;

                                        }
                                    case 1:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d1.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (morning == true) {
                                            t2d1.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d1.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d1.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else {
                                            stop = true;
                                            break;

                                        }
                                    case 2:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d2.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (morning == true) {
                                            t2d2.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d2.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d2.setText(_module_name_);
                                            stop = true;
                                            break;

                                        } else {
                                            stop = true;
                                            break;

                                        }

                                    case 3:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d3.setText(_module_name_);
                                            stop = true;
                                            break;

                                        } else if (morning == true) {
                                            t2d3.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d3.setText(_module_name_);
                                            stop = true;
                                            break;

                                        } else if (afternoon == true) {
                                            t4d3.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else {

                                            stop = true;
                                            break;

                                        }
                                    case 4:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d4.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (morning == true) {
                                            t2d4.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d4.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d4.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else {

                                            stop = true;
                                            break;

                                        }
                                    case 5:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d5.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (morning == true) {
                                            t2d5.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d5.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d5.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else {

                                            stop = true;
                                            break;

                                        }
                                    case 6:
                                        timeFinder(_times_);
                                        if (earlyMorning == true) {
                                            t1d6.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (morning == true) {
                                            t2d6.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (noon == true) {
                                            t3d6.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else if (afternoon == true) {
                                            t4d6.setText(_module_name_);
                                            stop = true;
                                            break;


                                        } else {

                                            stop = true;
                                            break;

                                        }
                                }
                                cursorz.moveToNext();
                            }
                        }
                    }

                    cursorz.close();

                    _cursor.moveToNext();
                }
            }

        } else {
        }
        _cursor.close();
        connection.close();

    }


    private Boolean timeFinder(String times) {
        String[] time = times.split(":");
        String holder = time[0];
        if (holder.startsWith("09") || holder.startsWith("10")) {
            earlyMorning = true;
            return earlyMorning;
        } else if (holder.startsWith("11") || holder.startsWith("12")) {
            morning = true;
            return morning;
        } else if (holder.startsWith("13") || holder.startsWith("14")) {
            noon = true;
            return noon;
        } else if (holder.startsWith("15") || holder.startsWith("16")) {
            afternoon = true;
            return afternoon;
        }
        return false;
    }

    private Calendar getDateOftheWeek(String date) {

        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date day = formater.parse(date);
            cal = Calendar.getInstance();
            cal.setTime(day);

        } catch (ParseException e) {
            Utils.makeText(TimeTable.this, "Error parsing dates...");
        }
        return cal;
    }

    private void Instanciate() {
        TimeTable = findViewById(R.id.timeTableTitle);
        PlaceHolderGroup = findViewById(R.id.PlaceHolderGroup);
        TableLayoutInstanciation();
    }

    private void TableLayoutInstanciation() {
        t1d1 = findViewById(R.id.t1d1);
        t1d2 = findViewById(R.id.t1d2);
        t1d3 = findViewById(R.id.t1d3);
        t1d4 = findViewById(R.id.t1d4);
        t1d5 = findViewById(R.id.t1d5);
        t1d6 = findViewById(R.id.t1d6);
        t1d7 = findViewById(R.id.t1d7);

        t2d1 = findViewById(R.id.t2d1);
        t2d2 = findViewById(R.id.t2d2);
        t2d3 = findViewById(R.id.t2d3);
        t2d4 = findViewById(R.id.t2d4);
        t2d5 = findViewById(R.id.t2d5);
        t2d6 = findViewById(R.id.t2d6);
        t2d7 = findViewById(R.id.t2d7);

        t3d1 = findViewById(R.id.t3d1);
        t3d2 = findViewById(R.id.t3d2);
        t3d3 = findViewById(R.id.t3d3);
        t3d4 = findViewById(R.id.t3d4);
        t3d5 = findViewById(R.id.t3d5);
        t3d6 = findViewById(R.id.t3d6);
        t3d7 = findViewById(R.id.t3d7);

        t4d1 = findViewById(R.id.t4d1);
        t4d2 = findViewById(R.id.t4d2);
        t4d3 = findViewById(R.id.t4d3);
        t4d4 = findViewById(R.id.t4d4);
        t4d5 = findViewById(R.id.t4d5);
        t4d6 = findViewById(R.id.t4d6);
        t4d7 = findViewById(R.id.t4d7);

    }

}
