package com.example.bookdom.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bookdom.R;
import com.example.bookdom.databinding.FragmentListBinding;
import com.example.bookdom.enums.CondicionLibro;
import com.example.bookdom.enums.Genero;
import com.example.bookdom.models.Libro;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.ILibroApi;
import com.example.bookdom.tools.ErrorManager;
import com.example.bookdom.tools.libros.LibroListener;
import com.example.bookdom.tools.libros.LibrosAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment implements LibroListener {
    private ProgressBar progressBar;
    private FragmentListBinding binding;
    private ILibroApi libroApi;
    private List<Libro> libros;
    private Spinner filterSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarList);
        filterSpinner = view.findViewById(R.id.filterSpinner);
        inicializarRetrofit();
        inicializarLibros();
        manejarBarraBusqueda();
        configurarSpinner();
    }

    private void inicializarRecyclerView() {
        if (isAdded()) {
            binding.recyclerViewLibros.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            binding.recyclerViewLibros.setAdapter(new LibrosAdapter(libros, this));
            progressBar.setVisibility(View.GONE);
        }
    }

    private void manejarBarraBusqueda() {
        SearchView searchBar = binding.getRoot().findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    buscarLibro(newText);
                } else {
                    inicializarLibros();
                }
                return false;
            }
        });
    }

    private void buscarLibro(String text) {
        progressBar.setVisibility(View.VISIBLE);
        libroApi.findByTituloOrAutor(text)
                .enqueue(new Callback<List<Libro>>() {
                    @Override
                    public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                        libros = response.body();
                        inicializarRecyclerView();
                    }

                    @Override
                    public void onFailure(Call<List<Libro>> call, Throwable t) {
                        ErrorManager.mensajeDeError("Error al buscar libros", t);
                    }
                });
    }

    private void inicializarLibros() {
        progressBar.setVisibility(View.VISIBLE);
        libroApi.findAll()
                .enqueue(new Callback<List<Libro>>() {
                    @Override
                    public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                        libros = response.body();
                        inicializarRecyclerView();
                    }

                    @Override
                    public void onFailure(Call<List<Libro>> call, Throwable t) {
                        ErrorManager.mensajeDeError("Error al cargar los libros", t);
                    }
                });
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        libroApi = retrofitService.getRetrofit().create(ILibroApi.class);
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.filter_options, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedFilter = (String) parentView.getItemAtPosition(position);
                applyFilter(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });
    }

    private void applyFilter(String filter) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Libro>> call;
        switch (filter) {
            case "Precio Descendente":
                call = libroApi.listarPorPrecioDescendente();
                break;
            case "Precio Ascendente":
                call = libroApi.listarPorPrecioAscendente();
                break;
            case "Narrativa":
                call = libroApi.findByGenero(Genero.Narrativa);
                break;
            case "Poesía":
                call = libroApi.findByGenero(Genero.Poesía);
                break;
            case "Teatro":
                call = libroApi.findByGenero(Genero.Teatro);
                break;
            case "Ensayo":
                call = libroApi.findByGenero(Genero.Ensayo);
                break;
            case "Ciencia ficción":
                call = libroApi.findByGenero(Genero.Ciencia_ficción);
                break;
            case "Fantasía":
                call = libroApi.findByGenero(Genero.Fantasía);
                break;
            case "Terror":
                call = libroApi.findByGenero(Genero.Terror);
                break;
            case "Romance":
                call = libroApi.findByGenero(Genero.Romance);
                break;
            case "Drama":
                call = libroApi.findByGenero(Genero.Drama);
                break;
            case "Aventura":
                call = libroApi.findByGenero(Genero.Aventura);
                break;
            case "Crimen":
                call = libroApi.findByGenero(Genero.Crimen);
                break;
            case "Misterio":
                call = libroApi.findByGenero(Genero.Misterio);
                break;
            case "Histórica":
                call = libroApi.findByGenero(Genero.Histórica);
                break;
            case "Biografía":
                call = libroApi.findByGenero(Genero.Biografía);
                break;
            case "Autobiografía":
                call = libroApi.findByGenero(Genero.Autobiografía);
                break;
            case "Viajes":
                call = libroApi.findByGenero(Genero.Viajes);
                break;
            case "Fantasía épica":
                call = libroApi.findByGenero(Genero.Fantasía_épica);
                break;
            case "Fantasía urbana":
                call = libroApi.findByGenero(Genero.Fantasía_urbana);
                break;
            case "Realismo mágico":
                call = libroApi.findByGenero(Genero.Realismo_mágico);
                break;
            case "Humor":
                call = libroApi.findByGenero(Genero.Humor);
                break;
            case "Surrealismo":
                call = libroApi.findByGenero(Genero.Surrealismo);
                break;
            case "Crónica":
                call = libroApi.findByGenero(Genero.Crónica);
                break;
            case "Erótica":
                call = libroApi.findByGenero(Genero.Erótica);
                break;
            case "Fábula":
                call = libroApi.findByGenero(Genero.Fábula);
                break;
            case "Cuento":
                call = libroApi.findByGenero(Genero.Cuento);
                break;
            case "Novela gráfica":
                call = libroApi.findByGenero(Genero.Novela_gráfica);
                break;
            case "Literatura infantil":
                call = libroApi.findByGenero(Genero.Literatura_infantil);
                break;
            case "Literatura juvenil":
                call = libroApi.findByGenero(Genero.Literatura_juvenil);
                break;
            default:
                call = libroApi.findAll();
                break;
        }

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                libros = response.body();
                inicializarRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                ErrorManager.mensajeDeError("Error al aplicar el filtro", t);
            }
        });
    }

    @Override
    public void onItemClicked(Libro libro) {
        Intent intent = new Intent(requireActivity(), LibroActivity.class);
        intent.putExtra("libroId", libro.getId());
        startActivity(intent);
    }
}
