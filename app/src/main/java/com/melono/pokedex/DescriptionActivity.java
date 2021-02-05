package com.melono.pokedex;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.melono.pokedex.adapter.EvolutionAdapter;
import com.melono.pokedex.model.Evolution;
import com.melono.pokedex.pokemon.EvolutionPokemon;
import com.melono.pokedex.pokemon.PokemonDao;
import com.melono.pokedex.results.Results;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DescriptionActivity extends AppCompatActivity {
    // description variables
    private ImageView pokemonImageDescription;
    private TextView pokemonNameDescription, pokemonDescDescription;
    private LinearLayout descriptionLayout;
    private TextView characteristics;

    //commun variables
    private Button btnDescription, btnEvolution;
    private Results pokemon;
    private Evolution[] evolution;
    private CompositeDisposable disposable = new CompositeDisposable();
    // evolution variables
    private LinearLayout evolutionLayout;
    private PokemonDao pokemonApiEvolution;
    private PokemonDao pokemonApiEvolutionPokemon;
    private ImageView pokemonImageEvolution;
    private TextView pokemonNameEvolution;
    private List<EvolutionPokemon> listEvolutionPokemons = new ArrayList<EvolutionPokemon>();;
    private RecyclerView rvEvolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // description views
        pokemonImageDescription = findViewById(R.id.pokemon_image_description);
        pokemonNameDescription = findViewById(R.id.pokemon_name_description);
        btnDescription = findViewById(R.id.btn_description);
        descriptionLayout = findViewById(R.id.description_layout);
        pokemonDescDescription = findViewById(R.id.pokemon_desc_description);
        characteristics = findViewById(R.id.characteristics);
        // evolution views
        btnEvolution = findViewById(R.id.btn_evolution);
        evolutionLayout = findViewById(R.id.evolution_layout);
        pokemonImageEvolution = findViewById(R.id.pokemon_image_evolution);
        pokemonNameEvolution = findViewById(R.id.pokemon_name_evolution);
        // recyler view adapter evolution
        rvEvolution = findViewById(R.id.rv_evolution);
        EvolutionAdapter evolutionAdapter = new EvolutionAdapter();
        rvEvolution.setLayoutManager(new LinearLayoutManager(DescriptionActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvEvolution.setAdapter(evolutionAdapter);
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofitEvolution = new Retrofit.Builder()
                .baseUrl("https://pokeapi.glitch.me/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        pokemonApiEvolution = retrofitEvolution.create(PokemonDao.class);

        Retrofit retrofitEvolutionPokemon = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        pokemonApiEvolutionPokemon = retrofitEvolutionPokemon.create(PokemonDao.class);
        if(getIntent().getExtras() != null){
            pokemon = (Results) getIntent().getSerializableExtra("pokemon");

            Call<Evolution[]> call = pokemonApiEvolution.getEvolution(pokemon.getId());

            call.enqueue(new Callback<Evolution[]>() {
                @Override
                public void onResponse(Call<Evolution[]> call, Response<Evolution[]> response) {
                    evolution = response.body();
                    // affichage de description par défaut description:
                    Log.d("pokemon", pokemon.getImage());
                    Glide.with(pokemonImageDescription.getContext())
                            .load(pokemon.getImage())
                            .into(pokemonImageDescription);
                    pokemonNameDescription.setText(pokemon.getName() );

                    // fill other characteristics
                    characteristics.setText("  Tail : " + pokemon.getHeight() + " m \n\n  weight : " + pokemon.getWeight() + " kg \n\n" +
                            "  Order : " + pokemon.getOrder() + "\n\n  base_experience : " + pokemon.getBase_experience());

                    // affichage de description par défaut evolution:
                    Glide.with(pokemonImageEvolution.getContext())
                            .load(pokemon.getImage())
                            .into(pokemonImageEvolution);
                    pokemonNameEvolution.setText(pokemon.getName());

                    if(evolution != null){
                        Log.d("nameEvolu", "not nul");
                        pokemonDescDescription.setText(evolution[0].getDescription());
                        Observable<String> EvolutionPokemonObservable = Observable
                                // use an operator
                                .fromIterable(evolution[0].getFamily().getEvolutionLine())
                                // what Thread you want to work on
                                .subscribeOn(Schedulers.io())
                                .filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<String>() {
                                            @Override
                                            public boolean test(String task) {
                                                Log.d("tag", "test" + Thread.currentThread().getName());

                                                return true;
                                            }
                                        }
                                )
                                .observeOn(Schedulers.io());

                        EvolutionPokemonObservable.subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.d("tag", "onSubscribe: called.");
                                disposable.add(d);
                            }
                            @Override
                            public void onNext(@NonNull String pokemonName) {
                                Log.d("TAG", "onNext: " + Thread.currentThread().getName());
                                Log.d("nameEvolution", "onNext: " + pokemonName);

                                Call<EvolutionPokemon> call = pokemonApiEvolutionPokemon.getEvolutionPokemonId(pokemonName.toLowerCase());
                                call.enqueue(new Callback<EvolutionPokemon>() {
                                    @Override
                                    public void onResponse(Call<EvolutionPokemon> call, Response<EvolutionPokemon> response) {
                                        if(!response.isSuccessful()){
                                            Log.d("no_response_error", "there is an error that occured");
                                            return;
                                        }
                                        EvolutionPokemon pokemonEvolution = response.body();
                                        System.out.println(pokemonEvolution);
                                        if(pokemonEvolution != null){
                                            Log.d("taillePokemon", String.valueOf(pokemonEvolution.getWeight()));
                                            int idPokemon = pokemonEvolution.getId();
                                            if(!pokemonEvolution.getName().equals(pokemon.getName()))
                                            {
                                                pokemonEvolution.setImage("https://pokeres.bastionbot.org/images/pokemon/"+ idPokemon +".png");
                                                listEvolutionPokemons.add(pokemonEvolution);
                                            }

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<EvolutionPokemon> call, Throwable t) {
                                        Log.d("failure2", "PokemonEvolution is null");
                                    }
                                });
                            }
                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("TAG", "onError: ", e);
                            }
                            @Override
                            public void onComplete() {
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<Evolution[]> call, Throwable t) {
                    Log.d("failure1", "evolution is null");
                }
            });

            btnEvolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descriptionLayout.setVisibility(View.INVISIBLE);
                    evolutionLayout.setVisibility(View.VISIBLE);
                    evolutionAdapter.updateRecyclerView(listEvolutionPokemons);

                }
            });

            btnDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descriptionLayout.setVisibility(View.VISIBLE);
                    evolutionLayout.setVisibility(View.INVISIBLE);
                }
            });

        }
    }
}




