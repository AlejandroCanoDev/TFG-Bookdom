package com.example.bookdom.views;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.bookdom.R;
import com.example.bookdom.dto.usuario.UsuarioLibroDTO;
import com.example.bookdom.enums.CondicionLibro;
import com.example.bookdom.enums.Genero;
import com.example.bookdom.models.ImgLibro;
import com.example.bookdom.models.Libro;
import com.example.bookdom.retrofit.apis.ILibroApi;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.NotificationManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLibroFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    private List<Bitmap> imagenesBitmap;
    private List<Uri> uriList;
    private List<ImgLibro> imagenesLibro;
    private ILibroApi libroApi;
    private ImageView[] imageViews;
    private EditText editTitulo;
    private EditText editAutor;
    private EditText editPrecio;
    private EditText editDescripcion;
    private Spinner spinnerGenero;
    private Spinner spinnerEstado;
    private Button btnSubir;
    private TableLayout imgTableLayout;
    private ProgressBar progressBar;
    private String titulo;
    private String autor;
    private String precio;
    private String descripcion;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_libro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarRetrofit();
        inicializarComponentes(view);

        if (!checkPermissions()) {
            requestPermissions();
        }

        imgTableLayout.setOnClickListener(v -> {
            if (imagenesBitmap.size() < 8)
                showImagePickerDialog();
            else
                CustomToast.showToast(getContext(), "Solo puedes seleccionar 8 imágenes", Toast.LENGTH_SHORT);
        });

        btnSubir.setOnClickListener(v -> {
            if (validarCampos()) {
                progressBar.setVisibility(View.VISIBLE);
                subirImagenesFirebase();
            }
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, REQUEST_PERMISSIONS);
    }

    private boolean validarCampos() {
        boolean libroValido = true;

        titulo = editTitulo.getText().toString();
        autor = editAutor.getText().toString();
        precio = editPrecio.getText().toString();
        descripcion = editDescripcion.getText().toString();

        if (titulo.isEmpty()) {
            editTitulo.setError("El título no puede estar vacío");
            libroValido = false;
        }

        if (autor.isEmpty()) {
            editAutor.setError("El autor no puede estar vacío");
            libroValido = false;
        }

        if (precio.isEmpty()) {
            editPrecio.setError("El precio no puede estar vacío");
            libroValido = false;
        }

        if (descripcion.isEmpty()) {
            editDescripcion.setError("La descripción no puede estar vacía");
            libroValido = false;
        }

        if (imagenesBitmap.size() == 0) {
            CustomToast.showToast(getContext(), "Selecciona al menos una imagen", Toast.LENGTH_SHORT);
            libroValido = false;
        }

        return libroValido;
    }

    private void subirLibro() {
        Libro libro = new Libro();
        titulo = editTitulo.getText().toString();
        autor = editAutor.getText().toString();
        precio = editPrecio.getText().toString();
        descripcion = editDescripcion.getText().toString();

        btnSubir.setClickable(false);

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setPrecio(Double.parseDouble(precio));
        libro.setDescripcion(descripcion);
        libro.setCondicion(CondicionLibro.valueOf(spinnerEstado.getSelectedItem().toString()));
        libro.setGenero(Genero.valueOf(spinnerGenero.getSelectedItem().toString()));

        libro.setImagenesLibro(imagenesLibro);

        libro.setVendedor(new UsuarioLibroDTO(UsuarioManager.getUsuario()));

        libroApi.addLibros(libro).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                progressBar.setVisibility(View.GONE);
                CustomToast.showToast(getContext(), "Se ha guardado el libro", Toast.LENGTH_SHORT);
                reestablecerCampos();

                NotificationManager.mostrarNotificacion(libro.getTitulo(), libro.getImagenesLibro().get(0).getUrl(), getContext(), getActivity());

            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                CustomToast.showToast(getContext(), "Error al guardar el libro", Toast.LENGTH_SHORT);
            }
        });
    }

    private void subirImagenesFirebase() {
        int totalImagenes = uriList.size();
        AtomicInteger imagenesSubidas = new AtomicInteger();

        for (Uri uri : uriList) {
            String imageName = UUID.randomUUID().toString();
            StorageReference imageRef = storageRef.child("imagenesLibros/" + imageName);

            imageRef.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri2 -> {
                                    String imageUrl = uri2.toString();
                                    imagenesLibro.add(new ImgLibro(imageUrl));

                                    imagenesSubidas.getAndIncrement();

                                    if (imagenesSubidas.get() == totalImagenes) {
                                        subirLibro();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    CustomToast.showToast(getContext(), "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT);
                                });
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        CustomToast.showToast(getContext(), "Error al subir una imagen", Toast.LENGTH_SHORT);
                    });
        }
    }

    private void reestablecerCampos() {
        editTitulo.setText("");
        editDescripcion.setText("");
        editAutor.setText("");
        editPrecio.setText("");
        spinnerGenero.setSelection(0);
        spinnerEstado.setSelection(0);
        uriList = new ArrayList<>();
        imagenesBitmap = new ArrayList<>();
        imagenesLibro = new ArrayList<>();

        for (ImageView imageView : imageViews) {
            imageView.setImageResource(R.drawable.camara);
        }
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        libroApi = retrofitService.getRetrofit().create(ILibroApi.class);
    }

    private void showImagePickerDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getContext(), "com.example.bookdom.fileprovider", photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
        }

        Intent chooserIntent = Intent.createChooser(pickIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE_FROM_GALLERY_REQUEST);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void mostrarImagenes() {
        for (int i = 0; i < imagenesBitmap.size(); i++) {
            if (i < imageViews.length) {
                imageViews[i].setImageBitmap(imagenesBitmap.get(i));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST) {
                if (data != null && data.getData() != null) {
                    // Selecciona una sola imagen
                    Uri selectedImageUri = data.getData();
                    handleImageUri(selectedImageUri);
                } else if (data != null && data.getClipData() != null) {
                    // Selecciona varias imágenes
                    int count = data.getClipData().getItemCount();
                    if (count <= 8) {
                        for (int i = 0; i < count; i++) {
                            Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
                            handleImageUri(selectedImageUri);
                        }
                    } else {
                        CustomToast.showToast(getContext(), "Selecciona solo hasta 8 imagenes", Toast.LENGTH_SHORT);
                    }
                } else {
                    // Imagen capturada con la cámara
                    handleImageUri(imageUri);
                }
            }
        }
    }

    private void handleImageUri(Uri imageUri) {
        if (imagenesBitmap.size() < 8) {
            try {
                uriList.add(imageUri);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                imagenesBitmap.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mostrarImagenes();
        } else {
            CustomToast.showToast(getContext(), "Solo puedes seleccionar hasta 8 imágenes", Toast.LENGTH_SHORT);
        }
    }

    private void inicializarComponentes(View view) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        uriList = new ArrayList<>();
        imagenesBitmap = new ArrayList<>();
        imagenesLibro = new ArrayList<>();
        btnSubir = view.findViewById(R.id.btnComprar);
        imgTableLayout = view.findViewById(R.id.imgTableLayout);
        editTitulo = view.findViewById(R.id.editTitulo);
        editAutor = view.findViewById(R.id.editAutor);
        editPrecio = view.findViewById(R.id.editPrecio);
        editDescripcion = view.findViewById(R.id.editDescripcion);
        spinnerGenero = view.findViewById(R.id.spinnerGenero);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        progressBar = view.findViewById(R.id.progressBarAddLibro);
        imageViews = new ImageView[]{
                view.findViewById(R.id.imgLibro1),
                view.findViewById(R.id.imgLibro2),
                view.findViewById(R.id.imgLibro3),
                view.findViewById(R.id.imgLibro4),
                view.findViewById(R.id.imgLibro5),
                view.findViewById(R.id.imgLibro6),
                view.findViewById(R.id.imgLibro7),
                view.findViewById(R.id.imgLibro8)
        };

        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Genero.values());
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGenero);

        ArrayAdapter<CondicionLibro> adapterEstado = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, CondicionLibro.values());
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CustomToast.showToast(getContext(), "Permisos concedidos", Toast.LENGTH_SHORT);
            } else {
                CustomToast.showToast(getContext(), "Permisos denegados", Toast.LENGTH_SHORT);
            }
        }
    }
}
