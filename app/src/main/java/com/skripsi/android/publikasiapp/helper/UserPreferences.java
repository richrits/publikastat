package com.skripsi.android.publikasiapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserPreferences {
    private static final String TAG = "UserPreferences";
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF = "userpref";


    private static final String PREF_NAME = "name";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PEKERJAAN = "pekerjaan";
    private static final String PREF_PENDIDIKAN = "pendidikan";

    public UserPreferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserPref(String name, String email, String pekerjaan, String pendidikan) {

        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("pekerjaan",pekerjaan);
        editor.putString("pendidikan_terakhir",pendidikan);


        // commit changes
        editor.commit();

        Log.d(TAG, "User pref modified!");
    }

    public void deleteUserPref(){
        editor.clear();
    }

    public String getPrefName() {
        return pref.getString(PREF_NAME,"Nama");
    }

    public  String getPrefEmail() {
        return pref.getString(PREF_EMAIL,"email");
    }
}
