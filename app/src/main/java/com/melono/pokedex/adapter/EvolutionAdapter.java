package com.melono.pokedex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.melono.pokedex.R;
import com.melono.pokedex.pokemon.EvolutionPokemon;

import java.util.ArrayList;
import java.util.List;

public class EvolutionAdapter extends RecyclerView.Adapter<EvolutionAdapter.ViewHolder> {
    private List<EvolutionPokemon> list_pokemon_evolution = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_evolution;
        TextView height_view, weight_view, order_view, name_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_evolution = itemView.findViewById(R.id.image_evolution);
            height_view = itemView.findViewById(R.id.height);
            weight_view = itemView.findViewById(R.id.weight);
            order_view = itemView.findViewById(R.id.order);
            name_view = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context  = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item_evolution_exemple = inflater.inflate(R.layout.item_evolution, parent, false);
        return new ViewHolder(item_evolution_exemple);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvolutionPokemon pokemon = this.list_pokemon_evolution.get(position);
        // fill pokemon image
        Glide.with(holder.image_evolution.getContext())
                .load(pokemon.getImage())
                .into(holder.image_evolution);
        // fill others fields
        holder.weight_view.setText(String.valueOf("Weight : " + pokemon.getWeight() + " kg"));
        holder.height_view.setText(String.valueOf("Tail : " + pokemon.getHeight() + " m"));
        holder.order_view.setText(String.valueOf("Order : " + pokemon.getOrder()));
        holder.name_view.setText(String.valueOf("Name : " + pokemon.getName()));


    }

    @Override
    public int getItemCount() {
        return this.list_pokemon_evolution.size();
    }


    public void updateRecyclerView(List<EvolutionPokemon> list_items_evolution){
        this.list_pokemon_evolution.clear();
        this.list_pokemon_evolution.addAll(list_items_evolution);
        notifyDataSetChanged();
    }


}

