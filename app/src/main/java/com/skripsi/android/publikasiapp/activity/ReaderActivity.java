package com.skripsi.android.publikasiapp.activity;
import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDoubleTapListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.listener.OnZoomListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skripsi.android.publikasiapp.Database.PublikasiDatabase;
import com.skripsi.android.publikasiapp.helper.UserPreferences;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.model.UserAktivitasPerHalaman;
import com.skripsi.android.publikasiapp.model.UserPublikasiOpenModel;
import com.skripsi.android.publikasiapp.utils.ActivityUtils;
import com.skripsi.android.publikasiapp.utils.NetworkUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * Created by Harits on 3/1/2018.
 */

public class ReaderActivity extends AppCompatActivity{
    private PDFView pdfView;
//    private AppCompatSeekBar seekBar;
    private ProgressBar progressBar;
    private static final String TAG = "ReaderActivity";
    private boolean unduhflag;

    public static final String PREFERENCE = "preference";
    public static final String PREF_TITLE = "title";
    public static final String PREF_EMAIL = "email";
    public int openPub = 0;
    public int closePub = 0;
    public int startHalamanPub = 0;
    public int endHalamanPub = 0;
    private Toast pagetoast = null;
    private UserPreferences userPreferences;
    private NetworkUtil networkUtil;
    private PublikasiDatabase pubDatabase;
    public String pub_open_id;
    public long halaman_id = 0;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        AppController.getInstance().setCurrentTitle(getIntent().getStringExtra("title"));

        userPreferences = new UserPreferences(getApplicationContext());

        pub_open_id = UUID.randomUUID().toString().replace("-", "");


        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        pdfView=(PDFView)findViewById(R.id.pdfView);


        actionBar.setTitle(getIntent().getStringExtra("title"));
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        } pubDatabase = Room.databaseBuilder(getApplicationContext(), PublikasiDatabase.class, "db_publikasiApp").build();


//        networkUtil = new NetworkUtil();
//        if (!networkUtil.isOnline(getApplicationContext())){
//              Toast.makeText(this,"Tidak ada jaringan!",Toast.LENGTH_LONG);
//        }else{
//
//
//
//        }
        getIncomingIntent();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
