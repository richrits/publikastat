package com.skripsi.android.publikasiapp.model;

import java.util.List;

public class UserActivityModel {
   private List<UserAktivitasPerHalaman> userAktivitasPerHalamans;
   private List<UserPublikasiOpenModel> userPublikasiOpenModels;

    public UserActivityModel() {
    }

    public UserActivityModel(List<UserAktivitasPerHalaman> userAktivitasPerHalamans, List<UserPublikasiOpenModel> userPublikasiOpenModels) {
        this.userAktivitasPerHalamans = userAktivitasPerHalamans;
        this.userPublikasiOpenModels = userPublikasiOpenModels;
    }

    public List<UserAktivitasPerHalaman> getUserAktivitasPerHalamans() {
        return userAktivitasPerHalamans;
    }

    public void setUserAktivitasPerHalamans(List<UserAktivitasPerHalaman> userAktivitasPerHalamans) {
        this.userAktivitasPerHalamans = userAktivitasPerHalamans;
    }

    public List<UserPublikasiOpenModel> getUserPublikasiOpenModels() {
        return userPublikasiOpenModels;
    }

    public void setUserPublikasiOpenModels(List<UserPublikasiOpenModel> userPublikasiOpenModels) {
        this.userPublikasiOpenModels = userPublikasiOpenModels;
    }
}
