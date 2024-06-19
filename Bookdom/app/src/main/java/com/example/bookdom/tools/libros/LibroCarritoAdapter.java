package com.example.bookdom.tools.libros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookdom.R;
import com.example.bookdom.models.Libro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LibroCarritoAdapter extends RecyclerView.Adapter<LibroCarritoViewHolder> {
    private List<Libro> libros;
    private LibroListener listener;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedItems = new HashSet<>();

    public LibroCarritoAdapter(List<Libro> libros, LibroListener listener) {
        this.libros = libros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroCarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_carrito_personalizado, parent, false);
        return new LibroCarritoViewHolder(view, this, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroCarritoViewHolder holder, int position) {
        holder.render(libros.get(position), isSelectionMode, selectedItems.contains(position));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }
}