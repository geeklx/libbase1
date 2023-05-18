package com.geek.ncalendar.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.calendar.WeekCalendar;
import com.geek.ncalendar.enumeration.DateChangeBehavior;
import com.geek.ncalendar.listener.OnCalendarChangedListener;
import com.geek.ncalendar.listener.OnCalendarMultipleChangedListener;

import org.joda.time.LocalDate;

import java.util.List;

public class TestWeekActivity extends BaseActivity {


    private TextView tv_result;
    private WeekCalendar weekCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        tv_result = findViewById(R.id.tv_result);


        weekCalendar = findViewById(R.id.weekCalendar);
        weekCalendar.setCheckMode(checkModel);
        weekCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);

                Log.d(TAG, "setOnCalendarChangedListener:::" + year + "年" + month + "月" + "   当前页面选中 " + localDate);
                Log.e(TAG, "baseCalendar::" + baseCalendar);
            }

        });

        weekCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");

                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
                Log.d(TAG, "全部选中：：" + totalCheckedList);
            }

        });

    }

    public void lastMonth(View view) {
        weekCalendar.toLastPager();
    }

    public void nextMonth(View view) {
        weekCalendar.toNextPager();
    }

    public void jump_2018_10_10(View view) {
        weekCalendar.jumpDate("2018-10-10");
    }

    public void jump_2019_10_10(View view) {
        weekCalendar.jumpDate("2019-10-10");
    }

    public void jump_2019_6_10(View view) {
        weekCalendar.jumpDate("2019-6-10");
    }


    public void today(View view) {
        weekCalendar.toToday();
    }
}
