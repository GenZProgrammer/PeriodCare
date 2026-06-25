package id.dimas.tugasakhirperiodecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private DBHelper dbHelper;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SharedPreferences
        sp = getSharedPreferences("pc", MODE_PRIVATE);

        // Jika sudah login
        if (sp.getBoolean("isLogin", false)) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
            return;
        }

        // Inisialisasi Database
        dbHelper = new DBHelper(this);

        // Inisialisasi View
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Tombol Login
        btnLogin.setOnClickListener(v -> loginUser());

        // Buka halaman Register
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    private void loginUser() {

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username tidak boleh kosong");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password tidak boleh kosong");
            return;
        }

        if (dbHelper.checkLogin(username, password)) {

            // Simpan sesi login
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLogin", true);
            editor.putString("username", username);
            editor.putString("name", dbHelper.getNamaUser(username));
            editor.apply();

            Toast.makeText(this,
                    "Login Berhasil",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this,
                    DashboardActivity.class));

            finish();

        } else {

            Toast.makeText(this,
                    "Username atau Password salah",
                    Toast.LENGTH_SHORT).show();

        }

    }

}