package com.example.moviesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.moviesapp.R;
import com.example.moviesapp.adapters.MovieAdapter;
import com.example.moviesapp.network.JsonParsor;
import com.example.moviesapp.network.MyVolley;
import com.example.moviesapp.pojo.MovieDetailPojo;
import com.example.moviesapp.pojo.MoviesPojo;
import com.example.moviesapp.storage.ENDPOINTS;
import com.example.moviesapp.storage.MySharedPreferences;
import com.example.moviesapp.utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {


    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.rating)
    TextView mRating;
    @BindView(R.id.year)
    TextView mYear;
    @BindView(R.id.type)
    TextView mType;
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.parent)
    LinearLayout mParent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fav) ImageView mFav;
    private MySharedPreferences sp;

    private RequestQueue mRequestQue;
    private MovieDetailPojo model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setupToolbar();
        sp = MySharedPreferences.getInstance(this);
        mRequestQue = MyVolley.getInstance().getRequestQueue();
        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            getMovieDetail(id);
        }
        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFav(model)){
                    removeFav(model);
                }
                else{
                    addToFav(model);
                }
            }
        });
    }

    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Movie Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getMovieDetail(String id) {
        if (Utils.isNetworkAvailable()) {
            Utils.showPB(mPb, mParent);
            StringRequest request = new StringRequest(Request.Method.GET,
                    ENDPOINTS.BASE_URL + "&i=" + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Utils.hidePB(mPb, mParent);


                    try {
                        if (JsonParsor.isReqSuccesful(response)) {
                            JSONObject movie = new JSONObject(response);
                             model = new MovieDetailPojo();
                            model.setTitle(movie.getString("Title"));
                            model.setYear(movie.getString("Year"));
                            model.setGenre(movie.getString("Genre"));
                            model.setDescription(movie.getString("Plot"));
                            model.setPosterImage(movie.getString("Poster"));
                            model.setRating(movie.getString("imdbRating"));
                            model.setId(movie.getString("imdbID"));
                            model.setType(movie.getString("Type"));

                            setupViews(model);

                        } else {
                            Utils.makeToast(JsonParsor.simpleParser(response));
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.hidePB(mParent, mPb);

                    Utils.parsingErrorToast();
                }
            });

            mRequestQue.add(request);

        } else {
            Utils.noInternetToast();
        }
    }

    private void setupViews(MovieDetailPojo model) {
        Picasso.get().load(model.getPosterImage()).into(mImage);
        mTitle.setText(model.getTitle());
        mDescription.setText(model.getDescription());
        mRating.setText(model.getRating());
        mYear.setText(model.getYear());
        mType.setText(model.getGenre());


        if (checkFav(model)) {
            mFav.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart));
        }


    }


    private boolean checkFav(MovieDetailPojo model) {
        String moviesIdStr = sp.getMoviesIds();

        List<String> movieIds = new ArrayList<>();
        Gson gson = new Gson();
        if (!moviesIdStr.isEmpty()) {
            movieIds = gson.fromJson(moviesIdStr, ArrayList.class);
            return movieIds.contains(model.getId());
        }

        return false;


    }

    private void addToFav(MovieDetailPojo model) {

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
                mFav.setImageDrawable(ContextCompat.
                        getDrawable(this, R.drawable.ic_heart));

            }
        } catch (JSONException e) {

        }


    }

    private void removeFav(MovieDetailPojo model) {

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
                mFav.setImageDrawable(ContextCompat.
                        getDrawable(this, R.drawable.ic_heart_outline));
            }

        }catch (JSONException e){

        }


    }
}