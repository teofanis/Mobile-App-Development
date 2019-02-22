package com.example.teofanispapadopoulos.roehamptonqaapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Activities.bookActivity;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context c;
    private Cursor cursor;


    public ItemAdapter(Context c, Cursor cursor) {
        this.c = c;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View activeView = inflater.inflate(R.layout.activity_item_layout, null);
        return new ItemViewHolder(activeView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, final int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        String title = cursor.getString(cursor.getColumnIndex(Contract.Book.TITLE));
        String category = cursor.getString(cursor.getColumnIndex(Contract.Book.CATEGORY_TYPE));
        String edition = cursor.getString(cursor.getColumnIndex(Contract.Book.EDITION));
        String ISBN = cursor.getString(cursor.getColumnIndex(Contract.Book.ISBN));
        String imageSrc = cursor.getString(cursor.getColumnIndex(Contract.Book.IMAGE));

        final byte[] decodeString = Base64.decode(imageSrc, Base64.DEFAULT);
        final Bitmap image = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        itemViewHolder.icon.setImageBitmap(image);

        itemViewHolder.tv_title.setText(title);
        itemViewHolder.tv_category.setText(category);
        itemViewHolder.tv_edition.setText(edition);
        itemViewHolder.tv_ISBN.setText(ISBN);

        itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent itemActivity = new Intent(c, bookActivity.class);
                itemActivity.putExtra("Title", itemViewHolder.tv_title.getText().toString());
                itemActivity.putExtra("ISBN", itemViewHolder.tv_ISBN.getText().toString());
                itemActivity.putExtra("Image", decodeString);
                Utils.makeText(c, itemViewHolder.tv_title.getText().toString());
                c.startActivity(itemActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();

    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView tv_title, tv_edition, tv_category, tv_ISBN;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.bookIcon);
            tv_title = itemView.findViewById(R.id.bookTitle);
            tv_edition = itemView.findViewById(R.id.bookEdition);
            tv_category = itemView.findViewById(R.id.bookCategory);
            tv_ISBN = itemView.findViewById(R.id.bookISBN);
            cardView = itemView.findViewById(R.id.cardId);

        }


    }
}
