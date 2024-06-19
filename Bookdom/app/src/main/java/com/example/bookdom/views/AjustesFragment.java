package com.example.bookdom.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookdom.R;

public class AjustesFragment extends Fragment {

    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        TextView txtConfiguracion = view.findViewById(R.id.txtConfiguracion);
        TextView txtSobreMi = view.findViewById(R.id.txtSobreMi);

        txtConfiguracion.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PreferenciasActivity.class));
        });

        txtSobreMi.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VideoActivity.class));
        });

        return view;
    }
}
