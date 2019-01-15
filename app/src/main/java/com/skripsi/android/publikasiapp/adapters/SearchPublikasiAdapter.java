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

public class SearchPublikasiAdapter extends RecyclerView.Adapter<SearchPublikasiAdapter.ViewHolder>{
    private Context context;
    private List<Publikasi> publikasisearchresult;
    private static final String TAG = "ListPublikasiAdapter";


    public SearchPublikasiAdapter(Context context, List<Publikasi> publikasisearchresult) {
        Log.d(TAG, "SearchPublikasiAdapter: started");
        this.context = context;
        this.publikasisearchresult = publikasisearchresult;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_publikasi_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");
        holder.title.setText(publikasisearchresult.get(position).getTitle());
        Glide.with(context).load(publikasisearchresult.get(position).getCover()).into(holder.cover);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: item publikasi");
                Intent intent = new Intent(context, DetailPublikasiActivity.class);
                intent.putExtra("id", publikasisearchresult.get(position).getPub_id());
                intent.putExtra("title", publikasisearchresult.get(position).getTitle());
                intent.putExtra("cover", publikasisearchresult.get(position).getCover());
                intent.putExtra("no_katalog", publikasisearchresult.get(position).getKat_no());
                intent.putExtra("no_publikasi", publikasisearchresult.get(position).getPub_no());
                intent.putExtra("issn", publikasisearchresult.get(position).getIssn());
                intent.putExtra("tanggal_rilis", publikasisearchresult.get(position).getRl_date());
                intent.putExtra("ukuran_file", publikasisearchresult.get(position).getSize());
                intent.putExtra("pdf", publikasisearchresult.get(position).getPdf());
                intent.putExtra("_abstract", publikasisearchresult.get(position).get_abstract());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return publikasisearchresult.size();
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
