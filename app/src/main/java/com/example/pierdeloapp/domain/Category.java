package com.example.pierdeloapp.domain;

import java.io.Serializable;

public class Category {
    String img_url;
    String text;
    String type;

    public Category()  {
    }

    public String getImg_url() {
        return img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
