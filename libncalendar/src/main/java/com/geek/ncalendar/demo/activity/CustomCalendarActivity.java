package com.geek.ncalendar.demo.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geek.ncalendar.R;
import com.geek.ncalendar.calendar.Miui10Calendar;
import com.geek.ncalendar.demo.painter.LigaturePainter;
import com.geek.ncalendar.demo.painter.TicketPainter;
import com.geek.ncalendar.enumeration.CheckModel;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by necer on 2019/1/4.
 */
public class CustomCalendarActivity extends AppCompatActivity {

    Miui10Calendar miui10Calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        miui10Calendar = findViewById(R.id.miui10Calendar);
        miui10Calendar.setCheckMode(CheckModel.MULTIPLE);
        LigaturePainter painter = new LigaturePainter(this);
        miui10Calendar.setCalendarPainter(painter);

    }

    public void ligaturePainter(View view) {
        LigaturePainter painter = new LigaturePainter(this);
        miui10Calendar.setCalendarPainter(painter);
    }

    public void ticketPainter(View view) {
        TicketPainter ticketPainter = new TicketPainter(this, miui10Calendar);

        Map<LocalDate, String> priceMap = new HashMap<>();
        priceMap.put(new LocalDate("2023-05-07"), "￥350");
        priceMap.put(new LocalDate("2023-05-07"), "￥350");
        priceMap.put(new LocalDate("2023-05-30"), "￥350");
        priceMap.put(new LocalDate("2023-05-03"), "￥350");
        priceMap.put(new LocalDate("2023-05-04"), "￥350");
        priceMap.put(new LocalDate("2023-05-10"), "￥350");
        priceMap.put(new LocalDate("2023-05-15"), "￥350");
        priceMap.put(new LocalDate("2023-05-30"), "￥350");
        priceMap.put(new LocalDate("2023-05-04"), "￥350");
        priceMap.put(new LocalDate("2023-05-29"), "￥350");

        ticketPainter.setPriceMap(priceMap);

        miui10Calendar.setCalendarPainter(ticketPainter);
    }
}
