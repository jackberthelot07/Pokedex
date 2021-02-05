package com.melono.pokedex.pokemon;

import androidx.room.Entity;

@Entity(tableName = "PokemonSpecies")
public class PokemonSpecies {

    private int id;
    private String image;
    private String name;
    private String url;
    private int base_experience;
    //    @ColumnInfo(name = "height")/
    private int height;
    //
    private int weight;


    public PokemonSpecies(int id, String image, String name, String url, int base_experience, int height, int weight) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.url = url;
        this.base_experience = base_experience;
        this.height = height;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(int base_experience) {
        this.base_experience = base_experience;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

