package com.melono.pokedex.results;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ResultsPokemonDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(Results mainData);
    //delete query
    @Delete
    void delete(Results  mainData);
    @Query("delete from Results where generationType = :generationType")
    void reset(String generationType);
    @Query("SELECT * FROM Results where generationType = :generationType")
    List<Results> getAll(String generationType);

}
