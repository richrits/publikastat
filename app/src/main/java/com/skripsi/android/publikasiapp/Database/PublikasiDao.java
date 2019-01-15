package com.skripsi.android.publikasiapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.List;

@Dao
public interface PublikasiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySinglePublikasi (Publikasi publikasi);

    @Insert
    void insertMultiplePublikasi (List<Publikasi> publikasiList);

    @Query("SELECT * FROM tb_publikasi WHERE pub_id = :pub_id")
    Publikasi fetchOnePublikasibyPubId (int pub_id);

    @Query("SELECT * FROM tb_publikasi ORDER BY pub_id DESC")
    LiveData<List<Publikasi>> fetchAllPublikasi();

//    @Query("SELECT * FROM publikasi")
//    List<Cursor> getCursorAll();

    @Update
    void updatePublikasi (Publikasi publikasi);

    @Delete
    void deletePublikasi (Publikasi publikasi);

    @Delete
    public void deleteAllPublikasi (List<Publikasi> publikasiList);

    @Query("DELETE FROM tb_publikasi")
    void droptable();
}
