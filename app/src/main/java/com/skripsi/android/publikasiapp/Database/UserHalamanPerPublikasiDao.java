package com.skripsi.android.publikasiapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.skripsi.android.publikasiapp.model.Publikasi;
import com.skripsi.android.publikasiapp.model.UserAktivitasPerHalaman;

import java.util.List;

@Dao
public interface UserHalamanPerPublikasiDao {
    @Insert
    Long insertOnlySingleHalaman (UserAktivitasPerHalaman halaman);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleHalaman (List<UserAktivitasPerHalaman> halaman);

    @Query("SELECT * FROM tb_user_aktivitas_halaman ORDER BY id DESC LIMIT 5")
    List<UserAktivitasPerHalaman> userAktivitasPerHalamans();

    @Query("SELECT * FROM tb_user_aktivitas_halaman WHERE is_uploaded = 0")
    List<UserAktivitasPerHalaman> getAlluserAktivitasperhalamanUpload();

    @Query("UPDATE tb_user_aktivitas_halaman SET is_uploaded = 1 WHERE id IN (:ids) ")
    int updateUserAktivitasHalamanUploaded(List<Integer> ids);

    @Delete
    public void deleteAllUserHalaman (List<UserAktivitasPerHalaman> userAktivitasPerHalamen);

}
