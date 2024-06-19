package com.example.bookdom.tools.libros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookdom.R;
import com.example.bookdom.models.Libro;

import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibroViewHolder> {
    private List<Libro> libros;
    private LibroListener listener;

    public LibrosAdapter(List<Libro> libros, LibroListener listener) {
        this.libros = libros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new LibroViewHolder(layoutInflater.inflate(R.layout.libro_personalizado, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.render(libro);
        holder.getCardView().setOnClickListener(v -> listener.onItemClicked(libro));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void removeLibro(Libro libro) {
        int position = libros.indexOf(libro);
        if (position != -1) {
            libros.remove(position);
            notifyItemRemoved(position);
        }
    }
}


