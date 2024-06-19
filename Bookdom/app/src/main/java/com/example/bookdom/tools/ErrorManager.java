package com.example.bookdom.tools;

import android.util.Log;

public class ErrorManager {
    public static void mensajeDeError(String errorMessage, Throwable t) {;
        if (t.getMessage() != null) {
            errorMessage += ": " + t.getMessage();
        }
        Log.e("Error: ", errorMessage, t);
    }
}
