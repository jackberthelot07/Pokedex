package com.melono.pokedex.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.melono.pokedex.DescriptionActivity;
import com.melono.pokedex.R;
import com.melono.pokedex.results.Results;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public List<Results> listPokemon = new ArrayList<>();
    private Context context;
    public Adapter(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_pokemon;
        TextView name_pokemon;
        ProgressBar progress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_pokemon = itemView.findViewById(R.id.image_pokemon);
            name_pokemon = itemView.findViewById(R.id.name_pokemon);
            progress = itemView.findViewById(R.id.progress);
        }

        private void showProgressBar(boolean showProgressBar){
            if(showProgressBar){
                progress.setVisibility(View.VISIBLE);
            }
            else{
                progress.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Results result = listPokemon.get(position);
        holder.name_pokemon.setText(result.getName());
        if(result.getImage() == null){
            holder.showProgressBar(true);
            holder.image_pokemon.setImageResource(0);
        }else{
            holder.showProgressBar(false);
            Glide.with(holder.image_pokemon.getContext())
                    .load(result.getImage())
                    .into(holder.image_pokemon);
        }

        holder.image_pokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Results resDetails = listPokemon.get(holder.getAdapterPosition());

                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("pokemon", resDetails);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.listPokemon.size();
    }

    public void setResults(List<Results> results){
        this.listPokemon.clear();
        this.listPokemon.addAll(results);
        notifyDataSetChanged();
    }

    public void updateResult(Results result){
        listPokemon.set(listPokemon.indexOf(result), result);
        notifyItemChanged(listPokemon.indexOf(result));
        notifyDataSetChanged();
    }
    public void updateDetailsPokemon(Results detailsPokemon){

    }

}
