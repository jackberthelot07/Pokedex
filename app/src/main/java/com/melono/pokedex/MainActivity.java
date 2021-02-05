package com.melono.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.melono.pokedex.adapter.Adapter;
import com.melono.pokedex.adapter.AdapterGeneration;
import com.melono.pokedex.pokemon.Generation;
import com.melono.pokedex.pokemon.Pokemon;
import com.melono.pokedex.pokemon.PokemonDao;
import com.melono.pokedex.results.Results;
import com.melono.pokedex.results.ResultsDB;
import com.melono.pokedex.results.ResultsGeneration;
import com.melono.pokedex.utils.CountGenerations;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterGeneration.callbackMethods{
    private LinearLayout layoutLoading;
    private ResultsDB database;
    Adapter adapter;
    AdapterGeneration adapterGeneration;
    private RecyclerView rv;
    private RecyclerView rvGeneration;
    private PokemonDao pokemonApi;
    private PokemonDao pokemonApiCount;
    private CompositeDisposable disposables = new CompositeDisposable();
    private CountGenerations countGenerations;
    private String activeGeneration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rvGeneration = findViewById(R.id.rv_generation);
        database = ResultsDB.getInstance(this);
        layoutLoading = findViewById(R.id.layoutLoading);
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                layoutLoading.setVisibility(View.GONE);
            }
        };

        new Handler().postDelayed(runnable, 9000);
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        pokemonApi = retrofit.create(PokemonDao.class);
        Retrofit retrofitCount = new Retrofit.Builder()
                .baseUrl("https://pokeapi.glitch.me/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        pokemonApiCount = retrofitCount.create(PokemonDao.class);
        getGenerationCount();
        rv.setLayoutManager(new GridLayoutManager(this, 3   ));
        adapterGeneration = new  AdapterGeneration(this);
        rvGeneration.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new Adapter(this);
        rv.setAdapter(adapter);
        rvGeneration.setAdapter(adapterGeneration);
    }

    public void setResults(List<Results> results){
        adapter.setResults(results);
    }
    public void updateDetailsPokemon(Results detailsPokemon) {
        if(adapter != null){
            adapter.updateDetailsPokemon(detailsPokemon);
        }
    }

    public void updateResult(Results result){
        adapter.updateResult(result);
    }
    // get count generation
    private void getGenerationCount(){
        Call<CountGenerations> call = pokemonApiCount.getCountGeneration();

        call.enqueue(new Callback<CountGenerations>() {
            @Override
            public void onResponse(Call<CountGenerations> call, Response<CountGenerations> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "this page my be deleted, code : "+ response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                countGenerations = response.body();
                Log.d("messageA", String.valueOf(countGenerations.getGen1()));
            }

            @Override
            public void onFailure(Call<CountGenerations> call, Throwable t) {
                Toast.makeText(MainActivity.this, "la requete n'a pas aboutie", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setGenerationName(String ActiveGeneration){
        this.activeGeneration = ActiveGeneration;
    }


    public @NonNull Observable<Results> getPokemonsObservable(int offset, int limit){
        return  pokemonApi
                .getPokemons(offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Pokemon, ObservableSource<Results>>() {
                    @Override
                    public ObservableSource<Results> apply(Pokemon pokemon) throws Throwable {
                        adapter.setResults(pokemon.getResults());
                        Log.d("pokemonN", String.valueOf(pokemon.getResults().get(2).getName()));
                        return Observable.fromIterable(pokemon.getResults());
                    }
                });
    }

    public Observable<Results> getDetailsPokemonObservable(final Results result, String generationType){
        return pokemonApi
                .getPokemonsDetails(result.getUrl())
                .map(new Function<Results, Results>() {
                    @Override
                    public Results apply(Results result2) throws Throwable {
                        result.setId(result2.getId());
                        result.setBase_experience(result2.getBase_experience());
                        result.setOrder(result2.getOrder());
                        result.setHeight(result2.getHeight());
                        result.setGenerationType(generationType);
                        result.setWeight(result2.getWeight());
                        result.setImage("https://pokeres.bastionbot.org/images/pokemon/"+ result2.getId() +".png");
                        Log.d("setImage", "ddddd");
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<ResultsGeneration> getDetailsGenerationObservable(final ResultsGeneration result){
        return pokemonApi
                .getGenerationsDetails(result.getUrl())
                .map(new Function<ResultsGeneration, ResultsGeneration>() {
                    @Override
                    public ResultsGeneration apply(ResultsGeneration result2) throws Throwable {
                        result.setId(result2.getId());
                        result.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMQUN42IwmH8Vst7Ftxykw-Z67iEVjl6TbmQ&usqp=CAU");
                        return result;
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    public @NonNull Observable<ResultsGeneration> getGenerationObservable(){

        return  pokemonApi
                .getGeneration()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Generation, ObservableSource<ResultsGeneration>>() {
                    @Override
                    public ObservableSource<ResultsGeneration> apply(Generation generation) throws Throwable {
                        // adapter here
                        for(ResultsGeneration gen: generation.getResults()){
                            gen.setColor("#000000");
                            gen.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMQUN42IwmH8Vst7Ftxykw-Z67iEVjl6TbmQ&usqp=CAU");
                        }
                        adapterGeneration.setResults(generation.getResults());
                        // stockage dans la base de donnees
                        Log.d("setImage", "");
                        return Observable.fromIterable(generation.getResults());
                    }
                });


    }



}
