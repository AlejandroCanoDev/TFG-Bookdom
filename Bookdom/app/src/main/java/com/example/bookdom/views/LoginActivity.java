package com.example.bookdom.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookdom.R;
import com.example.bookdom.dto.usuario.LoginResponse;
import com.example.bookdom.dto.usuario.LoginUsuario;
import com.example.bookdom.models.Usuario;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.IAuthApi;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.auth.TokenManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername, editPassword;
    private TextView textRegister;
    private Button btnLogin;
    private IAuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textRegister = findViewById(R.id.textRegister);

        authApi = RetrofitService.getRetrofit().create(IAuthApi.class);

        btnLogin.setOnClickListener(v -> login());

        textRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editUsername.setError("El nombre de usuario no puede estar vacío");
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("La contraseña no puede estar vacía");
            return;
        }

        LoginUsuario loginUsuario = new LoginUsuario(username, password);

        Call<LoginResponse> call = authApi.login(loginUsuario);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    TokenManager.getInstance().setToken(token);
                    TokenManager.getInstance().saveTokenToPreferences(LoginActivity.this, token);
                    Usuario usuario = response.body().getUsuario();
                    UsuarioManager.setUsuario(usuario);

                    // Reinicializar Retrofit con el nuevo token
                    RetrofitService.reiniciarRetrofit();

                    // Redirigir a la actividad principal
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.showToast(LoginActivity.this, "Nombre de usuario o contraseña incorrecta", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                CustomToast.showToast(LoginActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
            }
        });
    }
}
