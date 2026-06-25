package id.dimas.tugasakhirperiodecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "periodcare.db";
    private static final int DATABASE_VERSION = 1;

    // Nama tabel
    public static final String TABLE_USER = "user";
    public static final String TABLE_PERIOD = "period";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_USER + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nama TEXT," +
                        "username TEXT UNIQUE," +
                        "password TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_PERIOD + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "hpht TEXT," +
                        "siklus INTEGER," +
                        "lama_haid INTEGER," +
                        "ovulasi TEXT," +
                        "masa_subur_awal TEXT," +
                        "masa_subur_akhir TEXT," +
                        "haid_berikutnya TEXT," +
                        "hpl TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERIOD);

        onCreate(db);

    }

    //====================================================
    // REGISTER
    //====================================================

    public boolean insertUser(String nama,
                              String username,
                              String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("nama", nama);
        cv.put("username", username);
        cv.put("password", password);

        long result = db.insert(TABLE_USER, null, cv);

        return result != -1;
    }

    //====================================================
    // LOGIN
    //====================================================

    public boolean checkLogin(String username,
                              String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE username=? AND password=?",
                new String[]{
                        username,
                        password
                });

        boolean login = cursor.moveToFirst();

        cursor.close();

        return login;

    }

    //====================================================
    // CEK USERNAME
    //====================================================

    public boolean checkUsername(String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE username=?",
                new String[]{
                        username
                });

        boolean ada = cursor.moveToFirst();

        cursor.close();

        return ada;

    }

    //====================================================
    // AMBIL SEMUA USER
    //====================================================

    public Cursor getAllUsers() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " ORDER BY nama ASC",
                null
        );

    }

    //====================================================
    // AMBIL USER BERDASARKAN USERNAME
    //====================================================

    public Cursor getUser(String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_USER +
                        " WHERE username=?",
                new String[]{
                        username
                });

    }

    //====================================================
    // AMBIL NAMA USER
    //====================================================

    public String getNamaUser(String username) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nama FROM " + TABLE_USER +
                        " WHERE username=?",
                new String[]{
                        username
                });

        String nama = "";

        if (cursor.moveToFirst()) {

            nama = cursor.getString(
                    cursor.getColumnIndexOrThrow("nama"));

        }

        cursor.close();

        return nama;

    }

    //====================================================
    // UPDATE PASSWORD
    //====================================================

    public boolean updatePassword(String username,
                                  String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("password", password);

        int result = db.update(
                TABLE_USER,
                cv,
                "username=?",
                new String[]{
                        username
                });

        return result > 0;

    }

    //====================================================
    // SIMPAN DATA PERIOD
    //====================================================

    public boolean insertPeriod(
            String hpht,
            int siklus,
            int lamaHaid,
            String ovulasi,
            String awalSubur,
            String akhirSubur,
            String haidBerikutnya,
            String hpl) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("hpht", hpht);
        cv.put("siklus", siklus);
        cv.put("lama_haid", lamaHaid);
        cv.put("ovulasi", ovulasi);
        cv.put("masa_subur_awal", awalSubur);
        cv.put("masa_subur_akhir", akhirSubur);
        cv.put("haid_berikutnya", haidBerikutnya);
        cv.put("hpl", hpl);

        long result = db.insert(TABLE_PERIOD, null, cv);

        return result != -1;

    }

    //====================================================
    // AMBIL DATA PERIOD TERAKHIR
    //====================================================

    public Cursor getLastPeriod() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_PERIOD +
                        " ORDER BY id DESC LIMIT 1",
                null);

    }

    //====================================================
    // AMBIL SEMUA PERIOD
    //====================================================

    public Cursor getAllPeriod() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_PERIOD +
                        " ORDER BY id DESC",
                null);

    }

    //====================================================
    // UPDATE PERIOD
    //====================================================

    public boolean updatePeriod(
            int id,
            String hpht,
            int siklus,
            int lamaHaid,
            String ovulasi,
            String awalSubur,
            String akhirSubur,
            String haidBerikutnya,
            String hpl) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("hpht", hpht);
        cv.put("siklus", siklus);
        cv.put("lama_haid", lamaHaid);
        cv.put("ovulasi", ovulasi);
        cv.put("masa_subur_awal", awalSubur);
        cv.put("masa_subur_akhir", akhirSubur);
        cv.put("haid_berikutnya", haidBerikutnya);
        cv.put("hpl", hpl);

        int result = db.update(
                TABLE_PERIOD,
                cv,
                "id=?",
                new String[]{
                        String.valueOf(id)
                });

        return result > 0;

    }

    //====================================================
    // HAPUS PERIOD
    //====================================================

    public boolean deletePeriod(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_PERIOD,
                "id=?",
                new String[]{
                        String.valueOf(id)
                });

        return result > 0;

    }

}