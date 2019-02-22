package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

public class FloorPlan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1, spinner2;
    private TextView line, roomSelection, description;
    private ArrayAdapter<CharSequence> adapter1;
    private ImageView floorImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Instanciate();
    }

    private void Instanciate() {
        spinner1 = findViewById(R.id.floorplanSpinner);
        spinner2 = findViewById(R.id.RoomsSpinner);
        roomSelection = findViewById(R.id.OptionTextView);
        floorImg = findViewById(R.id.ImgFloorPlan);
        line = findViewById(R.id.horizontalLine);
        description = findViewById(R.id.floorDescription);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FloorPlan.this, R.array.FloorArray, R.layout.support_simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        spinner2.setVisibility(View.INVISIBLE);
        roomSelection.setVisibility(View.INVISIBLE);
        line.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.floorplanSpinner) {
            ((TextView) spinner1.getSelectedView()).setTextColor(getResources().getColor(R.color.white));
            ((TextView) spinner1.getSelectedView()).setTypeface(Typeface.DEFAULT_BOLD);
            //((TextView) spinner2.getItemAtPosition())
            switch (position) {

                case 0:
                    spinner2.setVisibility(View.INVISIBLE);
                    roomSelection.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.VISIBLE);
                    floorImg.setImageResource(R.drawable.groundfloor);
                    description.setText("Student services and registry can be found on the ground floor. As soon as you " +
                            "enter the building take the first left and on your left hand side you will find them.");
                    Utils.makeText(this, "Student Services");
                    break;
                case 1:
                    spinner2.setVisibility(View.INVISIBLE);
                    roomSelection.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.VISIBLE);
                    floorImg.setImageResource(R.drawable.firstfloor);
                    description.setText("Student finances office is located on the first floor." +
                            " Take the lift or stairs from ground floor and then take a right, keep walking and you will see them on the left hand side");
                    Utils.makeText(this, "Student Finance");

                    break;
                case 2:
                    spinner2.setVisibility(View.INVISIBLE);
                    roomSelection.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.VISIBLE);
                    floorImg.setImageResource(R.drawable.firstfloor);
                    description.setText("Student well-fare can be found on the first floor" +
                            " Take the lift or stairs from the ground floor and then take a right, keep walking until the end of the corridor" +
                            "where you will see the student well-being office");
                    Utils.makeText(this, "Student Well-being");

                    break;
                case 3:

                    Utils.makeText(this, "Floors");
                    roomSelection.setVisibility(View.VISIBLE);
                    spinner2.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    adapter1 = ArrayAdapter.createFromResource(FloorPlan.this, R.array.FloorSelect, R.layout.support_simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter1);
                    break;

            }
        } else if (parent.getId() == R.id.RoomsSpinner) {

            switch (position) {
                case 0:
                    floorImg.setImageResource(R.drawable.firstfloor);
                    description.setText("First Floor");
                    break;

                case 1:
                    floorImg.setImageResource(R.drawable.f101);
                    description.setText("Room 101 is next to the Student finance office");
                    break;

                case 2:
                    floorImg.setImageResource(R.drawable.f102);
                    description.setText("Room 102 is opposite of the Student well-being ");

                    break;

                case 3:
                    floorImg.setImageResource(R.drawable.f103);
                    description.setText("Room 103 is opposite of the finance meeting room");
                    break;
                case 4:
                    floorImg.setImageResource(R.drawable.secondfloor);
                    description.setText("Second Floor ");

                    break;
                case 5:
                    floorImg.setImageResource(R.drawable.f201);
                    description.setText("Room 201 is on the left side of the corridor next to the break out area of the second floor");

                    break;
                case 6:
                    floorImg.setImageResource(R.drawable.f202);
                    description.setText("Room 202 and 203 are next to 201");

                    break;
                case 7:
                    floorImg.setImageResource(R.drawable.f204);
                    description.setText("Room 204 is opposite of 202 and 203");

                    break;
                case 8:
                    floorImg.setImageResource(R.drawable.f205);
                    description.setText("Room 205 is opposite of 202 and 203");

                    break;
                case 9:
                    floorImg.setImageResource(R.drawable.thirdfloor);
                    description.setText("Third Floor");

                    break;
                case 10:
                    floorImg.setImageResource(R.drawable.f301);
                    description.setText("Room 301 is in front of the lifts");

                    break;
                case 11:
                    floorImg.setImageResource(R.drawable.f302);
                    description.setText("Room 302 is next to 301");

                    break;
                case 12:
                    floorImg.setImageResource(R.drawable.f303);
                    description.setText("Room 303 is next to 302");

                    break;
                case 13:
                    floorImg.setImageResource(R.drawable.f304);
                    description.setText("Room 303 is next to 304");

                    break;
                case 14:
                    floorImg.setImageResource(R.drawable.f305);
                    description.setText("Room 305 is opposite of 304 ");

                    break;
                case 15:
                    floorImg.setImageResource(R.drawable.f306);
                    description.setText("Room 306 is opposite of 303");
                    break;
                case 16:
                    floorImg.setImageResource(R.drawable.groundfloor);
                    description.setText("Ground Floor");
                    break;
                case 17:
                    floorImg.setImageResource(R.drawable.g1);
                    description.setText("Room G01 is on the right hand side before Admissions office ");
                    break;
                case 18:
                    floorImg.setImageResource(R.drawable.g2);
                    description.setText("Room G02 is next to G01");
                    break;
                case 19:
                    floorImg.setImageResource(R.drawable.lowegrofloor1);
                    description.setText("Lower Ground Floor 1");
                    break;
                case 20:
                    floorImg.setImageResource(R.drawable.lg1);
                    description.setText("LG01 is in front of the lifts");
                    break;
                case 21:
                    floorImg.setImageResource(R.drawable.lg23);
                    description.setText("LG02 and LG03 are next to LG01");

                    break;
                case 22:
                    floorImg.setImageResource(R.drawable.lg4);
                    description.setText("LG04 is opposite of LG03");

                    break;
                case 23:
                    floorImg.setImageResource(R.drawable.lg5);
                    description.setText("LG05 is opposite of LG02");
                    break;
                case 24:
                    floorImg.setImageResource(R.drawable.lowgrofloor2);
                    description.setText("Lower Ground Floor 2");
                    break;
                case 25:
                    floorImg.setImageResource(R.drawable.a1);
                    description.setText("Auditorium 1 is on the right hand side as you walk out of the lifts.");
                    break;
                case 26:
                    floorImg.setImageResource(R.drawable.a2);
                    description.setText("Auditorium 2 is next to Auditorium 1");
                    break;
            }

        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
