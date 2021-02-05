package com.melono.pokedex.pokemon;

import com.melono.pokedex.model.Evolution;
import com.melono.pokedex.results.Results;
import com.melono.pokedex.results.ResultsGeneration;
import com.melono.pokedex.utils.CountGenerations;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PokemonDao {

    @GET("api/v2/pokemon")
    Observable<Pokemon> getPokemons(
            @Query("offset") int offset,
            @Query("limit") int limit
    );
    @GET("api/v2/pokemon/{name}")
    Call<Results> getPokemonsDetailsByName(@Path("name") String name);
    @GET
    Observable<Results> getPokemonsDetails(@Url String url);

    @GET("api/v2/pokemon/{name}")
    Observable<Results> getPokemonsDetails3(@Path("name") String name);
    @GET("/v1/pokemon/counts")
    Call<CountGenerations> getCountGeneration();

    @GET("api/v2/generation")
    Observable<Generation> getGeneration();
    @GET
    Observable<ResultsGeneration> getGenerationsDetails(@Url String url);
    // Evolution requests :
    @GET("v1/pokemon/{id}")
    Call<Evolution[]> getEvolution(@Path("id") int id);

    @GET("api/v2/pokemon/{name}")
    Call<EvolutionPokemon> getEvolutionPokemonId(@Path("name") String name);

}

