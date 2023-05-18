package com.geek.ncalendar.adapter;

import android.content.Context;

import com.geek.ncalendar.calendar.BaseCalendar;
import com.geek.ncalendar.enumeration.CalendarType;

import org.joda.time.LocalDate;

/**
 * @author necer
 * @date 2018/9/11
 * qq群：127278900
 */
public class WeekPagerAdapter extends BasePagerAdapter {


    public WeekPagerAdapter(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
    }

    @Override
    protected LocalDate getPageInitializeDate(int position) {
        return getInitializeDate().plusDays((position - getPageCurrIndex()) * 7);
    }

    @Override
    protected CalendarType getCalendarType() {
        return CalendarType.WEEK;
    }

}
