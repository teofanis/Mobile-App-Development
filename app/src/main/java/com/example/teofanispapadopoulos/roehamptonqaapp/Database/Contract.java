package com.example.teofanispapadopoulos.roehamptonqaapp.Database;

public final class Contract {

    private Contract() {
    }


    public static class User {

        public static final String TABLE_NAME = "_user_";
        public static final String STUDENT_ID = "_Student_id_";
        public static final String FIRST_NAME = "_first_name_";
        public static final String SURNAME = "_surname_";
        public static final String EMAIL = "_email_";
        public static final String GROUP = "_group_";
        public static final String PASSWORD = "_password_";
        public static final String PROFILE_PICTURE  = "_profile_picture_";
    }

    public static class Author {
        public static final String TABLE_NAME = "_author_";
        public static final String AUTHOR_ID = "_author_id_";
        public static final String FIRST_NAME = "_first_name_";
        public static final String SURNAME = "_surname_";
    }

    public static class Book {
        public static final String TABLE_NAME = "_book_";
        public static final String ISBN = "_ISBN_";
        public static final String TITLE = "_title_";
        public static final String EDITION = "_edition_";
        public static final String CATEGORY_TYPE = "_category_type_";
        public static final String IMAGE = "_image_";
    }

    public static class Category {
        public static final String TABLE_NAME = "_category_";
        public static final String CATEGORY_TYPE = "_category_type_";
    }

    public static class Classes {
        public static final String TABLE_NAME = "_classes_";
        public static final String GROUP_ID = "_group_id_";
        public static final String DATE = "_date_";
        public static final String MODULE_CODE = "_module_code_";
        public static final String TIME = "_time_";
    }

    public static class hasWritten {
        public static final String TABLE_NAME = "_has_written_";
        public static final String BOOK_ID = "_book_id_";
        public static final String AUTHOR_ID = "_author_id_";
    }

    public static class Module {
        public static final String TABLE_NAME = "_module_";
        public static final String MODULE_CODE = "_module_code_";
        public static final String MODULE_NAME = "_module_name_";
    }

    public static class Reviews {
        public static final String TABLE_NAME = "_reviews_";
        public static final String REVIEW_ID = "_review_id_";
        public static final String USER_ID = "_user_id_";
        public static final String LIKE_COUNT = "_like_count_";
        public static final String LIKED_BY = "_liked_by_";
        public static final String REVIEW = "_review_";
        public static final String BOOK_ID = "_book_id_";
    }

    public static class Groups {
        public static final String TABLE_NAME = "_groups_";
        public static final String GROUP_ID = "_group_id_";
    }

    public static class Reservations {
        public static final String TABLE_NAME = "_reservations_";
        public static final String RESERVATION_ID = "_reservation_id_";
        public static final String BOOK_ID = "_book_id_";
        public static final String USER_ID = "_user_id_";
        public static final String AUTHOR_NAME = "_author_name_";
        public static final String BOOKED_ON = "_booked_on_";

    }

    public static class Likes {
        public static final String TABLE_NAME = "_likes_";
        public static final String LIKES_ID = "_likes_id_";
        public static final String REVIEW_ID = "_reviews_id_";
        public static final String USER_ID = "_user_id_";
    }


}
