package com.example.teofanispapadopoulos.roehamptonqaapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private Context c;
    private Cursor cursorz;


    public BookingAdapter(Context c, Cursor cursor) {
        this.c = c;
        this.cursorz = cursor;
    }


    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View activeView = inflater.inflate(R.layout.reserved_book_, null);
        return new ViewHolder(activeView);

    }

    @Override
    public void onBindViewHolder(@NonNull final BookingAdapter.ViewHolder viewHolder, final int i) {
        if (!cursorz.moveToPosition(i)) {
            return;
        }

        final String book_title_ = cursorz.getString(cursorz.getColumnIndex(Contract.Reservations.BOOK_ID));
        String book_author_ = cursorz.getString(cursorz.getColumnIndex(Contract.Reservations.AUTHOR_NAME));
        String book_bookedOn_ = cursorz.getString(cursorz.getColumnIndex(Contract.Reservations.BOOKED_ON));
        long id = cursorz.getInt(cursorz.getColumnIndex(Contract.Reservations.RESERVATION_ID));


        final SQLHELP tempConnection = new SQLHELP(c);
        final SQLiteDatabase mydb = tempConnection.getReadableDatabase();
        String filter[] = {book_title_};

        Cursor _cursor = mydb.query(Contract.Book.TABLE_NAME,
                null,
                Contract.Book.TITLE + "=?",
                filter,
                null,
                null,
                null
        );

        if (_cursor.moveToFirst()) {
            while (!_cursor.isAfterLast()) {

                String ISBN = _cursor.getString(_cursor.getColumnIndex(Contract.Book.ISBN));
                String imageSrc = _cursor.getString(_cursor.getColumnIndex(Contract.Book.IMAGE));


                final byte[] decodeString = Base64.decode(imageSrc, Base64.DEFAULT);
                final Bitmap image = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);

                viewHolder.bookLogo.setImageBitmap(image);
                viewHolder.book_isbn.setText(ISBN);
                viewHolder.itemView.setTag(id);

                _cursor.moveToNext();
            }

        } else {
            Utils.makeText(c, "Cursor is Empty ?!");
        }
        _cursor.close();
        tempConnection.close();

        viewHolder.book_title.setText(book_title_);
        viewHolder.book_author.setText(book_author_);
        viewHolder.book_bookedOn.setText(book_bookedOn_);


        viewHolder.book_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(c)
                        .setIcon(R.drawable.dialog_warning)
                        .setTitle("Amend booking")
                        .setMessage("Are you sure you want to cancel your booking for "
                                + viewHolder.book_title.getText().toString())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase dbz = tempConnection.getWritableDatabase();
                                tempConnection.CancelReservation(dbz, viewHolder.book_title.getText().toString());
                                tempConnection.close();
                                viewHolder.book_cancel.setText("Canceled");
                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Utils().showDialog(c, "Amend booking", "Your booking has not been canceled");

                            }
                        })
                        .show();


            }
        });

    }

    @Override
    public int getItemCount() {

        return cursorz.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView bookLogo;
        private TextView book_title, book_bookedOn, book_author, book_isbn;
        private Button book_cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookLogo = itemView.findViewById(R.id.bookingIcon);
            book_title = itemView.findViewById(R.id.bookingTitle);
            book_bookedOn = itemView.findViewById(R.id.bookedOn);
            book_author = itemView.findViewById(R.id.bookingAuthor);
            book_isbn = itemView.findViewById(R.id.bookingISBN);
            book_cancel = itemView.findViewById(R.id.cancelBooking);


        }
    }
}