//            case R.id.unduhbutton:
//                unduhflag = true;
//                Toast.makeText(this,"pdf terunduh silahkan lihat pada profil -> unduhan",Toast.LENGTH_LONG).show();
//                return true;
            case android.R.id.home:

                String title = getIntent().getStringExtra("title");
                String pdf_name = title + ".pdf";
                deletePdf(pdf_name);
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this,intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String title = getIntent().getStringExtra("title");

        String pdf_name = title + ".pdf";

        deletePdf(pdf_name);
    }


    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");
        if (getIntent().hasExtra("pdf")){

            Log.d(TAG, "getIncomingIntent: found intent extras" );
            String title = getIntent().getStringExtra("title");

            String pdf_name = title + ".pdf";
            String pub_id = getIntent().getStringExtra("id");
            initProgressbar();
            downloadPdf(pdf_name);
        }

    }


    private void initProgressbar(){
        progressBar = findViewById(R.id.progressbar);
        progressBar.getProgressDrawable();
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadPdf(final String filename){

        new AsyncTask<Void,Integer,Boolean>(){
            File file = getFileStreamPath(filename);
            @Override
            protected Boolean doInBackground(Void... voids) {
                return downloadPdf();
            }

            private Boolean downloadPdf() {
                try{
                   if(file.exists())
                        return true;
                    try{
                        FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        URL u = new URL(getIntent().getStringExtra("pdf"));
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();
                        InputStream inputStream = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentLength];
                        long total = 0;
                        int count;
                        while ((count=inputStream.read(data))!=-1){
                            total += count;
                            publishProgress((int)((total*100)/contentLength));
                            fileOutputStream.write(data,0,count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                        return true;
                    }catch (final Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    openPdf(filename);

                }else{
                    Toast.makeText(ReaderActivity.this, "unable to download PDF",Toast.LENGTH_SHORT).show();
                }
            }


        }.execute();

    }

    private void deletePdf(String filename){
        Log.d(TAG, "deletePdf: deleted");
        File file = getFileStreamPath(filename);

        file.delete();
    }

    private void openPdf(String filename){
        try {

            File file = getFileStreamPath(filename);
            Log.e("file","file: "+  file.getAbsolutePath());
            progressBar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);

            pdfView.fromFile(file)
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Log.d(TAG, "onInitiallyRendered: " + Integer.toString(pdfView.getCurrentPage()));

                        }
                    })
                    .onPageScroll(new OnPageScrollListener() {
                        @Override
                        public void onPageScrolled(int page, float positionOffset) {
//                            Log.d(TAG, "onPageScrolled: pagescrolled");
                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            openPub = (int) (new Date().getTime()/1000);

                            int duration = 0;
                            String email = userPreferences.getPrefEmail();

                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date tstamp = Calendar.getInstance().getTime();
                            String timestamp = df.format(tstamp);

                            UserPublikasiOpenModel openModel = new UserPublikasiOpenModel();
                            openModel.setId(pub_open_id);
                            openModel.setEmail(email);
                            openModel.setTimestamp(timestamp);

                            openModel.setDuration(duration);
                            openModel.setPub_id(getIntent().getStringExtra("id"));
                            openModel.setIs_uploaded(0);

                            addPublikasiOpen(openModel);
                        }
                    })

                    .enableDoubletap(true)
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {

                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date tstamp = Calendar.getInstance().getTime();
                            String timestamp = df.format(tstamp);
                            Log.i(TAG, "onTap: tap di " + (pdfView.getCurrentPage()+1));
                            UserAktivitasPerHalaman userAktivitasPerHalaman = new UserAktivitasPerHalaman();
                            userAktivitasPerHalaman.setPub_open_id(pub_open_id);
                            userAktivitasPerHalaman.setHalaman(pdfView.getCurrentPage()+1);
                            userAktivitasPerHalaman.setAktivitas("TAP");
                            userAktivitasPerHalaman.setDuration(0);
                            userAktivitasPerHalaman.setTimestamp(timestamp);
                            userAktivitasPerHalaman.setIs_uploaded(0);


                            addHalamanActivity(userAktivitasPerHalaman);
                            return false;
                        }
                    })
                    .enableAntialiasing(true)
                    .spacing(10)
                    .pageSnap(true)
                    .setOnDoubleTapListener(new OnDoubleTapListener() {
                        @Override
                        public void onDoubleTap(int page) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date tstamp = Calendar.getInstance().getTime();
                            String timestamp = df.format(tstamp);
                            Log.i(TAG, "onDoubleTap: Double tap di " + (pdfView.getCurrentPage()+1));
                            UserAktivitasPerHalaman userAktivitasPerHalaman = new UserAktivitasPerHalaman();
                            userAktivitasPerHalaman.setPub_open_id(pub_open_id);
                            userAktivitasPerHalaman.setHalaman(pdfView.getCurrentPage()+1);
                            userAktivitasPerHalaman.setAktivitas("DOUBLE TAP");
                            userAktivitasPerHalaman.setDuration(0);
                            userAktivitasPerHalaman.setTimestamp(timestamp);
                            userAktivitasPerHalaman.setIs_uploaded(0);


                            addHalamanActivity(userAktivitasPerHalaman);
                        }
                    })
                    .setOnZoomListener(new OnZoomListener() {
                        @Override
                        public void onZoom(int page) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            Date tstamp = Calendar.getInstance().getTime();
                            String timestamp = df.format(tstamp);
                            Log.i(TAG, "onZoom: zoom " + (pdfView.getCurrentPage()+1));
                            UserAktivitasPerHalaman userAktivitasPerHalaman = new UserAktivitasPerHalaman();
                            userAktivitasPerHalaman.setPub_open_id(pub_open_id);
                            userAktivitasPerHalaman.setHalaman(pdfView.getCurrentPage()+1);
                            userAktivitasPerHalaman.setAktivitas("ZOOM");
                            userAktivitasPerHalaman.setDuration(0);
                            userAktivitasPerHalaman.setTimestamp(timestamp);
                            userAktivitasPerHalaman.setIs_uploaded(0);


                            addHalamanActivity(userAktivitasPerHalaman);
                        }
                    })
                    .onPageChange(new OnPageChangeListener() {

                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            showToast("PAGE " + (page+1) + "/" + pdfView.getPageCount());
                            if (startHalamanPub!=0){
                                endHalamanPub = (int) (new Date().getTime()/1000);
                                if ((endHalamanPub-startHalamanPub)>=4){
                                    int halamanduration = endHalamanPub-startHalamanPub;

                                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    Date tstamp = Calendar.getInstance().getTime();
                                    String timestamp = df.format(tstamp);

                                    UserAktivitasPerHalaman userAktivitasPerHalaman = new UserAktivitasPerHalaman();
                                    userAktivitasPerHalaman.setPub_open_id((pub_open_id));
                                    userAktivitasPerHalaman.setHalaman(page);
                                    userAktivitasPerHalaman.setAktivitas("BACA");
                                    userAktivitasPerHalaman.setDuration(halamanduration);
                                    userAktivitasPerHalaman.setTimestamp(timestamp);
                                    userAktivitasPerHalaman.setIs_uploaded(0);

                                    addHalamanActivity(userAktivitasPerHalaman);

                                    Log.d("Event :", "halaman "+ page + " menetap selama " + halamanduration + " detik");

                                }
                                startHalamanPub = (int) (new Date().getTime()/1000);

                            }else{
                                startHalamanPub = (int) (new Date().getTime()/1000);
                            }
                        }

                    })
                    .load();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        pagetoast.cancel();
        endHalamanPub = (int) (new Date().getTime()/1000);
        if ((endHalamanPub-startHalamanPub)>=4){

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date tstamp = Calendar.getInstance().getTime();
            String timestamp = df.format(tstamp);

            int halamanduration = endHalamanPub-startHalamanPub;
            UserAktivitasPerHalaman userAktivitasPerHalaman = new UserAktivitasPerHalaman();
            userAktivitasPerHalaman.setPub_open_id(pub_open_id);
            userAktivitasPerHalaman.setHalaman(pdfView.getCurrentPage()+1);
            userAktivitasPerHalaman.setAktivitas("BACA");
            userAktivitasPerHalaman.setDuration(halamanduration);
            userAktivitasPerHalaman.setTimestamp(timestamp);
            userAktivitasPerHalaman.setIs_uploaded(0);

            addHalamanActivity(userAktivitasPerHalaman);

            Log.d("Event :", "halaman "+ pdfView.getCurrentPage() + " menetap selama " + halamanduration + " detik");
        }
        Log.d(TAG, "onStop: Mencatat aktivitas stop reader");
        closePub = (int) (new Date().getTime()/1000);
        int duration = closePub-openPub;
        updatePublikasiOpen(duration,pub_open_id);
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.addActivityToServer(this);

    }

    void showToast(String text)
    {
        if(pagetoast != null)
        {
            pagetoast.cancel();
        }
        pagetoast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        pagetoast.show();

    }

    public void addPublikasiOpen(final UserPublikasiOpenModel model){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        new Thread(new Runnable() {
            @Override
            public void run() {
                pubDatabase.userPublikasiOpenDaoAccess().insertOnlySingleOpenPublikasi(model);

                Log.i(TAG, "run: " + pub_open_id);
                Log.d(TAG, "addPublikasiOpen: berhasil ditambah ke sqlite ");
            }

        }).start();


    }

    public void updatePublikasiOpen(final int duration, final String id){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        new Thread(new Runnable() {
            @Override
            public void run() {
                pubDatabase.userPublikasiOpenDaoAccess().updatePublikasiOpenDuration(duration, id);
                Log.i(TAG, "run: " + gson.toJson(pubDatabase.userPublikasiOpenDaoAccess().selectAllOpen()));
                Log.d(TAG, "addPublikasiOpen: berhasil ditambah ke sqlite ");
            }

        }).start();


    }

    public void addHalamanActivity(final UserAktivitasPerHalaman model){
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        new Thread(new Runnable() {
            @Override
            public void run() {
                halaman_id = pubDatabase.userHalamanPerPublikasiDaoAccess().insertOnlySingleHalaman(model);
//                Log.i(TAG, "run: " + );
                Log.i(TAG, "run: " + gson.toJson(pubDatabase.userHalamanPerPublikasiDaoAccess().userAktivitasPerHalamans()));

//                for(UserAktivitasPerHalaman aktivitas : pubDatabase.userHalamanPerPublikasiDaoAccess().userAktivitasPerHalamans()) {
//                    Log.i(TAG, "run: data" + aktivitas.toString());
//                }
                Log.d(TAG, "addPublikasiOpen: berhasil ditambah ke sqlite " );
            }

        }).start();




    }






    //
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (getIntent().hasExtra("pdf")){
//            String title = getIntent().getStringExtra("title");
//
//            String pdf_name = title + ".pdf";
//            if(!unduhflag){
//                deletePdf(pdf_name);
//            }
//        }
//    }
}
