package com.plants; // Replace with your package name

import android.widget.TimePicker;
import java.util.Locale;

public class TimeUtils {
    public static String getTimeStringFromTimePicker(TimePicker timePicker) {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();


        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public static String getTimeStringFromTimePickerOld(TimePicker timePicker) {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }
}