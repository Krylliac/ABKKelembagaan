package com.example.abkkelembagaan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.example.abkkelembagaan.R;

import java.util.ArrayList;
import java.util.List;

public class EksporAdapter extends RecyclerView.Adapter<EksporAdapter.ViewHolder> {
    private Context mContext;
    private List<UraianTugas> mUraian;
    private List<String> mJumlah;

    public EksporAdapter(Context mContext, List<UraianTugas> mUraian){
        this.mUraian= mUraian;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EksporAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_export_uraian, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UraianTugas uraianTugas = mUraian.get(position);

        holder.rincian_tugas.setText(uraianTugas.getNama_uraian_tugas());

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            holder.cv_uraian_tugas.setBackgroundResource(R.drawable.btn_rounded_light);
            holder.rincian_tugas.setTextColor(Color.parseColor("#0c0c0c"));
        }else {
            holder.cv_uraian_tugas.setBackgroundResource(R.drawable.btn_rounded_black);
            holder.rincian_tugas.setTextColor(Color.parseColor("#ebebeb"));
        }
    }


    @Override
    public int getItemCount() {
        return mUraian.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rincian_tugas;
        public CardView cv_uraian_tugas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rincian_tugas = (TextView) itemView.findViewById(R.id.nama_uraian);
            cv_uraian_tugas = (CardView)itemView.findViewById(R.id.cv_uraian_tugas);
        }
    }

    public void updateList(List<UraianTugas> newList){
        mUraian = new ArrayList<>();
        mUraian.addAll(newList);
        notifyDataSetChanged();
    }
}
