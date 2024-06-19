package com.example.bookdom.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.bookdom.R;
import com.example.bookdom.tools.auth.TokenManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "bookdom_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenManager.getInstance().loadTokenFromPreferences(this);
        Log.d("MainActivity", "Token: " + TokenManager.getInstance().getToken());

        if (TokenManager.getInstance().getToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        manejarFragments();

        ocultarMenuInferior();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bookdom Channel";
            String description = "Channel for Bookdom notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void manejarFragments() {
        TabLayout tabLayout = findViewById(R.id.tabLayoutLibros);
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new CarritoFragment());
        fragmentList.add(new AddLibroFragment());
        fragmentList.add(new ListFragment());
        fragmentList.add(new ListFragment());
        fragmentList.add(new UsuarioFragment());

        TabLayout.Tab tab = tabLayout.getTabAt(2);
        if (tab != null) {
            tab.select();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutUsuario, new ListFragment())
                .commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = fragmentList.get(tab.getPosition());

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutUsuario, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void ocultarMenuInferior() {
        // Si el teclado está desplegado oculta el menú inferior
        View tabLayoutLibros = findViewById(R.id.tabLayoutLibros);

        View activityRootView = findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                boolean isKeyboardShowing = keypadHeight > screenHeight * 0.15;

                if (isKeyboardShowing) {
                    tabLayoutLibros.setVisibility(View.GONE);
                } else {
                    tabLayoutLibros.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
