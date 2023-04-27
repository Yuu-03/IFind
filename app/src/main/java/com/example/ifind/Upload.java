package com.example.ifind;

public class Upload {
    private String mimageUri;

    public Upload() {

    }

    public Upload( String imageUri){
        mimageUri = imageUri;
    }
    public String getImageUrl(){
        return mimageUri;
    }

    public void setImageUri(String imageUri) {
        mimageUri = imageUri;
    }
}

