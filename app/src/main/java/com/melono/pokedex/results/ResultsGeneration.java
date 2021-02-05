package com.melono.pokedex.results;

public class ResultsGeneration {

    private int id;
    private String name;
    private String image;
    private String url;
    private String color;

    public ResultsGeneration(int id, String name, String image, String url, String color) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.url = url;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

