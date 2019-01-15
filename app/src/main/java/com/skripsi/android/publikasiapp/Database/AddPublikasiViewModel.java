package com.skripsi.android.publikasiapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.skripsi.android.publikasiapp.adapters.ListPublikasiAdapter;
import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.Objects;

public class AddPublikasiViewModel extends AndroidViewModel {
    private static final String TAG = "AddPublikasiViewModel";
    private PublikasiDatabase publikasiDatabase;
    public AddPublikasiViewModel(@NonNull Application application) {
        super(application);

        publikasiDatabase = PublikasiDatabase.getDatabase(this.getApplication());
    }

    public void addPublikasi(final Publikasi publikasi){
        new addAsyncTask(publikasiDatabase).execute(publikasi);
    }

    private  static class addAsyncTask extends AsyncTask<Publikasi, Void, Void>{
        private PublikasiDatabase db;
        addAsyncTask(PublikasiDatabase publikasiDatabase){
            db = publikasiDatabase;
        }


        @Override
        protected Void doInBackground(final Publikasi... params) {
            db.publikasiDaoAccess().insertOnlySinglePublikasi(params[0]);
            return null;
        }
    }
}
