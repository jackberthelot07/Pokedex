package com.melono.pokedex.pokemon;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.melono.pokedex.results.ResultsGeneration;

import java.util.ArrayList;
import java.util.List;

public class Generation {

    @PrimaryKey(autoGenerate = true)
    private int ID;
    @ColumnInfo(name = "count")
    private int count;
    @ColumnInfo(name = "next")
    private String next;
    @ColumnInfo(name = "previous")
    private String previous;
    @ColumnInfo(name = "results")
    private List<ResultsGeneration> results = new ArrayList<>();

    public Generation(int ID, int count, String next, String previous, List<ResultsGeneration> results) {
        this.ID = ID;
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<ResultsGeneration> getResults() {
        return results;
    }

    public void setResults(List<ResultsGeneration> results) {
        this.results = results;
    }
}

