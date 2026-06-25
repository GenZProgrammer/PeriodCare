package id.dimas.tugasakhirperiodecare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etKonfirmasi;

    private Button btnRegister;

    private TextView tvLogin;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(v -> register());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

    }

    private void initView() {

        etNama = findViewById(R.id.etNama);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etKonfirmasi = findViewById(R.id.etKonfirmasi);

        btnRegister = findViewById(R.id.btnRegister);

        tvLogin = findViewById(R.id.tvLogin);

    }

    private void register() {

        String nama = etNama.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String konfirmasi = etKonfirmasi.getText().toString().trim();

        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(konfirmasi)) {
            etKonfirmasi.setError("Konfirmasi password");
            return;
        }

        if (!password.equals(konfirmasi)) {
            Toast.makeText(this,
                    "Konfirmasi password tidak sama",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUsername(username)) {

            Toast.makeText(this,
                    "Username sudah digunakan",
                    Toast.LENGTH_SHORT).show();

            return;

        }

        boolean berhasil = dbHelper.insertUser(
                nama,
                username,
                password
        );

        if (berhasil) {

            Toast.makeText(this,
                    "Registrasi berhasil",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(RegisterActivity.this,
                    LoginActivity.class));

            finish();

        } else {

            Toast.makeText(this,
                    "Registrasi gagal",
                    Toast.LENGTH_SHORT).show();

        }

    }

}