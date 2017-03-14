package com.thilinas.twallpapers.activities;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.thilinas.twallpapers.R;
import com.thilinas.twallpapers.adapters.PhotoListAdapter;
import com.thilinas.twallpapers.adapters.recycleViewListeners.EndlessRecyclerViewScrollListener;
import com.thilinas.twallpapers.adapters.recycleViewListeners.RecyclerItemClickListener;
import com.thilinas.twallpapers.adapters.recycleViewListeners.StartSnapHelper;
import com.thilinas.twallpapers.customs.WaveProgressDialog;
import com.thilinas.twallpapers.models.Photo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoViewActivity extends NetworkChangeActivity {

    private List<Photo> photoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoListAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int start=0;
    private final int limit=5;
    private static int total;
    private final String base_url = "http://192.99.54.24/ukku/api.php?";

    private WaveProgressDialog progressDialog;
    private FavoriteItemsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prepareList();
        datasource = new FavoriteItemsDataSource(this);
        datasource.open();
        send();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();

    }

    private void prepareList(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new PhotoListAdapter(photoList,getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
       /* SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);*/
        // custom helper
        SnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        try {
                            // final Notification notification = itemList.get(position);
                        }catch (Exception e){

                        }
                    }
                    @Override
                    public void onLongClick(View view, int position) {  }
                })
        );
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(start<=total){
                    start+=limit;
                    send();
                }
            }
        };
        mAdapter.setOnIconsClickedListener(new PhotoListAdapter.OnIconsClickedListener() {
            @Override
            public void onSetWallClicked(String url) {
                setDesktop(url);
            }

            @Override
            public void onItemLiked(Photo photo) {
                photo.setFav(true);
                Log.i("YYYYY"," item d"+photo.getFav().toString());
                datasource.insertItem(photo);
                Toast.makeText(getApplicationContext(),"Item successfully added to your favourites.",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.pink, R.color.indigo, R.color.lime);
        swipeRefreshLayout.setOnRefreshListener(new     SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                start = 0;
                send();
            }
        });
    }

    public String getUrl(){
        String url = base_url + "offset="+start+ "&quantity=" +limit;
        return  url;
    }

    public void send(){
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> jsonParams = new HashMap<String, String>();
        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST,getUrl(),
                new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
                            photoList.clear();
                            mAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        onResponceReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        queue.add(postRequest);
    }

    public synchronized void onResponceReceived(JSONObject response){
        try {
            JSONObject pagination = response.getJSONObject("pagination");
            Log.i("XXXX",pagination.toString());
            total = pagination.getInt("total");
            JSONArray files = response.getJSONArray("files");
            JSONObject file;
            Photo photo;
            int size = files.length();
            if(size>0){
                for (int i=0;i<size;i++){
                    file = files.getJSONObject(i);
                    photo = new Photo();
                    photo.setUrl(file.getString("url"));
                    photo.setpId(String.valueOf(i));
                    photoList.add(photo);
                }
                mAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }

    public void setDesktop(String url){
        Log.i("XXXX","setting");
        progressDialog = WaveProgressDialog.getInstance(this,"Setting up the wallpaper.\nPlease wait.");
        final WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        Glide.with(getApplicationContext())
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              try {
                                  myWallpaperManager.setBitmap(resource);
                                  Toast.makeText(getApplicationContext(),"Wallpaper successfully set.",Toast.LENGTH_SHORT).show();
                              } catch (IOException e) {
                                  e.printStackTrace();
                                  Toast.makeText(getApplicationContext(),"Unable to set wallpaper. Please try again.",Toast.LENGTH_SHORT).show();
                              }finally {
                                  if (progressDialog != null) progressDialog.dismiss();
                              }
                          }
                      }
                );
    }
}
