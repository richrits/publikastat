package com.skripsi.android.publikasiapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "tb_user_aktivitas_halaman")
    public class UserAktivitasPerHalaman {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String pub_open_id;
    private int halaman;
    private String aktivitas;
    private int duration;
    private String timestamp;
    private int is_uploaded;


    public UserAktivitasPerHalaman(@NonNull int id, String pub_open_id, int halaman, String aktivitas, int duration, String timestamp, int is_uploaded) {
        this.id = id;
        this.pub_open_id = pub_open_id;
        this.halaman = halaman;
        this.aktivitas = aktivitas;
        this.duration = duration;
        this.timestamp = timestamp;
        this.is_uploaded = is_uploaded;
    }



    @Ignore
    public UserAktivitasPerHalaman() {
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getPub_open_id() {
        return pub_open_id;
    }

    public void setPub_open_id(String pub_open_id) {
        this.pub_open_id = pub_open_id;
    }

    public int getHalaman() {
        return halaman;
    }

    public void setHalaman(int halaman) {
        this.halaman = halaman;
    }

    public String getAktivitas() {
        return aktivitas;
    }

    public void setAktivitas(String aktivitas) {
        this.aktivitas = aktivitas;
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

    public int getIs_uploaded() {
        return is_uploaded;
    }

    public void setIs_uploaded(int is_uploaded) {
        this.is_uploaded = is_uploaded;
    }

    @Override
    public String toString() {
        return "UserAktivitasPerHalaman{" +
                "id=" + id +
                ", pub_open_id=" + pub_open_id +
                ", halaman='" + halaman + '\'' +
                ", aktivitas='" + aktivitas + '\'' +
                ", duration=" + duration +
                '}';
    }
}
