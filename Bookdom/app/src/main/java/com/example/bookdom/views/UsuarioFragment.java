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
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookdom.R;
import com.example.bookdom.models.Usuario;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.IUsuarioApi;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.ErrorManager;
import com.example.bookdom.tools.usuarios.UsuarioManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioFragment extends Fragment {
    private TextView txtUserUsername;
    private TextView txtUserEmail;
    private ImageView imgUsuario;
    private Uri imageUri;
    private StorageReference storageRef;
    private ProgressBar progressBar;
    private final int PICK_IMAGE_FROM_GALLERY_REQUEST = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtUserUsername = view.findViewById(R.id.txtUserUsername);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        imgUsuario = view.findViewById(R.id.imgUsuarioLibro);
        progressBar = view.findViewById(R.id.progressBarUsuario);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imgUsuario.setOnClickListener(v -> showImagePickerDialog());

        manejarFragments(view);

        checkPermissions();
        establecerDatosUsuario();
    }

    private void manejarFragments(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutUsuario);
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(new VentasFragment());
        fragmentList.add(new ComprasFragment());
        fragmentList.add(new AjustesFragment());

        // Initialize with the first fragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutUsuario, fragmentList.get(0))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = fragmentList.get(tab.getPosition());

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.frameLayoutUsuario, fragment)
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

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_FROM_GALLERY_REQUEST);
        }
    }

    private void establecerDatosUsuario() {
        Usuario usuario = UsuarioManager.getUsuario();
        txtUserUsername.setText(usuario.getNombreUsuario());
        txtUserEmail.setText(usuario.getEmail());

        if (usuario.getFotoPerfil() != null) {
            String imageUrl = usuario.getFotoPerfil();
            Glide.with(getContext())
                    .load(imageUrl)
                    .into(imgUsuario);
        }
    }

    private void showImagePickerDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

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

    private void subirImagenFirebase() {
        // Eliminar imagen actual antes de subir la nueva
        String currentImageUrl = UsuarioManager.getUsuario().getFotoPerfil();
        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
            StorageReference currentImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentImageUrl);
            currentImageRef.delete().addOnSuccessListener(aVoid -> {
                // Imagen eliminada con éxito, ahora sube la nueva
                subirNuevaImagen();
            }).addOnFailureListener(e -> {
                // Error al eliminar la imagen, pero intenta subir la nueva de todos modos
                subirNuevaImagen();
            });
        } else {
            // No hay imagen actual, solo sube la nueva
            subirNuevaImagen();
        }
    }

    private void subirNuevaImagen() {
        String imageName = UUID.randomUUID().toString();
        StorageReference imageRef = storageRef.child("imagenesUsuarios/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri2 -> {
                            String imageUrl = uri2.toString();
                            guardarUsuarioConImagen(imageUrl);
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            ErrorManager.mensajeDeError("Error al obtener la URL de la imagen: ", e);
                        }))
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    ErrorManager.mensajeDeError("Error al subir la imagen: ", e);
                });
    }

    private void guardarUsuarioConImagen(String imageUrl) {
        RetrofitService retrofitService = new RetrofitService();
        IUsuarioApi usuarioApi = retrofitService.getRetrofit().create(IUsuarioApi.class);

        long id = UsuarioManager.getUsuario().getId();

        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("url", imageUrl);

        usuarioApi.saveUsuarioWithImage(id, urlMap).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    UsuarioManager.setUsuario(response.body());
                    establecerDatosUsuario();
                    progressBar.setVisibility(View.GONE);
                    CustomToast.showToast(getContext(), "Imagen actualizada con éxito", Toast.LENGTH_SHORT);
                } else {
                    manejarError(response);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ErrorManager.mensajeDeError("Error al actualizar la imagen: ", t);
            }
        });
    }

    private void manejarError(Response<?> response) {
        progressBar.setVisibility(View.GONE);
        String errorBody = "";
        try {
            errorBody = response.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ErrorManager.mensajeDeError("Error al actualizar la imagen: " + response.code() + " - " + errorBody, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST) {
            progressBar.setVisibility(View.VISIBLE);
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                imageUri = selectedImageUri;  // Guarda la URI seleccionada
                handleImageUri(selectedImageUri);
                subirImagenFirebase();
            } else if (imageUri != null) {
                handleImageUri(imageUri);
                subirImagenFirebase();
            } else {
                progressBar.setVisibility(View.GONE);
                ErrorManager.mensajeDeError("No se seleccionó ninguna imagen", null);
            }
        }
    }

    private void handleImageUri(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            imgUsuario.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
