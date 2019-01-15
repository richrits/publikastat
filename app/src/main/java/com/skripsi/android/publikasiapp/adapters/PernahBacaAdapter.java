package com.skripsi.android.publikasiapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.activity.DetailPublikasiActivity;
import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.List;

public class PernahBacaAdapter extends RecyclerView.Adapter<PernahBacaAdapter.ViewHolder>{
    private Context context;
    private List<Publikasi> pernahbacapublikasi;

    private static final String TAG = "ListPublikasiAdapter";


    public PernahBacaAdapter(Context context, List<Publikasi> pernahbacapublikasi) {
        Log.d(TAG, "SearchPublikasiAdapter: started");
        this.context = context;
        this.pernahbacapublikasi = pernahbacapublikasi;
    }

    @NonNull
    @Override
    public PernahBacaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_publikasi_item,parent,false);

        return new PernahBacaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PernahBacaAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");
        holder.title.setText(pernahbacapublikasi.get(position).getTitle());
        Glide.with(context).load(pernahbacapublikasi.get(position).getCover()).into(holder.cover);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item publikasi");
                Intent intent = new Intent(context, DetailPublikasiActivity.class);
                intent.putExtra("id", pernahbacapublikasi.get(position).getPub_id());
                intent.putExtra("title", pernahbacapublikasi.get(position).getTitle());
                intent.putExtra("cover", pernahbacapublikasi.get(position).getCover());
                intent.putExtra("no_katalog", pernahbacapublikasi.get(position).getKat_no());
                intent.putExtra("no_publikasi", pernahbacapublikasi.get(position).getPub_no());
                intent.putExtra("issn", pernahbacapublikasi.get(position).getIssn());
                intent.putExtra("tanggal_rilis", pernahbacapublikasi.get(position).getRl_date());
                intent.putExtra("ukuran_file", pernahbacapublikasi.get(position).getSize());
                intent.putExtra("pdf", pernahbacapublikasi.get(position).getPdf());
                intent.putExtra("_abstract", pernahbacapublikasi.get(position).get_abstract());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pernahbacapublikasi.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView cover;
        public LinearLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
            title = (TextView) itemView.findViewById(R.id.title);
            cover = (ImageView) itemView.findViewById(R.id.cover);
        }
    }
}
