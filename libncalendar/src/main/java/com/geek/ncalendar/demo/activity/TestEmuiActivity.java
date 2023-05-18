package com.geek.ncalendar.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.calendar.EmuiCalendar;
import com.geek.ncalendar.enumeration.CalendarState;
import com.geek.ncalendar.enumeration.DateChangeBehavior;
import com.geek.ncalendar.listener.OnCalendarChangedListener;
import com.geek.ncalendar.listener.OnCalendarMultipleChangedListener;

import org.joda.time.LocalDate;

import java.util.List;


/**
 * Created by necer on 2018/11/12.
 */
public class TestEmuiActivity extends BaseActivity {

    private EmuiCalendar emuiCalendar;

    private TextView tv_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emui);

        emuiCalendar = findViewById(R.id.emuiCalendar);

        tv_result = findViewById(R.id.tv_result);

        emuiCalendar.setCheckMode(checkModel);
        emuiCalendar.setDefaultCheckedFirstDate(true);//只在selectedMode==SINGLE_SELECTED有效

        emuiCalendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + "   当前页面选中 " + localDate);
                Log.d(TAG, "当前页面选中：：" + localDate);
                Log.e(TAG, "baseCalendar::" + baseCalendar);
            }

        });
        emuiCalendar.setOnCalendarMultipleChangedListener(new OnCalendarMultipleChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, List<LocalDate> currPagerCheckedList, List<LocalDate> totalCheckedList, DateChangeBehavior dateChangeBehavior) {
                tv_result.setText(year + "年" + month + "月" + " 当前页面选中 " + currPagerCheckedList.size() + "个  总共选中" + totalCheckedList.size() + "个");
                Log.d(TAG, year + "年" + month + "月");
                Log.d(TAG, "当前页面选中：：" + currPagerCheckedList);
                Log.d(TAG, "全部选中：：" + totalCheckedList);
            }

        });


    }

    public void lastPager(View view) {
        emuiCalendar.toLastPager();
    }

    public void nextPager(View view) {
        emuiCalendar.toNextPager();
    }

    public void today(View view) {
        emuiCalendar.toToday();
    }


    public void fold(View view) {
        if (emuiCalendar.getCalendarState() == CalendarState.WEEK) {
            emuiCalendar.toMonth();
        } else {
            emuiCalendar.toWeek();
        }
    }
}
