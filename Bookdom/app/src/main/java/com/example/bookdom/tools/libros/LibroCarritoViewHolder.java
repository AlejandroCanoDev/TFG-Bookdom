package com.example.bookdom.tools.libros;

import static com.example.bookdom.views.CarritoFragment.imgBasura;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookdom.R;
import com.example.bookdom.models.Libro;

public class LibroCarritoViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTituloLibro;
    private TextView txtPrecioLibro;
    private ImageView imgLibro;
    private CardView cardView;
    private RadioButton radioButton;
    private Vibrator vibrator;
    private Libro libro;

    public LibroCarritoViewHolder(@NonNull View itemView, LibroCarritoAdapter adapter, LibroListener listener) {
        super(itemView);
        txtTituloLibro = itemView.findViewById(R.id.txtTituloLibroCarrito);
        txtPrecioLibro = itemView.findViewById(R.id.txtPrecioLibroCarrito);
        imgLibro = itemView.findViewById(R.id.imgLibroCarrito);
        cardView = itemView.findViewById(R.id.cardContainerCarrito);
        radioButton = itemView.findViewById(R.id.radioButtonEliminar);

        vibrator = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        cardView.setOnLongClickListener(v -> {
            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }
            imgBasura.setVisibility(View.VISIBLE);
            adapter.setSelectionMode(true);
            return true;
        });

        cardView.setOnClickListener(v -> {
            if (adapter.isSelectionMode()) {
                adapter.toggleSelection(getAdapterPosition());
            } else if (listener != null && libro != null) {
                listener.onItemClicked(libro);
            }
        });
    }

    public void render(Libro libro, boolean isSelectionMode, boolean isSelected) {
        this.libro = libro; // Almacena el libro actual

        if (!libro.getImagenesLibro().isEmpty()) {
            String imageUrl = libro.getImagenesLibro().get(0).getUrl();
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(imgLibro);
        }
        txtTituloLibro.setText(libro.getTitulo());
        txtPrecioLibro.setText(String.valueOf(libro.getPrecio()) + "€");

        radioButton.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        radioButton.setChecked(isSelected);
    }
}
