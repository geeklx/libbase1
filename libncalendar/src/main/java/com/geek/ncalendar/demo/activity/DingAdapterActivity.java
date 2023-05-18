package com.geek.ncalendar.demo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.calendar.ICalendar;
import com.geek.ncalendar.entity.CalendarDate;
import com.geek.ncalendar.enumeration.DateChangeBehavior;
import com.geek.ncalendar.listener.OnCalendarChangedListener;
import com.geek.ncalendar.painter.CalendarAdapter;
import com.geek.ncalendar.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;

public class DingAdapterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adapter);
        List<String> pointList = Arrays.asList("2023-05-01", "2023-05-19", "2023-05-20", "2023-05-23", "2023-05-02", "2023-05-03");


        ICalendar miui10Calendar = findViewById(R.id.miui10Calendar);
//        InnerPainter innerPainter = (InnerPainter) miui10Calendar.getCalendarPainter();
//        innerPainter.setPointList(pointList);
        miui10Calendar.setCalendarAdapter(new DingAdapter(pointList));
        // miui10Calendar.setSelectedMode(SelectedModel.MULTIPLE);
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                Log.e("onCalendarChange", "onCalendarChange:::" + localDate);
            }

        });

    }


    public static class DingAdapter extends CalendarAdapter {
        List<String> pointList;

        public DingAdapter(List<String> pointList) {
            this.pointList = pointList;
        }

        @Override
        public View getCalendarItemView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
        }

        /**
         * 当天
         */
        @Override
        public void onBindToadyView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {

            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_item = view.findViewById(R.id.tv_item);
            ImageView iv_dian = view.findViewById(R.id.iv_dian);
            tv_item.setTextColor(Color.RED);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            setLunar(view, localDate, totalCheckedDateList);
            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.BLACK);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }
        }

        /**
         * 不是当天的当月其他日期
         */
        @Override
        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {

            View ll_content = view.findViewById(R.id.ll_content);

            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setTextColor(Color.BLACK);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));

            setLunar(view, localDate, totalCheckedDateList);

            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.BLACK);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }

        }

        /**
         * 不是当月的日期
         */
        @Override
        public void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            setLunar(view, localDate, totalCheckedDateList);
            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding_last_next);
            } else {
                tv_item.setTextColor(Color.GRAY);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }
        }


        private void setLunar(View view, LocalDate localDate, List<LocalDate> selectedDateList) {

            TextView tv_lunar = view.findViewById(R.id.tv_lunar);
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
            ImageView iv_dian = view.findViewById(R.id.iv_dian);
            if (pointList.contains(localDate.toString())) {
                iv_dian.setVisibility(View.VISIBLE);
            } else {
                iv_dian.setVisibility(View.GONE);
            }
            if (selectedDateList.contains(localDate)) {
                tv_lunar.setTextColor(Color.WHITE);
            } else {
                tv_lunar.setTextColor(Color.GRAY);
            }
        }
    }

}
