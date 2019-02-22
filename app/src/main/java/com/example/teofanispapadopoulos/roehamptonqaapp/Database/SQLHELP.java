package com.example.teofanispapadopoulos.roehamptonqaapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;


public class SQLHELP extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "local_Roehampton_DB";
    public static final int DATABASE_VERSION = 1;
    public static final String createUserTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.User.TABLE_NAME +
            "(" + Contract.User.STUDENT_ID + " INTEGER PRIMARY KEY autoincrement, " +
            Contract.User.FIRST_NAME + " varchar(45) NOT NULL, " +
            Contract.User.SURNAME + " varchar(45) NOT NULL, " +
            Contract.User.EMAIL + " varchar(45) NOT NULL, " +
            Contract.User.GROUP + " varchar(45) NOT NULL, " +
            Contract.User.PASSWORD + " varchar(45) NOT NULL, " +
            Contract.User.PROFILE_PICTURE + "BLOB ," +
            "FOREIGN KEY (_group_) REFERENCES _groups_ (_group_id_), " +
            "UNIQUE (_email_));";
    public static final String createAuthorTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Author.TABLE_NAME +
            "(" + Contract.Author.AUTHOR_ID + " INTEGER PRIMARY KEY autoincrement, " +
            Contract.Author.FIRST_NAME + " varchar(45) NOT NULL, " +
            Contract.Author.SURNAME + " varchar(45) NOT NULL );";
    public static final String createBookTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Book.TABLE_NAME +
            "(" + Contract.Book.ISBN + " varchar(45) PRIMARY KEY, " +
            Contract.Book.TITLE + " varchar(45) NOT NULL, " +
            Contract.Book.EDITION + " int(11) NOT NULL, " +
            Contract.Book.CATEGORY_TYPE + " varchar(45) NOT NULL, " +
            Contract.Book.IMAGE + " BLOB ," +
            "FOREIGN KEY (_category_type_) REFERENCES _category_ (_category_type_));";
    public static final String createCategoryTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Category.TABLE_NAME +
            "(" + Contract.Category.CATEGORY_TYPE + " varchar(45) PRIMARY KEY );";
    public static final String createClassesTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Classes.TABLE_NAME +
            "(" + Contract.Classes.GROUP_ID + " varchar(45) NOT NULL, " +
            Contract.Classes.DATE + " date NOT NULL, " +
            Contract.Classes.MODULE_CODE + " int(11) NOT NULL, " +
            Contract.Classes.TIME + " time NOT NULL, " +
            "FOREIGN KEY (_group_id_) REFERENCES _groups_ (_group_id_), " +
            "FOREIGN KEY (_module_code_) REFERENCES _module_ (_module_code_));";
    public static final String createWrittenTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.hasWritten.TABLE_NAME +
            "(" + Contract.hasWritten.BOOK_ID + " varchar(45) PRIMARY KEY, " +
            Contract.hasWritten.AUTHOR_ID + " int(11) NOT NULL, " +
            "FOREIGN KEY (_author_id_) REFERENCES _author_ (_author_id_), " +
            "FOREIGN KEY (_book_id_) REFERENCES _book_ (_book_id_));";
    public static final String createModuleTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Module.TABLE_NAME +
            "(" + Contract.Module.MODULE_CODE + " INTEGER PRIMARY KEY, " +
            Contract.Module.MODULE_NAME + " varchar(45) NOT NULL );";
    public static final String createReviewsTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Reviews.TABLE_NAME +
            "(" + Contract.Reviews.REVIEW_ID + " INTEGER PRIMARY KEY, " +
            Contract.Reviews.USER_ID + " int(11) NOT NULL, " +
            Contract.Reviews.LIKE_COUNT + " int(11) NOT NULL, " +
            Contract.Reviews.LIKED_BY + " int(11) NOT NULL, " +
            Contract.Reviews.REVIEW + " varchar(200) NOT NULL, " +
            Contract.Reviews.BOOK_ID + " varchar(45) NOT NULL, " +
            "FOREIGN KEY (_book_id_) REFERENCES _book_(_book_id_), " +
            "FOREIGN KEY (_user_id_) REFERENCES _user_(_user_id_));";
    public static final String createGroupsTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Groups.TABLE_NAME +
            "(" + Contract.Groups.GROUP_ID + " varchar(45) PRIMARY KEY );";
    public static final String createReservationsTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Reservations.TABLE_NAME +
            "(" + Contract.Reservations.RESERVATION_ID + " INTEGER PRIMARY KEY autoincrement," +
            Contract.Reservations.BOOK_ID + " varchar(45) NOT NULL, " +
            Contract.Reservations.USER_ID + " int(11) NOT NULL, " +
            Contract.Reservations.AUTHOR_NAME + " varchar(45) NOT NULL, " +
            Contract.Reservations.BOOKED_ON + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
    public static final String createLikesTableQuery = "CREATE TABLE IF NOT EXISTS " +
            Contract.Likes.TABLE_NAME +
            "(" + Contract.Likes.LIKES_ID + " INTEGER PRIMARY KEY autoincrement, " +
            Contract.Likes.REVIEW_ID + " int(11) NOT NULL, " +
            Contract.Likes.USER_ID + " int(11) NOT NULL);";

    public SQLHELP(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createUserTableQuery);
        db.execSQL(createAuthorTableQuery);
        db.execSQL(createBookTableQuery);
        db.execSQL(createCategoryTableQuery);
        db.execSQL(createClassesTableQuery);
        db.execSQL(createWrittenTableQuery);
        db.execSQL(createModuleTableQuery);
        db.execSQL(createReviewsTableQuery);
        db.execSQL(createGroupsTableQuery);
        db.execSQL(createReservationsTableQuery);
        db.execSQL(createLikesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor authenticate(SQLiteDatabase db, String em[]) {

        String selection = Contract.User.EMAIL + "=?";
        String[] loginCols = {Contract.User.EMAIL, Contract.User.PASSWORD, Contract.User.GROUP, Contract.User.STUDENT_ID, Contract.User.PROFILE_PICTURE};
        Cursor cursor = db.query(Contract.User.TABLE_NAME, loginCols,
                selection, em, null, null, null);
        return cursor;
    }

    public Cursor getBooksAuthor(SQLiteDatabase db, String bookId[]) {

        String selection = Contract.hasWritten.BOOK_ID + "=?";
        String[] projection = {Contract.hasWritten.BOOK_ID, Contract.hasWritten.AUTHOR_ID};
        Cursor cursor = db.query(Contract.hasWritten.TABLE_NAME, projection, selection, bookId, null, null, null);

        return cursor;
    }

    public Cursor getAuthor(SQLiteDatabase db, String authId[]) {
        String selection = Contract.Author.AUTHOR_ID + "=?";
        String[] projection = {Contract.Author.AUTHOR_ID, Contract.Author.FIRST_NAME, Contract.Author.SURNAME};
        Cursor cursor = db.query(Contract.Author.TABLE_NAME, projection, selection, authId, null, null, null);
        return cursor;
    }
    public void uploadProfilePicture(SQLiteDatabase db, String Student_id,  String image){
        String selection = Contract.User.STUDENT_ID + "=?";
        String[] projection = {Student_id};
        ContentValues cv = new ContentValues();
        cv.put(Contract.User.PROFILE_PICTURE, image);
        db.update(Contract.User.TABLE_NAME, cv, selection, projection);
    }
    public void addLike(SQLiteDatabase db, String reviewID, String like) {
        String selection = Contract.Reviews.REVIEW_ID + "=?";
        String[] projection = {reviewID};
        ContentValues cv = new ContentValues();
        cv.put(Contract.Reviews.LIKE_COUNT, like);
        cv.put(Contract.Reviews.REVIEW_ID, reviewID);
        db.update(Contract.Reviews.TABLE_NAME, cv, selection, projection);
    }

    public void addLikesSaved(SQLiteDatabase db, String reviewId, String likedby) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.Likes.REVIEW_ID, reviewId);
        cv.put(Contract.Likes.USER_ID, likedby);
        db.insert(Contract.Likes.TABLE_NAME, null, cv);
    }

    public void removeLike(SQLiteDatabase db, String reviewId, String likedby) {
        String selection = Contract.Likes.REVIEW_ID + "=?" + " AND " + Contract.Likes.USER_ID + "=?";
        String[] selectionArgs = {reviewId, likedby};
        db.delete(Contract.Likes.TABLE_NAME, selection, selectionArgs);
    }

    public void addReview(SQLiteDatabase db, Integer id, String Review, String bookId) {

        ContentValues cv = new ContentValues();

        cv.put(Contract.Reviews.USER_ID, id);
        cv.put(Contract.Reviews.REVIEW, Review);
        cv.put(Contract.Reviews.BOOK_ID, bookId);

        db.insert(Contract.Reviews.TABLE_NAME, null, cv);
    }

    public void updatePassword(SQLiteDatabase db, String email, String password) {

        String selection = Contract.User.EMAIL + "=?";
        String[] projection = {email};
        ContentValues cv = new ContentValues();
        cv.put(Contract.User.PASSWORD, password);
        db.update(Contract.User.TABLE_NAME, cv, selection, projection);

    }

    public void updateUserEmail(SQLiteDatabase db, String id, String email) {
        String selection = Contract.User.STUDENT_ID + "=?";
        String[] projection = {id};
        ContentValues cv = new ContentValues();
        cv.put(Contract.User.EMAIL, email);
        db.update(Contract.User.TABLE_NAME, cv, selection, projection);
    }

    public Cursor getUserName(SQLiteDatabase db, String filter[]) {

        String selection = Contract.User.STUDENT_ID + "=?";
        String[] projection = {Contract.User.STUDENT_ID, Contract.User.FIRST_NAME, Contract.User.SURNAME};
        Cursor cursor = db.query(Contract.User.TABLE_NAME, projection, selection, filter, null, null, null);
        return cursor;
    }

    public void ReserveBook(SQLiteDatabase db, String bookId, String userId, String authorName) {

        ContentValues cv = new ContentValues();
        cv.put(Contract.Reservations.BOOK_ID, bookId);
        cv.put(Contract.Reservations.USER_ID, userId);
        cv.put(Contract.Reservations.AUTHOR_NAME, authorName);
        db.insert(Contract.Reservations.TABLE_NAME, null, cv);
    }

    public void CancelReservation(SQLiteDatabase db, String bookId) {
        String selection = Contract.Reservations.BOOK_ID + "=?";
        String[] selectionArgs = {bookId};
        db.delete(Contract.Reservations.TABLE_NAME, selection, selectionArgs);
    }

    public void Register(SQLiteDatabase db, String firstName, String surname, String email, String group, String password) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.User.FIRST_NAME, firstName);
        cv.put(Contract.User.SURNAME, surname);
        cv.put(Contract.User.EMAIL, email);
        cv.put(Contract.User.GROUP, group);
        cv.put(Contract.User.PASSWORD, password);
        db.insert(Contract.User.TABLE_NAME, null, cv);
    }

    public Cursor getModuleName(SQLiteDatabase db, String code[]) {
        String selection = Contract.Module.MODULE_CODE + "=?";
        String projection[] = {Contract.Module.MODULE_NAME};

        Cursor cursor = db.query(Contract.Module.TABLE_NAME, projection, selection, code, null, null, null);

        return cursor;
    }

    public Cursor getTimeTable(SQLiteDatabase db, String details[]) {

        String seletion = Contract.Classes.GROUP_ID + "=?";
        String projection[] = {Contract.Classes.DATE, Contract.Classes.TIME, Contract.Classes.GROUP_ID, Contract.Classes.MODULE_CODE};

        Cursor cursor = db.query(Contract.Classes.TABLE_NAME, projection, seletion, details, null, null, null);
        return cursor;
    }


}