package com.example.teofanispapadopoulos.roehamptonqaapp.Activities;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Adapters.reviewAdapter;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.mDialog;

import static com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Notifications.CHANNEL_1_ID;


public class bookActivity extends AppCompatActivity implements View.OnClickListener, mDialog.mDialogListener {
    public boolean isAlreadyReserved = false;
    RecyclerView Reviews;
    com.example.teofanispapadopoulos.roehamptonqaapp.Adapters.reviewAdapter reviewAdapter;
    SQLiteDatabase db;
    private TextView tv_BookTitle, tv_author, tv_ISBN;
    private ImageView logo;
    private Button reserve, review;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        notificationManager = NotificationManagerCompat.from(this);

        Instanciate();
        getIntents();

        SQLHELP connection = new SQLHELP(bookActivity.this);
        db = connection.getReadableDatabase();

        reviewAdapter = new reviewAdapter(this, getReviews());
        Reviews.setAdapter(reviewAdapter);


        checkReservedBookState();

        reserve.setOnClickListener(this);
        review.setOnClickListener(this);


    }

    private void getIntents() {
        Intent itemViewInformation = getIntent();
        String title = itemViewInformation.getExtras().getString("Title");
        String ISBN = itemViewInformation.getExtras().getString("ISBN");
        byte[] bytes = getIntent().getByteArrayExtra("Image");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        tv_BookTitle.setText(title);
        logo.setImageBitmap(bmp);
        tv_ISBN.setText(ISBN);
        getAuthor(ISBN);
    }

    private void getAuthor(String ISBN) {
        SQLHELP connection = new SQLHELP(bookActivity.this);
        SQLiteDatabase db = connection.getReadableDatabase();

        String bookId[] = {ISBN};
        Cursor _cursor = connection.getBooksAuthor(db, bookId);
        if (_cursor.moveToFirst()) {
            while (!_cursor.isAfterLast()) {

                String _authorid = _cursor.getString(_cursor.getColumnIndex(Contract.hasWritten.AUTHOR_ID));
                String _bookid = _cursor.getString(_cursor.getColumnIndex(Contract.hasWritten.BOOK_ID));

                String authId[] = {_authorid};
                Cursor _cursorz = connection.getAuthor(db, authId);

                if (_cursorz.moveToNext()) {
                    while (!_cursorz.isAfterLast()) {
                        String aFirstName = _cursorz.getString(_cursorz.getColumnIndex(Contract.Author.FIRST_NAME));
                        String aSurname = _cursorz.getString(_cursorz.getColumnIndex(Contract.Author.SURNAME));

                        String name = aFirstName + " " + aSurname;

                        tv_author.setText(name);


                        _cursorz.moveToNext();
                    }
                }
                _cursorz.close();

                _cursor.moveToNext();
            }
            _cursor.close();
            connection.close();

        }


    }


    private Cursor getReviews() {
        String ISBN[] = {tv_ISBN.getText().toString()};
        return db.query(
                Contract.Reviews.TABLE_NAME,
                null,
                Contract.Reviews.BOOK_ID + "=?",
                ISBN,
                null,
                null,
                Contract.Reviews.LIKE_COUNT + " DESC"
        );
    }

    private void Instanciate() {

        Reviews = findViewById(R.id.RecyclerViewReview);
        Reviews.setHasFixedSize(true);
        Reviews.setLayoutManager(new LinearLayoutManager(this));

        tv_BookTitle = findViewById(R.id.titles);
        logo = findViewById(R.id.bookLogo);
        reserve = findViewById(R.id.btn_reserve);
        tv_author = findViewById(R.id.bookAuthor);
        review = findViewById(R.id.btn_review);
        tv_ISBN = findViewById(R.id.tvBookISBN);


    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reserve:
                if (!isAlreadyReserved) {
                    reserveBook();
                    sendBookingConfirmation(v);
                } else {
                    Utils.makeText(bookActivity.this, "You've already RESERVED that book");
                }
                break;

            case R.id.btn_review:
                openDialog();
                break;

        }
    }


    private void openDialog() {
        mDialog myDialog = new mDialog();
        myDialog.show(getSupportFragmentManager(), "mDialog");
    }

    public void sendBookingConfirmation(View v) {
        String reserveTitle = tv_BookTitle.getText().toString();
        String reserveISBN = tv_ISBN.getText().toString();
        /*
        Bitmap reserveLogo = ((BitmapDrawable) logo.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        reserveLogo.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteImg = stream.toByteArray();

        */
        Intent tapped = new Intent(this, manage_bookings.class);
        tapped.putExtra("Reserved", reserveTitle);

        PendingIntent tappedPendingIntent = PendingIntent.getActivity(this, 1, tapped, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setContentTitle("Booking confirmation")
                .setContentText("You have successfully reserved " + reserveTitle +
                        " with ISBN: " + reserveISBN)
                .setContentIntent(tappedPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();


        notificationManager.notify("Booking Confirmation", 1, notification);

    }


    public void reserveBook() {

        isAlreadyReserved = true;
        updateBookState();
        SQLHELP connection = new SQLHELP(bookActivity.this);
        SQLiteDatabase db = connection.getWritableDatabase();
        connection.ReserveBook(db, tv_BookTitle.getText().toString().trim(), Library.activeStudentID, tv_author.getText().toString().trim());
        connection.close();

    }

    public void checkReservedBookState() {

        SQLHELP connection = new SQLHELP(bookActivity.this);
        SQLiteDatabase db = connection.getReadableDatabase();
        String arguments[] = {tv_BookTitle.getText().toString(), Library.activeStudentID};
        Cursor _cursor = db.query(Contract.Reservations.TABLE_NAME,
                null,
                (Contract.Reservations.BOOK_ID + "=?" + " AND " + Contract.Reservations.USER_ID + "=?"),
                arguments,
                null,
                null,
                null
        );
        if (_cursor.moveToFirst()) {
            while (!_cursor.isAfterLast()) {

                String _book_Title = _cursor.getString(_cursor.getColumnIndex(Contract.Reservations.BOOK_ID));
                String _user_id_ = _cursor.getString(_cursor.getColumnIndex(Contract.Reservations.USER_ID));

                if (_book_Title.compareToIgnoreCase(tv_BookTitle.getText().toString().trim()) == 0 &&
                        _user_id_.equals(Library.activeStudentID)) ;
                {
                    isAlreadyReserved = true;
                    updateBookState();
                }


                _cursor.moveToNext();
            }
        }
        connection.close();
    }

    public void updateBookState() {
        reserve.setAllCaps(true);
        reserve.setText("Reserved");
    }


    @Override
    public void applyTexts(String contentReview) {

        SQLHELP conObject = new SQLHELP(bookActivity.this);
        SQLiteDatabase dbz = conObject.getWritableDatabase();

        String review = contentReview;
        String bookId = tv_ISBN.getText().toString();
        int id = Integer.parseInt(Library.activeStudentID);

        conObject.addReview(dbz, id, review, bookId);
        conObject.close();

        Utils.makeText(this, "Thank you ! Your review has been added !");
        reviewAdapter = new reviewAdapter(bookActivity.this, getReviews());
        Reviews.setAdapter(reviewAdapter);
    }


}
