package com.melono.pokedex.pokemon;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.melono.pokedex.results.Results;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "Pokemon")
public class Pokemon {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    public int getID() {
        return ID;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String image;
    @ColumnInfo(name = "count")
    private int count;
    @ColumnInfo(name = "next")
    private String next;

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

    @ColumnInfo(name = "previous")
    private String previous;
    @ColumnInfo(name = "results")
    private List<Results> results = new ArrayList<>();
    private int base_experience;
    //    @ColumnInfo(name = "height")/
    private int height;
    //
    private int weight;

    public Pokemon(int ID, int count, String next, String previous, List<Results> results, int base_experience, int height, int weight) {
        this.ID = ID;
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
        this.base_experience = base_experience;
        this.height = height;
        this.weight = weight;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public List<Results> getResults() {
        return results;
    }
    public void setResults(List<Results> results) {
        this.results = results;
    }
}
