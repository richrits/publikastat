package com.skripsi.android.publikasiapp.fragment;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skripsi.android.publikasiapp.Database.PublikasiDatabase;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.activity.LoginActivity;
import com.skripsi.android.publikasiapp.helper.SessionManager;
import com.skripsi.android.publikasiapp.helper.UserPreferences;
import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPubDibaca;
    private Button buttonLogout;
    private Button buttonListPubDibaca;
    private Fragment fragment;
    private List<Publikasi> publikasilist;
    private PublikasiDatabase pubDatabase;
    SharedPreferences sharedPreferences;

//    private static ProfileFragment INSTANCE = null;
    private SessionManager session;
    private UserPreferences userPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        rootView.setTag(TAG);
        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);
//        txtPubDibaca = (TextView)rootView.findViewById(R.id.txtJlhPub);

        buttonLogout = (Button)rootView.findViewById(R.id.btnLogout);
        buttonListPubDibaca = (Button)rootView.findViewById(R.id.btnPubDibaca);





        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        userPreferences = new UserPreferences(getActivity().getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
//        HashMap<String, String> user = db.getUserDetails();
//
//        String name = user.get("name");
//        String email = user.get("email");
//        String id = user.get("id");

        // Displaying the user details on the screen
        String nama = userPreferences.getPrefName();
        String email = userPreferences.getPrefEmail();

        txtName.setText(nama);
        txtEmail.setText(email);
//        txtPubDibaca.setText();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClicklogout: logout");
                logoutUser();
            }
        });

        buttonListPubDibaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new PernahBacaFragment();
                loadFragment(fragment);

            }
        });

        return rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.action_settings).setVisible(true);
//        menu.findItem(R.id.action_logout).setVisible(true);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void logoutUser() {

        Log.i(TAG, "logoutUser: " + publikasilist);
        deleteDatabasePublikasi();

        session.setLogin(false);
        userPreferences.deleteUserPref();




        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            Log.d(TAG, "onOptionsItemSelected: setting selected");
//
////            Intent startSettingsActivity = new Intent(this,SettingsActivity.class);
////            startActivity(startSettingsActivity);
//            return true;
//        } else if (id == R.id.action_logout) {
//            Log.d(TAG, "onOptionsItemSelected: logout selected");
//            logoutUser();
//        }
//        return super.onOptionsItemSelected(item);
//    }
    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: started");

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }

    private void deleteDatabasePublikasi(){
        pubDatabase = Room.databaseBuilder(getActivity().getApplication(), PublikasiDatabase.class, "db_publikasiApp").build();
        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                pubDatabase.publikasiDaoAccess().droptable();
            }
        });
        r.start();
    }

}
