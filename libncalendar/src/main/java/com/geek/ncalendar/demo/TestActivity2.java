package com.geek.ncalendar.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.calendar.MonthCalendar;
import com.geek.ncalendar.enumeration.DateChangeBehavior;
import com.geek.ncalendar.listener.OnCalendarChangedListener;

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
