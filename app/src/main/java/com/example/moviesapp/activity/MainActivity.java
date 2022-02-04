package com.example.moviesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moviesapp.R;
import com.example.moviesapp.adapters.MovieAdapter;
import com.example.moviesapp.pojo.MoviesPojo;
import com.example.moviesapp.storage.MySharedPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {



    @BindView(R.id.no_fav)
    LinearLayout mNoFav;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search)
    FloatingActionButton mSearch;

    private List<MoviesPojo> mList =new ArrayList<>();
    private MySharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();

        sp =MySharedPreferences.getInstance(this);
//        sp.clearData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SearchMoviesActivity.class));

            }
        });

        getFavMovies();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getFavMovies();
    }

    private void  getFavMovies(){
    String moviesStr = sp.getMovies();

    String movieIdStr = sp.getMoviesIds();
    mList.clear();

      Log.d("TAC", "MUR getFavMovies: 1");
    if(!moviesStr.isEmpty()){
        Log.d("TAC", "MUR getFavMovies:2 ");


        try {

            List<String> ids = new Gson().fromJson(movieIdStr, ArrayList.class);
            JSONObject movies = new JSONObject(moviesStr);
            if (ids.isEmpty()) {
                mNoFav.setVisibility(View.VISIBLE);
                Log.d("TAC", "MUR getFavMovies: 3");

            } else {

                for (int i = 0; i < ids.size(); i++) {

                    JSONObject movieObj = movies.getJSONObject(ids.get(i));
                    MoviesPojo model = new MoviesPojo();
                    model.setId(movieObj.getString("imdbID"));
                    model.setTitle(movieObj.getString("Title"));
                    model.setYear(movieObj.getString("Year"));
                    model.setType(movieObj.getString("Type").toUpperCase());
                    model.setPosterImage(movieObj.getString("Poster"));
                    mList.add(model);
                }

                MovieAdapter adapter = new MovieAdapter(
                        MainActivity.this, mList);
                adapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(adapter);
                Log.d("TAC", "MUR getFavMovies: 4");

            }
        }catch (JSONException e){

        }
    }
    else {

        Log.d("TAC", "MUR getFavMovies: 5");

        mNoFav.setVisibility(View.VISIBLE);

    }

  }
   private void setupToolbar(){

        setSupportActionBar(mToolbar);
        assert  getSupportActionBar() != null;
        getSupportActionBar().setTitle("My Favourites!\uD83E\uDD0D ");
    }
}