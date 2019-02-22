package com.example.teofanispapadopoulos.roehamptonqaapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.teofanispapadopoulos.roehamptonqaapp.Activities.Library;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.Contract;
import com.example.teofanispapadopoulos.roehamptonqaapp.Database.SQLHELP;
import com.example.teofanispapadopoulos.roehamptonqaapp.R;
import com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces.Utils;

import java.util.HashMap;
import java.util.Map;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.reviewViewHolder> {

    Map<String, String> likes_map = new HashMap<>();
    private Context context;
    private Cursor cursor;

    public reviewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public reviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.activity_reviews, null);
        return new reviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final reviewViewHolder reviewViewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }

        final String review = cursor.getString(cursor.getColumnIndex(Contract.Reviews.REVIEW));
        final String reviewBookId = cursor.getString(cursor.getColumnIndex(Contract.Reviews.BOOK_ID));
        final String userNameID = cursor.getString(cursor.getColumnIndex(Contract.Reviews.USER_ID));
        final String reviewId = cursor.getString(cursor.getColumnIndex(Contract.Reviews.REVIEW_ID));
        final String likeCount = cursor.getString(cursor.getColumnIndex(Contract.Reviews.LIKE_COUNT));
        final String likedBy = cursor.getString(cursor.getColumnIndex(Contract.Reviews.LIKED_BY));


        final SQLHELP tempConnection = new SQLHELP(context);
        SQLiteDatabase mydb = tempConnection.getReadableDatabase();
        String filter[] = {userNameID};

        Cursor _cursor = tempConnection.getUserName(mydb, filter);
        if (_cursor.moveToFirst()) {
            while (!_cursor.isAfterLast()) {

                String firstName = _cursor.getString(_cursor.getColumnIndex(Contract.User.FIRST_NAME));
                String surname = _cursor.getString(_cursor.getColumnIndex(Contract.User.SURNAME));

                String fullName = firstName + " " + surname;

                reviewViewHolder.tv_userID.setText(fullName);

                _cursor.moveToNext();
            }

        } else {
            Utils.makeText(context, "Cursor is Empty ?!");
        }
        _cursor.close();
        tempConnection.close();

        /*
         * QUERY FOR LIKED REVIEWS START
         */

        final SQLHELP conforLikes = new SQLHELP(context);
        final SQLiteDatabase likedb = conforLikes.getWritableDatabase();
        String[] args = {Library.activeStudentID};
        Cursor likeCursor = likedb.query(Contract.Likes.TABLE_NAME,

                null,
                Contract.Likes.USER_ID + "=?",
                args,
                null,
                null,
                null);

        if (!likeCursor.moveToFirst()) {
            likeCursor.close();
        } else {
            while (!likeCursor.isAfterLast()) {
                String review_id_ = likeCursor.getString(likeCursor.getColumnIndex(Contract.Likes.REVIEW_ID));
                String user_id_ = likeCursor.getString(likeCursor.getColumnIndex(Contract.Likes.USER_ID));

                if ((Integer.parseInt(review_id_) == Integer.parseInt(reviewId) && user_id_.compareToIgnoreCase(Library.activeStudentID) == 0)) {

                    reviewViewHolder.likeButton.setImageResource(R.drawable.thumbup);
                    reviewViewHolder.likeButton.setTag("Liked");
                    likes_map.put(review_id_, user_id_);


                } else {
                    reviewViewHolder.likeButton.setTag("NotLiked");
                }
                likeCursor.moveToNext();
            }
            likeCursor.close();
            conforLikes.close();
        }



        /*
         * QUERY FOR LIKED REVIEWS END
         */

        reviewViewHolder.tv_review_Content.setText(review);
        reviewViewHolder.tv_review_Content.setTag(reviewId);
        reviewViewHolder.tv_like_counter.setText(likeCount);


        reviewViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLHELP doCon = new SQLHELP(context);
                SQLiteDatabase dbDO = doCon.getWritableDatabase();


                if (likes_map.containsKey(reviewViewHolder.tv_review_Content.getTag().toString())) {

                    int currentLikes = Integer.parseInt(reviewViewHolder.tv_like_counter.getText().toString());
                    currentLikes--;
                    doCon.addLike(dbDO, reviewViewHolder.tv_review_Content.getTag().toString(), String.valueOf(currentLikes));
                    doCon.removeLike(dbDO, reviewViewHolder.tv_review_Content.getTag().toString(), Library.activeStudentID);
                    reviewViewHolder.likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    Utils.makeText(context, "Like has been removed");
                    reviewViewHolder.tv_like_counter.setText(String.valueOf(currentLikes));
                    reviewViewHolder.likeButton.setTag("NotLiked");
                    likes_map.remove(reviewViewHolder.tv_review_Content.getTag().toString());

                } else {

                    int currentLikes = Integer.parseInt(reviewViewHolder.tv_like_counter.getText().toString());
                    currentLikes++;
                    doCon.addLike(dbDO, reviewViewHolder.tv_review_Content.getTag().toString(), String.valueOf(currentLikes));
                    doCon.addLikesSaved(dbDO, reviewViewHolder.tv_review_Content.getTag().toString(), Library.activeStudentID);
                    reviewViewHolder.likeButton.setImageResource(R.drawable.thumbup);
                    likes_map.put(reviewViewHolder.tv_review_Content.getTag().toString(), String.valueOf(currentLikes));
                    Utils.makeText(context, "Liked");
                    reviewViewHolder.tv_like_counter.setText(String.valueOf(currentLikes));
                    reviewViewHolder.likeButton.setTag("Liked");
                }


                doCon.close();


            }
        });

    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class reviewViewHolder extends RecyclerView.ViewHolder {

        TextView tv_userID, tv_review_Content, tv_like_counter;
        CardView reviewCardView;
        ImageButton likeButton;

        public reviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_userID = itemView.findViewById(R.id.userReviewId);
            tv_review_Content = itemView.findViewById(R.id.reviewContent);
            tv_like_counter = itemView.findViewById(R.id.likeCount);
            likeButton = itemView.findViewById(R.id.likeButton);
            reviewCardView = itemView.findViewById(R.id.reviewCard);


        }
    }
}
