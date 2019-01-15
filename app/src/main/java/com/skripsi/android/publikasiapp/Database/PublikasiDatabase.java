package com.skripsi.android.publikasiapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.skripsi.android.publikasiapp.model.Publikasi;
import com.skripsi.android.publikasiapp.model.UserAktivitasPerHalaman;
import com.skripsi.android.publikasiapp.model.UserPublikasiOpenModel;

@Database(entities = {Publikasi.class,UserPublikasiOpenModel.class, UserAktivitasPerHalaman.class}, version = 1, exportSchema = false)
public abstract class PublikasiDatabase extends RoomDatabase {

    public abstract PublikasiDao publikasiDaoAccess() ;

    public abstract UserPublikasiOpenDao userPublikasiOpenDaoAccess();

    public abstract UserHalamanPerPublikasiDao userHalamanPerPublikasiDaoAccess();


    private static PublikasiDatabase INSTANCE;

    public static PublikasiDatabase getDatabase(Context context){
        if (INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PublikasiDatabase.class, "publikasi_db").build();
        }
        return INSTANCE;
    }

}

