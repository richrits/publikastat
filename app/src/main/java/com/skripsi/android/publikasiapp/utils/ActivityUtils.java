package com.skripsi.android.publikasiapp.utils;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.skripsi.android.publikasiapp.Database.PublikasiDatabase;
import com.skripsi.android.publikasiapp.Database.UserHalamanPerPublikasiDao;
import com.skripsi.android.publikasiapp.Database.UserPublikasiOpenDao;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.model.UserActivityModel;
import com.skripsi.android.publikasiapp.model.UserAktivitasPerHalaman;
import com.skripsi.android.publikasiapp.model.UserPublikasiOpenModel;

import java.lang.reflect.Type;
import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityUtils {
    private static final String TAG = "ActivityUtils";
    private static PublikasiDatabase publikasiDatabase;
    private static Context context;




    public static void addActivityToServer(final Context context) {
        Log.d(TAG, "addActivityToServer: mulai upload");
        publikasiDatabase = Room.databaseBuilder(context, PublikasiDatabase.class, "db_publikasiApp").build();


//        new AsyncClassActivity().execute();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();

                final UserPublikasiOpenDao userPublikasiOpenDao = publikasiDatabase.userPublikasiOpenDaoAccess();
                final UserHalamanPerPublikasiDao userHalamanPerPublikasiDao = publikasiDatabase.userHalamanPerPublikasiDaoAccess();

                final UserActivityModel userActivityModel = new UserActivityModel();
                userActivityModel.setUserPublikasiOpenModels(userPublikasiOpenDao.getAlluserPublikasiOpenUpload());
                userActivityModel.setUserAktivitasPerHalamans(userHalamanPerPublikasiDao.getAlluserAktivitasperhalamanUpload());

                final String json = gson.toJson(userActivityModel);

                final List<Integer> listIdAktivitas = new ArrayList<>();
                final List<String> listIdPublikasi = new ArrayList<>();

                for (UserAktivitasPerHalaman aktivitas : userActivityModel.getUserAktivitasPerHalamans()) {
                    listIdAktivitas.add(aktivitas.getId());
                }
                for (UserPublikasiOpenModel aktivitas : userActivityModel.getUserPublikasiOpenModels()) {
                    listIdPublikasi.add(aktivitas.getId());
                }

                Log.i(TAG, "hasil " + json);

                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppConfig.URL_ACTIVITY_USER, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                userHalamanPerPublikasiDao.updateUserAktivitasHalamanUploaded(listIdAktivitas);
                                userPublikasiOpenDao.updatePublikasiOpenUploaded(listIdPublikasi);
                            }
                        }).start();
//                            Type type = new TypeToken<UserActivityModel>(){}.getType();
//                            UserActivityModel userActivityModel1 = gson.fromJson(response,type);
//                            userPublikasiOpenDao.insertMultipleOpenPublikasi(userActivityModel1.getUserPublikasiOpenModels());
//                            userHalamanPerPublikasiDao.insertMultipleHalaman(userActivityModel1.getUserAktivitasPerHalamans());
                        Log.d(TAG, "Berhasil mengunggah aktivitas: " + response);

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "gagal mengunggah aktivitas" + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("data", json);

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        }).start();

    }


//    private static class AsyncClassActivity extends AsyncTask<Void, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//            final UserPublikasiOpenDao userPublikasiOpenDao = publikasiDatabase.userPublikasiOpenDaoAccess();
//            final UserHalamanPerPublikasiDao userHalamanPerPublikasiDao = publikasiDatabase.userHalamanPerPublikasiDaoAccess();
//
//            final UserActivityModel userActivityModel = new UserActivityModel();
//            userActivityModel.setUserPublikasiOpenModels(userPublikasiOpenDao.getAlluserPublikasiOpenUpload());
//            userActivityModel.setUserAktivitasPerHalamans(userHalamanPerPublikasiDao.getAlluserAktivitasperhalamanUpload());
//
//            final String json = gson.toJson(userActivityModel);
//
//            final List<Integer> listIdAktivitas = new ArrayList<>();
//            final List<Integer> listIdPublikasi = new ArrayList<>();
//
//            for (UserAktivitasPerHalaman aktivitas : userActivityModel.getUserAktivitasPerHalamans()) {
//                listIdAktivitas.add(aktivitas.getId());
//            }
//            for (UserPublikasiOpenModel aktivitas : userActivityModel.getUserPublikasiOpenModels()) {
//                listIdPublikasi.add(aktivitas.getId());
//            }
//
//            Log.i(TAG, "hasil " + json);
//
//            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppConfig.URL_ACTIVITY_USER, new com.android.volley.Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            userHalamanPerPublikasiDao.updateUserAktivitasHalamanUploaded(listIdAktivitas);
//                            userPublikasiOpenDao.updatePublikasiOpenUploaded(listIdPublikasi);
//                        }
//                    }).start();
////                            Type type = new TypeToken<UserActivityModel>(){}.getType();
////                            UserActivityModel userActivityModel1 = gson.fromJson(response,type);
////                            userPublikasiOpenDao.insertMultipleOpenPublikasi(userActivityModel1.getUserPublikasiOpenModels());
////                            userHalamanPerPublikasiDao.insertMultipleHalaman(userActivityModel1.getUserAktivitasPerHalamans());
//                    Log.d(TAG, "Berhasil mengunggah aktivitas: " + response);
//
//                }
//            }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(TAG, "gagal mengunggah aktivitas" + error.getMessage());
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("data", json);
//
//                    return params;
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(stringRequest);
//            return null;
//        }
//    }




}
