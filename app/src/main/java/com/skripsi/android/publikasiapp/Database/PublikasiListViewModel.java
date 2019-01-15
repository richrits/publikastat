package com.skripsi.android.publikasiapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.List;

public class PublikasiListViewModel extends AndroidViewModel {
    private final LiveData<List<Publikasi>> publikasiList;

    private PublikasiDatabase publikasiDatabase;

    public PublikasiListViewModel(@NonNull Application application) {
        super(application);

        publikasiDatabase = PublikasiDatabase.getDatabase(this.getApplication());
        publikasiList = publikasiDatabase.publikasiDaoAccess().fetchAllPublikasi();
    }

    public LiveData<List<Publikasi>> getPublikasiList() {
        return publikasiList;
    }

    public void deleteItem(Publikasi publikasi) {
        new deleteAsyncTask(publikasiDatabase).execute(publikasi);
    }

    private static class deleteAsyncTask extends AsyncTask<Publikasi, Void, Void> {

        private PublikasiDatabase db;

        deleteAsyncTask(PublikasiDatabase publikasiDatabase) {
            db = publikasiDatabase;
        }


        @Override
        protected Void doInBackground(final Publikasi... params) {
            db.publikasiDaoAccess().deletePublikasi(params[0]);
            return null;
        }
    }

}
