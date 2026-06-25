package id.dimas.tugasakhirperiodecare;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class TodayDecorator implements DayViewDecorator {

    private final CalendarDay today;

    public TodayDecorator() {
        today = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(today);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Membuat lingkaran abu-abu terang sebagai latar belakang hari ini
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.parseColor("#F5F5F5")); // Abu-abu sangat muda
        drawable.setStroke(4, Color.parseColor("#BDBDBD")); // Garis tepi abu-abu

        view.setBackgroundDrawable(drawable);
        
        // Membuat teks hari ini menjadi Tebal (Bold)
        view.addSpan(new StyleSpan(Typeface.BOLD));
    }
}