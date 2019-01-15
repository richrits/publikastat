package com.skripsi.android.publikasiapp.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.skripsi.android.publikasiapp.Database.AddPublikasiViewModel;
import com.skripsi.android.publikasiapp.Database.PublikasiDatabase;
import com.skripsi.android.publikasiapp.Database.PublikasiListViewModel;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.activity.MainActivity;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.adapters.ListPublikasiAdapter;
import com.skripsi.android.publikasiapp.model.Publikasi;
import com.skripsi.android.publikasiapp.utils.ActivityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

//    private static HomeFragment INSTANCE = null;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private PublikasiListViewModel publikasiListViewModel;
    private ListPublikasiAdapter adapter;
    private AddPublikasiViewModel addPublikasiViewModel;
    private List<Publikasi> publikasiList;
    private Context context;
    private static final String DATABASE_PUBLIKASI = "publikasi_db";
    private PublikasiDatabase pubDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //Recycler view variables
        rootView.setTag(TAG);

//        ActivityUtils.addActivityToServer(null);

        recyclerView = rootView.findViewById(R.id.recycler_view_1);
//        publikasiList = new ArrayList<>();
        adapter = new ListPublikasiAdapter(getActivity().getApplicationContext(), new ArrayList<Publikasi>());


        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        publikasiListViewModel = ViewModelProviders.of(this).get(PublikasiListViewModel.class);

        publikasiListViewModel.getPublikasiList().observe(HomeFragment.this, new Observer<List<Publikasi>>() {
            @Override
            public void onChanged(@Nullable List<Publikasi> publikasi) {
                adapter.addItems(publikasi);
            }
        });

        pubDatabase = Room.databaseBuilder(getActivity().getApplication(), PublikasiDatabase.class, "db_publikasiApp").build();

//        new AsyncClassFetchAll().execute();




//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                publikasiList = pubDatabase.publikasiDaoAccess().fetchAllPublikasi();
//            }
//        }).start();



            callData();





//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if(gridLayoutManager.findLastCompletelyVisibleItemPosition()==publikasiList.size()-1){
//                    callData(publikasiList.get(publikasiList.size()-1).getPub_id());
//                }
//            }
//        });


        return rootView;
    }


    private void callData() {

        addPublikasiViewModel = ViewModelProviders.of(this).get(AddPublikasiViewModel.class);

        Log.d(TAG, "load_data_from_server: started");

//        swipe.setRefreshing(true);

        //Creating volley request obj
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_PUBLIKASI2, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.e(TAG, response.toString());
                //parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj =  response.getJSONObject(i);

//                        final Publikasi data = new Publikasi(
//                                obj.getInt("pub_id"),
//                                obj.getString("title"),
//                                obj.getString("kat_no"),
//                                obj.getString("pub_no"),
//                                obj.getString("issn"),
//                                obj.getString("abstract"),
//                                obj.getString("sch_date"),
//                                obj.getString("rl_date"),
//                                obj.getString("cover"),
//                                obj.getString("pdf"),
//                                obj.getString("size"));


                        addPublikasiViewModel.addPublikasi(new Publikasi(obj.getInt("pub_id"),
                                        obj.getString("title"),
                                        obj.getString("kat_no"),
                                        obj.getString("pub_no"),
                                        obj.getString("issn"),
                                        obj.getString("abstract"),
                                        obj.getString("sch_date"),
                                        obj.getString("rl_date"),
                                        obj.getString("cover"),
                                        obj.getString("pdf"),
                                        obj.getString("size")));
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        pubDatabase.publikasiDaoAccess().insertOnlySinglePublikasi(data);
////                                        Long result = pubDatabase.publikasiDaoAccess().insertOnlySinglePublikasi(data);
//                                        Log.d(TAG, "run : berhasil menambah 1 data publikasi " );
//                                    }
//                                }).start();

                    } catch (JSONException e) {
                        System.out.println("End of content");
                    }

                }
//                swipe.setRefreshing(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("End of content");
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
//                swipe.setRefreshing(false);
            }
        });
        //adding request to request queuee
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

//    @Override
//    public void onRefresh() {
//        Log.d(TAG, "onRefresh: start");
//        publikasiList.clear();
//  //      db.deletePublikasi();
//        callData(0);
//    }



//
//    private class AsyncClassFetchAll extends AsyncTask<Void, Void, Void>{
////        List<Publikasi> publikasiList;
//
//
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (!publikasiList.isEmpty()){
//                adapter = new ListPublikasiAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(),publikasiList);
//                recyclerView.setAdapter(adapter);
//            }else{
//                Log.d(TAG, "onPostExecute: publikasi list empty");
//            }
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            publikasiList = pubDatabase.publikasiDaoAccess().fetchAllPublikasi();
////            Log.i(TAG, "doInBackground: cek log");
////            for(Publikasi pub : publikasiList) {
////                Log.i(TAG, "doInBackground: data publikasi" + pub.getTitle());
////            }
//            if (publikasiList.isEmpty()){
//                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_PUBLIKASI2, new com.android.volley.Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(final JSONArray response) {
//                        Log.e(TAG, response.toString());
//                        //parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject obj =  response.getJSONObject(i);
//
//                                final Publikasi data = new Publikasi(
//                                        obj.getInt("pub_id"),
//                                        obj.getString("title"),
//                                        obj.getString("kat_no"),
//                                        obj.getString("pub_no"),
//                                        obj.getString("issn"),
//                                        obj.getString("abstract"),
//                                        obj.getString("sch_date"),
//                                        obj.getString("rl_date"),
//                                        obj.getString("cover"),
//                                        obj.getString("pdf"),
//                                        obj.getString("size"));
//
//                                publikasiList.add(data);
//
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Long result = pubDatabase.publikasiDaoAccess().insertOnlySinglePublikasi(data);
//                                        Log.d(TAG, "run : berhasil menambah 1 data publikasi " + result);
//                                    }
//                                }).start();
//
//                            } catch (JSONException e) {
//                                System.out.println("End of content");
//                            }
//
//                        }
////                swipe.setRefreshing(false);
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        System.out.println("End of content");
////                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
////                swipe.setRefreshing(false);
//                    }
//                });
//                //adding request to request queuee
//                AppController.getInstance().addToRequestQueue(jsonArrayRequest);
//
//            }else {
//
//                Log.d(TAG, "doInBackground: publikasi size " + publikasiList.size());
//
//
//            }

//
//            return null;
//        }
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.action_settings).setVisible(false);
//        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
    }



}
