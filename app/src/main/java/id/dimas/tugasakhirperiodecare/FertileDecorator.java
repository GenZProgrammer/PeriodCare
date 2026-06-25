package id.dimas.tugasakhirperiodecare;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class    FertileDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;

    public FertileDecorator(Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Warna pink muda lembut untuk masa subur
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FCE4EC")));
    }
}