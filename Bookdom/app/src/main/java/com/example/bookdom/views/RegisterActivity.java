package com.example.bookdom.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookdom.R;
import com.example.bookdom.dto.usuario.LoginResponse;
import com.example.bookdom.dto.usuario.RegisterUsuario;
import com.example.bookdom.enums.Rol;
import com.example.bookdom.models.Usuario;
import com.example.bookdom.retrofit.apis.IAuthApi;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.ErrorManager;
import com.example.bookdom.tools.auth.TokenManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private IAuthApi authApi;
    private RetrofitService retrofitService;
    private EditText editRegisterUsername;
    private EditText editRegisterEmailAddress;
    private EditText editRegisterPassword;
    private EditText editRegisterConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterUsername = findViewById(R.id.editRegisterUsername);
        editRegisterEmailAddress = findViewById(R.id.editRegisterEmailAddress);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);
        editRegisterConfirmPassword = findViewById(R.id.editRegisterConfirmPassword);
        Button btnRegistrar = findViewById(R.id.btnRegister);

        retrofitService = new RetrofitService();
        authApi = retrofitService.getRetrofit().create(IAuthApi.class);

        btnRegistrar.setOnClickListener(v -> {
            if (validarCampos()) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String username = editRegisterUsername.getText().toString().trim();
        String email = editRegisterEmailAddress.getText().toString().trim();
        String password = editRegisterPassword.getText().toString().trim();

        RegisterUsuario registerUsuario = new RegisterUsuario(username, email, password, Rol.USUARIO);

        Call<LoginResponse> call = authApi.register(registerUsuario);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    TokenManager.getInstance().setToken(token);
                    Usuario newUsuario = response.body().getUsuario();
                    newUsuario.setVentas(new ArrayList<>());
                    UsuarioManager.setUsuario(newUsuario);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.showToast(RegisterActivity.this, "El username o el email ya están en uso", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                CustomToast.showToast(RegisterActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT);
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    private boolean validarCampos() {
        String username = editRegisterUsername.getText().toString().trim();
        String email = editRegisterEmailAddress.getText().toString().trim();
        String password = editRegisterPassword.getText().toString().trim();
        String confirmPassword = editRegisterConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editRegisterUsername.setError("El nombre de usuario no puede estar vacío");
            return false;
        }

        if (email.isEmpty()) {
            editRegisterEmailAddress.setError("El correo electrónico no puede estar vacío");
            return false;
        }

        if (password.isEmpty()) {
            editRegisterPassword.setError("La contraseña no puede estar vacía");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            editRegisterConfirmPassword.setError("Debe confirmar la contraseña");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editRegisterPassword.setError("Las contraseñas no coinciden");
            editRegisterConfirmPassword.setError("Las contraseñas no coinciden");
            return false;
        }

        return true;
    }
}
