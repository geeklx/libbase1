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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.calendar.MonthCalendar;
import com.geek.ncalendar.demo.adapter.Generalibean;
import com.geek.ncalendar.demo.adapter.GeneralitemAdapter;
import com.geek.ncalendar.demo.adapter.GeneralitemAdapter1;
import com.geek.ncalendar.entity.CalendarDate;
import com.geek.ncalendar.entity.Lunar;
import com.geek.ncalendar.enumeration.DateChangeBehavior;
import com.geek.ncalendar.listener.OnCalendarChangedListener;
import com.geek.ncalendar.painter.CalendarAdapter;
import com.geek.ncalendar.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneralAdapterActivity extends AppCompatActivity implements View.OnClickListener {


    private MonthCalendar miui10Calendar;
    private RecyclerView recyclerView;
    private TextView tv_data;
    private TextView lastMonth;//上个月
    private TextView tv_today;//当前选择月份
    private TextView nextMonth;//下个月

    List<Generalibean> mList=new ArrayList<>();
    private GeneralitemAdapter1 generalitemAdapter=new GeneralitemAdapter1(mList);


    public List<Generalibean> addList1() {
        Random rand = new Random();
        mList = new ArrayList<>();
        mList.add(new Generalibean("灯塔APP需求确认评审" + rand.nextInt(100) + 1, "12:00-13:00", "我创建", "1"));
        mList.add(new Generalibean("灯塔即时通讯会议" + rand.nextInt(100) + 1, "12:00-13:00", "他人创建", "0"));
        return mList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adapter);

        List<String> pointList = Arrays.asList(
                "2023-04-12", "2023-04-10", "2023-04-18", "2023-04-20", "2023-04-23", "2023-04-30",
                "2023-05-12", "2023-05-10", "2023-05-18", "2023-05-20", "2023-05-23", "2023-05-30",
                "2023-06-01", "2023-06-10", "2023-06-15", "2023-06-20", "2023-06-23", "2023-06-30");

        miui10Calendar = findViewById(R.id.miui10Calendar);
        tv_data = findViewById(R.id.tv_data);
        tv_today = findViewById(R.id.tv_today);
        lastMonth = findViewById(R.id.lastMonth);
        nextMonth = findViewById(R.id.nextMonth);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        generalitemAdapter = new GeneralitemAdapter1(addList1());
        recyclerView.setAdapter(generalitemAdapter);
        lastMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);


        miui10Calendar.setCalendarAdapter(new GeneralAdapter(pointList));
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                Log.e("onCalendarChange", "onCalendarChange:::" + localDate);
                tv_today.setText(localDate.toString("yyyy年MM月"));
                if (localDate != null) {
                    CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                    Lunar lunar = calendarDate.lunar;
                    tv_data.setText("今天" + localDate.toString("MM月dd日") + "  农历" + lunar.lunarMonthStr + lunar.lunarDayStr);
                } else {
                    tv_data.setText("");
                }
                if (pointList.contains(localDate.toString())) {
                    generalitemAdapter.setNewData(addList1());
                } else {
                    if (mList != null && mList.size() > 0){
                        mList.clear();
                        generalitemAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lastMonth) {
            miui10Calendar.toLastPager();
        } else if (id == R.id.nextMonth) {
            miui10Calendar.toNextPager();
        }
    }

    public class GeneralAdapter extends CalendarAdapter {
        List<String> pointList;

        public GeneralAdapter(List<String> pointList) {
            this.pointList = pointList;
        }

        @Override
        public View getCalendarItemView(Context context) {
            return LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
        }

        @Override
        public void onBindToadyView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_lunar = view.findViewById(R.id.tv_lunar);
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            setLunar(view, localDate);
            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                tv_lunar.setTextColor(Color.WHITE);
                tv_lunar.setText("今日");
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.RED);
                tv_lunar.setTextColor(Color.RED);
                tv_lunar.setText("今日");
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }

        }

        @Override
        public void onBindCurrentMonthOrWeekView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {

            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_item = view.findViewById(R.id.tv_item);
            TextView tv_lunar = view.findViewById(R.id.tv_lunar);
            tv_item.setTextColor(Color.BLACK);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
            setLunar(view, localDate);

            if (totalCheckedDateList.contains(localDate)) {
                tv_item.setTextColor(Color.WHITE);
                tv_lunar.setTextColor(Color.WHITE);
                ll_content.setBackgroundResource(R.drawable.bg_checked_ding);
            } else {
                tv_item.setTextColor(Color.BLACK);
                tv_lunar.setTextColor(Color.BLACK);
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }

        }

        @Override
        public void onBindLastOrNextMonthView(View view, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
            View ll_content = view.findViewById(R.id.ll_content);
            TextView tv_item = view.findViewById(R.id.tv_item);
            tv_item.setTextColor(Color.GRAY);
            tv_item.setText(String.valueOf(localDate.getDayOfMonth()));

            setLunar(view, localDate);
            if (totalCheckedDateList.contains(localDate)) {
                ll_content.setBackgroundResource(R.drawable.bg_last_next_checked);
            } else {
                ll_content.setBackgroundResource(R.drawable.bg_unchecked);
            }
        }


        private void setLunar(View view, LocalDate localDate) {
            TextView tv_lunar = view.findViewById(R.id.tv_lunar);
            ImageView iv_dian = view.findViewById(R.id.iv_dian);
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
            if (pointList.contains(localDate.toString())) {
                iv_dian.setVisibility(View.VISIBLE);
            } else {
                iv_dian.setVisibility(View.GONE);
            }

        }
    }

}
