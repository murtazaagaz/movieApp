package com.example.moviesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.moviesapp.pojo.MoviesPojo;
import com.example.moviesapp.storage.ENDPOINTS;
import com.example.moviesapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMoviesActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @BindView(R.id.search)
    EditText mSearchEt;
    @BindView(R.id.searchBtn)
    CardView mSearchBtn;

    @BindView(R.id.next)
    ImageView mNext;
    @BindView(R.id.pre)ImageView mPre;
@BindView(R.id.count)
    TextView mCount;

@BindView(R.id.page)
LinearLayout mPageCount;
    private RequestQueue mRequestQue;

    private MovieAdapter adapter;
    private List<MoviesPojo> mList = new ArrayList<>();

    private int pageNum = 1;
    private int maxPage = 1;
    private  String oldSearchTerm = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        ButterKnife.bind(this);
        setupToolbar();

        mRequestQue = MyVolley.getInstance().getRequestQueue();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchMovies();

            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum++;
                if(pageNum<10){
                    mCount.setText("0"+pageNum);

                }
                else {
                    mCount.setText(pageNum);

                }
                searchMovies();
                mPre.setVisibility(View.VISIBLE);

            }
        });

        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum--;

                if(pageNum<10){
                    mCount.setText("0"+pageNum);

                }
                else {
                    mCount.setText(pageNum);

                }
                if(pageNum==1){
                    mPre.setVisibility(View.GONE);
                }
                searchMovies();

            }
        });


    }



    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Search Movies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    // We Are using OMDB API to SEARCH MOVIES
    private  void  searchMovies(){
        String search = mSearchEt.getText().toString().trim();
        if(search.isEmpty()){
            return;
        }


        if (!oldSearchTerm.equalsIgnoreCase(search)){
            pageNum =1;
            oldSearchTerm = search;
        }
        if(Utils.isNetworkAvailable()){

            Utils.showPB(mPb, mRecyclerView);
            StringRequest request = new StringRequest(Request.Method.GET, ENDPOINTS.BASE_URL+"&s="+search+"&page="+pageNum, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Utils.hidePB(  mPb, mRecyclerView);


                    try {
                        if (JsonParsor.isReqSuccesful(response)){
                            maxPage = 1;
                            mPageCount.setVisibility(View.VISIBLE);

                            JSONObject object = new JSONObject(response);
                            JSONArray searchArray = object.getJSONArray("Search");
                            mList.clear();

                            float totalRes = Integer.parseInt(object.getString("totalResults"));

                            maxPage = Math.round(totalRes/10);
                            if(maxPage == pageNum){
                                mNext.setVisibility(View.GONE);
                            }
                            else {
                                mNext.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < searchArray.length(); i++) {

                                JSONObject movieObj = searchArray.getJSONObject(i);
                                MoviesPojo model = new MoviesPojo();
                                model.setId(movieObj.getString("imdbID"));
                                model.setTitle(movieObj.getString("Title"));
                                model.setYear(movieObj.getString("Year"));
                                model.setType(movieObj.getString("Type").toUpperCase());
                                model.setPosterImage(movieObj.getString("Poster"));
                                mList.add(model);



                            }
                            adapter = new MovieAdapter(SearchMoviesActivity.this, mList);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(adapter);



                        }
                        else {
                            Utils.makeToast(JsonParsor.simpleParser(response));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.hidePB( mRecyclerView, mPb);

                    Utils.parsingErrorToast();
                }
            });

            mRequestQue.add(request);
        }else {
            Utils.noInternetToast();
        }
    }
}