package com.geek.libncalendar.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.libncalendar.R;
import com.geek.libncalendar.calendar.BaseCalendar;
import com.geek.libncalendar.calendar.MonthCalendar;
import com.geek.libncalendar.enumeration.DateChangeBehavior;
import com.geek.libncalendar.listener.OnCalendarChangedListener;

import org.joda.time.LocalDate;

/**
 * Created by necer on 2020/3/24.
 */
public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


        MonthCalendar monthCalendar = findViewById(R.id.monthCalendar);
        monthCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {

            }
        });

    }
}
