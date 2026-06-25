package id.dimas.tugasakhirperiodecare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    //=========================
    // TextView
    //=========================

    private TextView tvWelcome;
    private TextView tvMenstruation;
    private TextView tvFertile;
    private TextView tvOvulation;
    private TextView tvHpl;

    //=========================
    // Material Card
    //=========================

    private MaterialCardView cardMenstruation;
    private MaterialCardView cardFertile;
    private MaterialCardView cardOvulation;
    private MaterialCardView cardHpl;

    //=========================
    // EditText
    //=========================

    private EditText etDate;
    private EditText etCycle;

    //=========================
    // Button
    //=========================

    private Button btnCalculate;
    private Button btnLogout;

    //=========================
    // Calendar
    //=========================

    private MaterialCalendarView calendarView;

    //=========================
    // Database
    //=========================

    private DBHelper dbHelper;

    private SharedPreferences sp;

    //=========================
    // Text To Speech
    //=========================

    private TextToSpeech tts;

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("dd-MM-yyyy",
                    Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),

                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars());

                    v.setPadding(

                            systemBars.left,

                            systemBars.top,

                            systemBars.right,

                            systemBars.bottom

                    );

                    return insets;

                });

        //=========================
        // Inisialisasi
        //=========================

        dbHelper = new DBHelper(this);

        sp = getSharedPreferences("pc",
                MODE_PRIVATE);

        tts = new TextToSpeech(this, this);

        initView();

        //=========================
        // Menampilkan Nama User
        //=========================

        String username =
                sp.getString("username", "");

        String nama =
                dbHelper.getNamaUser(username);

        if (nama.isEmpty()) {

            nama = "Pengguna";

        }

        tvWelcome.setText("Halo, " + nama + " 🌸");

        //=========================
        // Load Data Terakhir
        //=========================

        String hpht =
                sp.getString("saved_hpht", "");

        String cycle =
                sp.getString("saved_cycle", "");

        etDate.setText(hpht);

        etCycle.setText(cycle);

        calendarView.addDecorator(
                new TodayDecorator());

        //=========================
        // Date Picker
        //=========================

        etDate.setOnClickListener(v ->
                showDatePicker());

        //=========================
        // Tombol Hitung
        //=========================

        btnCalculate.setOnClickListener(v -> {

            calculateCycle();

            saveInput();

        });

        //=========================
        // Logout
        //=========================

        btnLogout.setOnClickListener(v -> {

            SharedPreferences.Editor editor =
                    sp.edit();

            editor.clear();

            editor.apply();

            Intent intent =
                    new Intent(
                            DashboardActivity.this,
                            LoginActivity.class);

            intent.setFlags(

                    Intent.FLAG_ACTIVITY_NEW_TASK
                            |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK

            );

            startActivity(intent);

            finish();

        });

        //=========================
        // Klik Card + Animasi + Suara
        //=========================

        setupVoiceClickListeners();

    }

    //=========================
    // Inisialisasi View
    //=========================

    private void initView() {

        tvWelcome = findViewById(R.id.tvWelcome);

        tvMenstruation = findViewById(R.id.tvMenstruation);

        tvFertile = findViewById(R.id.tvFertile);

        tvOvulation = findViewById(R.id.tvOvulation);

        tvHpl = findViewById(R.id.tvHpl);

        etDate = findViewById(R.id.etDate);

        etCycle = findViewById(R.id.etCycle);

        btnCalculate = findViewById(R.id.btnCalculate);

        btnLogout = findViewById(R.id.btnLogout);

        calendarView = findViewById(R.id.calendarView);

        cardMenstruation = findViewById(R.id.cardMenstruation);

        cardFertile = findViewById(R.id.cardFertile);

        cardOvulation = findViewById(R.id.cardOvulation);

        cardHpl = findViewById(R.id.cardHpl);

    }
    //====================================================
    // DATE PICKER
    //====================================================

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(

                this,

                (view, year, month, dayOfMonth) -> {

                    Calendar pilih = Calendar.getInstance();

                    pilih.set(year, month, dayOfMonth);

                    etDate.setText(
                            sdf.format(pilih.getTime())
                    );

                },

                calendar.get(Calendar.YEAR),

                calendar.get(Calendar.MONTH),

                calendar.get(Calendar.DAY_OF_MONTH)

        );

        dialog.show();

    }

    //====================================================
    // SIMPAN INPUT
    //====================================================

    private void saveInput() {

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(
                "saved_hpht",
                etDate.getText().toString());

        editor.putString(
                "saved_cycle",
                etCycle.getText().toString());

        editor.apply();

    }

    //====================================================
    // HITUNG SIKLUS
    //====================================================

    private void calculateCycle() {

        try {

            String strDate =
                    etDate.getText().toString().trim();

            String strCycle =
                    etCycle.getText().toString().trim();

            if (strDate.isEmpty()) {

                etDate.setError("Pilih tanggal HPHT");

                return;

            }

            if (strCycle.isEmpty()) {

                etCycle.setError("Masukkan siklus");

                return;

            }

            int cycle = Integer.parseInt(strCycle);

            Calendar hpht = Calendar.getInstance();

            hpht.setTime(
                    sdf.parse(strDate)
            );

            //========================================
            // Bersihkan Kalender
            //========================================

            calendarView.removeDecorators();

            calendarView.addDecorator(
                    new TodayDecorator()
            );

            //========================================
            // HAID SAAT INI
            //========================================

            ArrayList<CalendarDay> periodDays =
                    new ArrayList<>();

            Calendar current =
                    (Calendar) hpht.clone();

            for (int i = 0; i < 5; i++) {

                periodDays.add(
                        CalendarDay.from(current)
                );

                current.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );

            }

            //========================================
            // HAID BERIKUTNYA
            //========================================

            Calendar nextPeriod =
                    (Calendar) hpht.clone();

            nextPeriod.add(
                    Calendar.DAY_OF_YEAR,
                    cycle
            );

            Calendar next =
                    (Calendar) nextPeriod.clone();

            for (int i = 0; i < 5; i++) {

                periodDays.add(
                        CalendarDay.from(next)
                );

                next.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );

            }

            calendarView.addDecorator(

                    new PeriodDecorator(
                            periodDays
                    )

            );

            //========================================
            // OVULASI
            //========================================

            Calendar ovulation =
                    (Calendar) nextPeriod.clone();

            ovulation.add(
                    Calendar.DAY_OF_YEAR,
                    -14
            );

            ArrayList<CalendarDay> ovulationDays =
                    new ArrayList<>();

            ovulationDays.add(
                    CalendarDay.from(ovulation)
            );

            calendarView.addDecorator(

                    new OvulationDecorator(
                            ovulationDays
                    )

            );

            //========================================
            // MASA SUBUR
            //========================================

            ArrayList<CalendarDay> fertileDays =
                    new ArrayList<>();

            Calendar fertile =
                    (Calendar) ovulation.clone();

            fertile.add(
                    Calendar.DAY_OF_YEAR,
                    -3
            );

            for (int i = 0; i < 6; i++) {

                fertileDays.add(
                        CalendarDay.from(fertile)
                );

                fertile.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );

            }

            calendarView.addDecorator(

                    new FertileDecorator(
                            fertileDays
                    )

            );
            //========================================
            // UPDATE HASIL ANALISIS
            //========================================

            tvMenstruation.setText(
                    "🔴 Haid Berikutnya : "
                            + sdf.format(nextPeriod.getTime())
            );

            tvOvulation.setText(
                    "🟣 Hari Ovulasi : "
                            + sdf.format(ovulation.getTime())
            );

            String awalSubur =
                    sdf.format(
                            fertileDays.get(0)
                                    .getCalendar()
                                    .getTime()
                    );

            String akhirSubur =
                    sdf.format(
                            fertileDays.get(5)
                                    .getCalendar()
                                    .getTime()
                    );

            tvFertile.setText(
                    "🟢 Masa Subur : "
                            + awalSubur
                            + " s/d "
                            + akhirSubur
            );

            Calendar hpl =
                    (Calendar) hpht.clone();

            hpl.add(
                    Calendar.DAY_OF_YEAR,
                    280
            );

            tvHpl.setText(
                    "👶 Estimasi HPL : "
                            + sdf.format(hpl.getTime())
            );

            //========================================
            // PINDAH KE BULAN PREDIKSI
            //========================================

            calendarView.setCurrentDate(
                    CalendarDay.from(nextPeriod)
            );

            //========================================
            // SIMPAN KE SQLITE
            //========================================

            dbHelper.insertPeriod(

                    strDate,

                    cycle,

                    5,

                    sdf.format(
                            ovulation.getTime()
                    ),

                    awalSubur,

                    akhirSubur,

                    sdf.format(
                            nextPeriod.getTime()
                    ),

                    sdf.format(
                            hpl.getTime()
                    )

            );

            //========================================
            // SET REMINDER
            //========================================

            setReminder(nextPeriod);

            Toast.makeText(
                    this,
                    "Perhitungan berhasil",
                    Toast.LENGTH_SHORT
            ).show();

        }

        catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Format tanggal salah",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
    //====================================================
    // SET REMINDER
    //====================================================

    private void setReminder(Calendar nextPeriod) {

        Calendar reminder = (Calendar) nextPeriod.clone();

        reminder.add(Calendar.DAY_OF_YEAR, -1);

        reminder.set(Calendar.HOUR_OF_DAY, 8);
        reminder.set(Calendar.MINUTE, 0);
        reminder.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this, ReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(

                this,

                0,

                intent,

                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE

        );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {

            alarmManager.set(

                    AlarmManager.RTC_WAKEUP,

                    reminder.getTimeInMillis(),

                    pendingIntent

            );

        }

    }

    //====================================================
    // ANIMASI CARD
    //====================================================

    private void animateCard(View view) {

        ObjectAnimator scaleX =
                ObjectAnimator.ofFloat(

                        view,

                        "scaleX",

                        1f,

                        0.95f,

                        1f

                );

        ObjectAnimator scaleY =
                ObjectAnimator.ofFloat(

                        view,

                        "scaleY",

                        1f,

                        0.95f,

                        1f

                );

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(scaleX, scaleY);

        animatorSet.setDuration(180);

        animatorSet.start();

    }

    //====================================================
    // CLICK CARD + SUARA
    //====================================================

    private void setupVoiceClickListeners() {

        cardMenstruation.setOnClickListener(v -> {

            animateCard(v);

            speak(tvMenstruation.getText().toString());

        });

        cardFertile.setOnClickListener(v -> {

            animateCard(v);

            speak(tvFertile.getText().toString());

        });

        cardOvulation.setOnClickListener(v -> {

            animateCard(v);

            speak(tvOvulation.getText().toString());

        });

        cardHpl.setOnClickListener(v -> {

            animateCard(v);

            speak(tvHpl.getText().toString());

        });

    }

    //====================================================
    // TEXT TO SPEECH
    //====================================================

    private void speak(String text) {

        if (tts == null)
            return;

        tts.speak(

                text,

                TextToSpeech.QUEUE_FLUSH,

                null,

                "PERIODCARE"

        );

    }

    //====================================================
    // INIT TEXT TO SPEECH
    //====================================================

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(

                    new Locale("id", "ID")

            );

            tts.setSpeechRate(0.9f);

            tts.setPitch(1.0f);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(

                        this,

                        "Bahasa Indonesia tidak didukung",

                        Toast.LENGTH_SHORT

                ).show();

            }

        }

    }

    //====================================================
    // DESTROY
    //====================================================

    @Override
    protected void onDestroy() {

        if (tts != null) {

            tts.stop();

            tts.shutdown();

        }

        super.onDestroy();

    }

}