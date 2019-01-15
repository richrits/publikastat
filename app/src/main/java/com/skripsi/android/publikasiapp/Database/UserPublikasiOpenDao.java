package com.skripsi.android.publikasiapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.skripsi.android.publikasiapp.model.Publikasi;
import com.skripsi.android.publikasiapp.model.UserAktivitasPerHalaman;
import com.skripsi.android.publikasiapp.model.UserPublikasiOpenModel;

import java.util.List;

@Dao
public interface UserPublikasiOpenDao {

    @Insert
    Long insertOnlySingleOpenPublikasi (UserPublikasiOpenModel publikasiOpenModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleOpenPublikasi (List<UserPublikasiOpenModel> userPublikasiOpenModels);


    @Update
    void update(UserPublikasiOpenModel... userPublikasiOpenModels);

    @Delete
    void delete(UserPublikasiOpenModel... userPublikasiOpenModels);



    @Query("SELECT * FROM tb_user_publikasi_open")
    List<UserPublikasiOpenModel> getAllPublikasiOpen();

    @Query("UPDATE tb_user_publikasi_open SET duration = :duration WHERE id = :id")
    int updatePublikasiOpenDuration(int duration, String id);

    @Query("SELECT * FROM tb_user_publikasi_open WHERE open_pub_id=:pub_id")
    List<UserPublikasiOpenModel> FindPublikasiOpenModelPerPub(final int pub_id);

    @Query("SELECT * FROM tb_user_publikasi_open ORDER BY id DESC LIMIT 3 ")
    List<UserPublikasiOpenModel> selectAllOpen();


    @Query("SELECT * FROM tb_user_publikasi_open WHERE is_uploaded = 0")
    List<UserPublikasiOpenModel> getAlluserPublikasiOpenUpload();

    @Query("UPDATE tb_user_publikasi_open SET is_uploaded = 1 WHERE id IN (:ids)")
    int updatePublikasiOpenUploaded(List<String> ids);

    @Delete
    public void deleteAllUserPublikasiOpen (List<UserPublikasiOpenModel> userPublikasiOpenModels);

}
