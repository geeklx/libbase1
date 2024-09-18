package com.geek.libncalendar.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.geek.libncalendar.adapter.BasePagerAdapter;
import com.geek.libncalendar.adapter.MonthPagerAdapter;
import com.geek.libncalendar.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * @author necer
 * @date 2018/9/11
 * qq群：127278900
 */
public class MonthCalendar extends BaseCalendar {

    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected BasePagerAdapter getPagerAdapter(Context context, BaseCalendar baseCalendar) {
        return new MonthPagerAdapter(context, baseCalendar);
    }

    @Override
    protected int getTwoDateCount(LocalDate startDate, LocalDate endDate, int type) {
        return CalendarUtil.getIntervalMonths(startDate, endDate);
    }

    @Override
    protected LocalDate getIntervalDate(LocalDate localDate, int count) {
        return localDate.plusMonths(count);
    }

}
