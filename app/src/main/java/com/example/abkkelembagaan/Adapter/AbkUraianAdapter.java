package com.example.abkkelembagaan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abkkelembagaan.ABK.TampilSemuaAbkRincian;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TampilSemuaJabatan;
import com.example.abkkelembagaan.TampilSemuaRincianTugas;
import com.example.abkkelembagaan.TampilSemuaUraianJabatan;
import com.example.abkkelembagaan.TampilSemuaUraianTugas;
import com.example.abkkelembagaan.TampilUraianTugas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AbkUraianAdapter extends RecyclerView.Adapter<AbkUraianAdapter.ViewHolder> {
    private Context mContext;
    private List<UraianTugas> mTugas;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

    public AbkUraianAdapter(Context mContext, List<UraianTugas> mTugas){
        this.mTugas = mTugas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AbkUraianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_rincian_uraian, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UraianTugas uraianTugas = mTugas.get(position);

        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(mContext, TampilUraianTugas.class);
        //        intent.putExtra("uid", uraianTugas.getUraian_id());
        //        mContext.startActivity(intent);
        //    }
        //});

        holder.uraian_tugas.setText(uraianTugas.getNama_uraian_tugas());
        holder.tambah_rincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TampilSemuaAbkRincian.class);
                intent.putExtra("uid_jabatan", uraianTugas.getId_jabatan());
                intent.putExtra("uid_uraian", uraianTugas.getUraian_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
            }
        });

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            holder.cv_uraian.setBackgroundResource(R.drawable.btn_rounded_light);
            holder.uraian_tugas.setTextColor(Color.parseColor("#0c0c0c"));
            holder.tambah_rincian.setColorFilter(Color.parseColor("#0c0c0c"));
        }else {
            holder.cv_uraian.setBackgroundResource(R.drawable.btn_rounded_black);
            holder.uraian_tugas.setTextColor(Color.parseColor("#ebebeb"));
            holder.tambah_rincian.setColorFilter(Color.parseColor("#ebebeb"));
        }
    }

    @Override
    public int getItemCount() {
        return mTugas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView uraian_tugas;
        public CardView cv_uraian;
        public ImageView tambah_rincian;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            uraian_tugas = (TextView) itemView.findViewById(R.id.nama_uraian);
            cv_uraian = (CardView)itemView.findViewById(R.id.cv_uraian_tugas);
            tambah_rincian = (ImageView) itemView.findViewById(R.id.tambah_rincian);

        }
    }

    public void updateListUraian(List<UraianTugas> listUraian){
        mTugas = new ArrayList<>();
        mTugas.addAll(listUraian);
        notifyDataSetChanged();

    }


}