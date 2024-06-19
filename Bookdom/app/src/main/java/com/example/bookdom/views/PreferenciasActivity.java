package com.example.bookdom.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookdom.R;
import com.example.bookdom.tools.auth.TokenManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import java.util.Locale;

public class PreferenciasActivity extends AppCompatActivity {

    private Switch switchMantenerSesion;
    private Switch switchTema;
    private Spinner spinnerIdioma;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_preferencias);

        switchMantenerSesion = findViewById(R.id.switch_mantener_sesion);
        switchTema = findViewById(R.id.switch_tema);
        spinnerIdioma = findViewById(R.id.spinner_idioma);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);

        cargarPreferencias();

        switchMantenerSesion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_logged_in", isChecked);
            if (!isChecked) {
                TokenManager.getInstance().clearToken();
                editor.remove("auth_token");
                editor.remove("user_id");
            } else {
                editor.putString("auth_token", TokenManager.getInstance().getToken());
                editor.putLong("user_id", UsuarioManager.getUsuario().getId());
            }
            editor.apply();
        });

        switchTema.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String currentTheme = sharedPreferences.getString("theme", "light");
            String newTheme = isChecked ? "dark" : "light";
            if (!currentTheme.equals(newTheme)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("theme", newTheme);
                editor.apply();
                recreate();
            }
        });

        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = parentView.getItemAtPosition(position).toString();
                String currentLanguage = sharedPreferences.getString("language", "en");
                if (!selectedLanguage.equals(currentLanguage)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", selectedLanguage);
                    editor.apply();
                    setLanguage(selectedLanguage);
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void cargarPreferencias() {
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);
        switchMantenerSesion.setChecked(isLoggedIn);

        String theme = sharedPreferences.getString("theme", "light");
        switchTema.setChecked(theme.equals("dark"));

        String language = sharedPreferences.getString("language", "en");
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerIdioma.getAdapter();
        int position = adapter.getPosition(language);
        spinnerIdioma.setSelection(position);
    }

    private void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languageCode);
        editor.apply();

        recreate();
    }
}