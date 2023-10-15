package com.example.abkkelembagaan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abkkelembagaan.ABK.TambahDetailAbkRincian;
import com.example.abkkelembagaan.ABK.TampilDetailAbkRincian;
import com.example.abkkelembagaan.DetailRincianTugas;
import com.example.abkkelembagaan.EditRincianTugas;
import com.example.abkkelembagaan.Model.Jabatan;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TampilRincianTugas;
import com.example.abkkelembagaan.TampilSemuaJabatan;
import com.example.abkkelembagaan.TampilSemuaRincianTugas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

public class AbkRincianAdapter extends RecyclerView.Adapter<AbkRincianAdapter.ViewHolder> {
    private Context mContext;
    private List<RincianTugas> mRincian;
    private List<String> mJumlah;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

    public AbkRincianAdapter(Context mContext, List<RincianTugas> mRincian){
        this.mRincian= mRincian;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AbkRincianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_abk_rincian, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RincianTugas rincianTugas = mRincian.get(position);

        holder.expandableTextView.setText(rincianTugas.getNama_rincian_tugas());

        if(rincianTugas.getBeban_kerja() == null){
            holder.tambah_abk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TambahDetailAbkRincian.class);
                    intent.putExtra("uid_jabatan", rincianTugas.getId_nama_jabatan());
                    intent.putExtra("uid_uraian", rincianTugas.getId_uraian_tugas());
                    intent.putExtra("uid_rincian", rincianTugas.getId_rincian_tugas());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.tambah_abk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TampilDetailAbkRincian.class);
                    intent.putExtra("uid_jabatan", rincianTugas.getId_nama_jabatan());
                    intent.putExtra("uid_uraian", rincianTugas.getId_uraian_tugas());
                    intent.putExtra("uid_rincian", rincianTugas.getId_rincian_tugas());
                    mContext.startActivity(intent);
                }
            });
        }

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            holder.cv_cardview.setBackgroundResource(R.drawable.btn_rounded_light);
            //holder.rincian_tugas.setTextColor(Color.parseColor("#0c0c0c"));
        }else {
            holder.cv_cardview.setBackgroundResource(R.drawable.btn_rounded_black);
            //holder.rincian_tugas.setTextColor(Color.parseColor("#ebebeb"));
        }
    }


    @Override
    public int getItemCount() {
        return mRincian.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rincian_tugas;
        public CardView cv_cardview;
        public ImageView tambah_abk;
        public ExpandableTextView expandableTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rincian_tugas = (TextView) itemView.findViewById(R.id.rincian_tugas);
            cv_cardview = (CardView)itemView.findViewById(R.id.cv_rincian);
            tambah_abk = (ImageView) itemView.findViewById(R.id.tambah_abk);
            expandableTextView = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
        }
    }

    public void updateList(List<RincianTugas> newList){
        mRincian = new ArrayList<>();
        mRincian.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateListJumlahPegawai(List<String> newList){
        mJumlah = new ArrayList<>();
        mJumlah.addAll(newList);
        notifyDataSetChanged();
    }

    public double getTotal(List<RincianTugas> list){

        double total=0.0;
        for(int i=0;i<list.size();i++){
            total=total+Double.parseDouble(list.get(i).getPegawai_yang_dibutuhkan());
        }
        return total;
    }
}
