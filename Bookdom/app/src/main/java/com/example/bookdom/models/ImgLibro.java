package com.example.bookdom.models;

import java.io.Serializable;

public class ImgLibro implements Serializable {
    private String url;

    public ImgLibro(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
