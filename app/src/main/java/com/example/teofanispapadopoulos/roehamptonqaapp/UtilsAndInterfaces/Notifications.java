package com.example.teofanispapadopoulos.roehamptonqaapp.UtilsAndInterfaces;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notifications extends Application {
    public static final String CHANNEL_1_ID = "Bookings";
    public static final String CHANNEL_2_ID = "Generic";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel book_channel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Booking Confirmation",
                    NotificationManager.IMPORTANCE_HIGH
            );
            book_channel.setDescription("This is the book channel handling, book reservations");

            NotificationChannel generic_channel = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            generic_channel.setDescription("This is a generic channel handling, reminders");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(book_channel);
            manager.createNotificationChannel(generic_channel);

        }

    }
}
