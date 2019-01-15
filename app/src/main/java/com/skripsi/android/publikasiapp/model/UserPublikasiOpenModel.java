package com.skripsi.android.publikasiapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "tb_user_publikasi_open")
public class UserPublikasiOpenModel {

    @NonNull
    @PrimaryKey
    private String id;
    @ColumnInfo(name = "open_pub_id")
    private String pub_id;
    private String email;
    private int duration;
    private String timestamp;
    private int is_uploaded;
    @Ignore
    private List<UserAktivitasPerHalaman> userAktivitasPerHalamen;

    // TODO: 7/9/2018 Atur ini database supaya bisa dimasukkin dbnya 


    public UserPublikasiOpenModel(String pub_id, String email, int duration,String timestamp, int is_uploaded) {
        this.pub_id = pub_id;
        this.email = email;
        this.duration = duration;
        this.timestamp = timestamp;
        this.is_uploaded = is_uploaded;
    }
@Ignore
    public UserPublikasiOpenModel(@NonNull String id, String pub_id, String email, int duration, String timestamp, List<UserAktivitasPerHalaman> userAktivitasPerHalamen) {
        this.id = id;
        this.pub_id = pub_id;
        this.email = email;
        this.duration = duration;
        this.timestamp = timestamp;
        this.userAktivitasPerHalamen = userAktivitasPerHalamen;
    }

    public List<UserAktivitasPerHalaman> getUserAktivitasPerHalamen() {
        return userAktivitasPerHalamen;
    }

    public void setUserAktivitasPerHalamen(List<UserAktivitasPerHalaman> userAktivitasPerHalamen) {
        this.userAktivitasPerHalamen = userAktivitasPerHalamen;
    }

    @Ignore
    public UserPublikasiOpenModel() {
    }

    public String getPub_id() {
        return pub_id;
    }

    public void setPub_id(String pub_id) {
        this.pub_id = pub_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(int is_uploaded) {
        this.is_uploaded = is_uploaded;
    }
}

