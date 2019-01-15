package com.skripsi.android.publikasiapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.adapters.SearchPublikasiAdapter;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.helper.UserPreferences;
import com.skripsi.android.publikasiapp.model.Publikasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PernahBacaFragment extends Fragment {
    private static final String TAG = "PernahBacaFragment";

    private static PernahBacaFragment INSTANCE = null;


    private RecyclerView recyclerViewPernahBaca;
    private GridLayoutManager gridLayoutManager;
    private SearchPublikasiAdapter adapterPernahBaca;
    private List<Publikasi> pernahBacapublikasiList;
    private SwipeRefreshLayout swipe;
    private UserPreferences userPreferences;


    public PernahBacaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_pernah_baca, container, false);

        rootView.setTag(TAG);

        //Code disini
        //Inisiasi

        swipe = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_search_results);
        Log.d(TAG, "onCreateView: swipe initiated");
        recyclerViewPernahBaca = (RecyclerView)rootView.findViewById(R.id.recycler_view_pernah_baca);
        Log.d(TAG, "onCreateView: rview init");
        pernahBacapublikasiList = new ArrayList<>();
        Log.d(TAG, "onCreateView: pub init");
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewPernahBaca.setLayoutManager(gridLayoutManager);

        adapterPernahBaca = new SearchPublikasiAdapter(getContext(),pernahBacapublikasiList);
        recyclerViewPernahBaca.setAdapter(adapterPernahBaca);

        userPreferences = new UserPreferences(getActivity().getApplicationContext());


        // Fetching user details from sqlite

        final String email = userPreferences.getPrefEmail();

        tampilData(email);

//        swipe.setOnRefreshListener(this);
//        swipe.post(new Runnable() {
//            @Override
//            public void run() {
//                pernahBacapublikasiList.clear();
//                swipe.setRefreshing(true);
//                tampilData(email);
//            }
//        });



        return rootView;
    }
    private void tampilData(String email){
        Log.d(TAG, "CariData: start");
        adapterPernahBaca.notifyDataSetChanged();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(AppConfig.URL_PERNAH_BACA + email, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                //parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj =  response.getJSONObject(i);

                        Publikasi data = new Publikasi(obj.getInt("pub_id"), obj.getString("title"), obj.getString("kat_no"), obj.getString("pub_no"), obj.getString("issn"), obj.getString("abstract"), obj.getString("sch_date"), obj.getString("rl_date"),  obj.getString("cover"), obj.getString("pdf"), obj.getString("size"));

                        pernahBacapublikasiList.add(data);

                    } catch (JSONException e) {
                        System.out.println("End of content");
                    }

                }
                adapterPernahBaca.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error :" + error.getMessage());
                System.out.println("End of content");
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        //adding request to request queuee
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
//    @Override
//    public void onRefresh() {
//        db = new SQLiteHandler(getActivity().getApplicationContext());
//
//        // Fetching user details from sqlite
//        HashMap<String, String> user = db.getUserDetails();
//        final String email = user.get("email");
//
//        Log.d(TAG, "onRefresh: start");
//        pernahBacapublikasiList.clear();
//        tampilData(email);
//    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.action_settings).setVisible(false);
//        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
    }


}
