package com.example.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.activity.MovieDetailActivity;
import com.example.moviesapp.pojo.MoviesPojo;
import com.example.moviesapp.storage.MySharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private List<MoviesPojo> mList;

    private MySharedPreferences sp;

    public MovieAdapter(Context context, List<MoviesPojo> mList) {
        this.context = context;
        this.mList = mList;
        sp = MySharedPreferences.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MoviesPojo model = mList.get(position);
        Picasso.get().load(model.getPosterImage()).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.type.setText(model.getType());
        holder.year.setText(model.getYear());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("id", model.getId());
                context.startActivity(intent);
            }
        });


        holder.mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAC", "MUR FAV onClick: ");
                if (checkFav(model)) {
                    Log.d("TAC", "MUR FAV onClick: 1");

                    removeFav(model, holder);
                } else {
                    Log.d("TAC", "MUR FAV onClick: 2");

                    addToFav(model, holder);
                }
            }
        });
        if (checkFav(model)) {
            holder.mFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart));
        }


    }

    private boolean checkFav(MoviesPojo model) {
        String moviesIdStr = sp.getMoviesIds();

        List<String> movieIds = new ArrayList<>();
        Gson gson = new Gson();
        if (!moviesIdStr.isEmpty()) {
            movieIds = gson.fromJson(moviesIdStr, ArrayList.class);
            return movieIds.contains(model.getId());
        }

        return false;


    }

    private void addToFav(MoviesPojo model, MyViewHolder holder) {

        String moviesInStr = sp.getMovies();
        String moviesIdStr = sp.getMoviesIds();

        try {
            JSONObject movies = new JSONObject();


            List<String> movieIds = new ArrayList<>();
            Gson gson = new Gson();
            if (!moviesInStr.isEmpty()) {
                movies = new JSONObject(moviesInStr);


                movieIds = gson.fromJson(moviesIdStr, ArrayList.class);
            }

            if (!movieIds.contains(model.getId())) {
                JSONObject movie = new JSONObject();
                movie.put("imdbID", model.getId());
                movie.put("Title", model.getTitle());
                movie.put("Year", model.getYear());
                movie.put("Type", model.getType());
                movie.put("Poster", model.getPosterImage());


                movies.put(model.getId(),movie);
                movieIds.add(model.getId());
                sp.addMovies(movies.toString());
                sp.addMoviesIds(gson.toJson(movieIds, ArrayList.class));
                holder.mFav.setImageDrawable(ContextCompat.
                        getDrawable(context, R.drawable.ic_heart));

            }
        } catch (JSONException e) {

        }


    }

    private void removeFav(MoviesPojo model, MyViewHolder holder) {

        String moviesInStr = sp.getMovies();
        String moviesIdStr = sp.getMoviesIds();

        try {


            JSONObject movies = new JSONObject();
            List<String> movieIds = new ArrayList<>();
            Gson gson = new Gson();
            if (!moviesInStr.isEmpty()) {
                movies = new JSONObject(moviesInStr);
                movieIds = gson.fromJson(moviesIdStr, ArrayList.class);
            }

            if (movieIds.contains(model.getId())) {

                JSONObject movie = new JSONObject();
                movie.put("imdbID", model.getId());
                movie.put("Title", model.getTitle());
                movie.put("Year", model.getYear());
                movie.put("Type", model.getType());
                movie.put("Poster", model.getPosterImage());
                movies.remove(model.getId());
                movieIds.remove(model.getId());

                sp.addMovies(movies.toString());
                sp.addMoviesIds(gson.toJson(movieIds, ArrayList.class));
                holder.mFav.setImageDrawable(ContextCompat.
                        getDrawable(context, R.drawable.ic_heart_white));
            }

        }catch (JSONException e){

        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.year)
        TextView year;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.fav)
        ImageView mFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
