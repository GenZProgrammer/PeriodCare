package id.dimas.tugasakhirperiodecare;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class PeriodDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;

    public PeriodDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.parseColor("#F44336"));

        view.setBackgroundDrawable(drawable);
        view.addSpan(new android.text.style.ForegroundColorSpan(Color.WHITE));

    }
}