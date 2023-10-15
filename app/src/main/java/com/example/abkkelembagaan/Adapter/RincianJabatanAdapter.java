package com.example.abkkelembagaan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abkkelembagaan.Model.Jabatan;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TampilJabatan;
import com.example.abkkelembagaan.TampilSemuaJabatan;
import com.example.abkkelembagaan.TampilSemuaRincianUraianTugas;
import com.example.abkkelembagaan.TampilSemuaUraianTugas;
import com.example.abkkelembagaan.TampilUraianTugas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RincianJabatanAdapter extends RecyclerView.Adapter<RincianJabatanAdapter.ViewHolder> {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");
    private Context mContext;
    private List<Jabatan> mJabatan;

    public RincianJabatanAdapter(Context mContext, List<Jabatan> mJabatan){
        this.mJabatan = mJabatan;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RincianJabatanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_rincian_jataban, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Jabatan jabatan = mJabatan.get(position);

        //holder.itemView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(mContext, TampilJabatan.class);
        //        intent.putExtra("uid_jabatan", jabatan.getId_jabatan());
        //        mContext.startActivity(intent);
        //    }
        //});


        holder.tambah_uraian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TampilSemuaRincianUraianTugas.class);
                intent.putExtra("uid_jabatan", jabatan.getId_jabatan());
                mContext.startActivity(intent);
            }
        });

        holder.nama_jabatan.setText(jabatan.getNama_jabatan());

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            holder.cv_cardview.setBackgroundResource(R.drawable.btn_rounded_light);
            holder.nama_jabatan.setTextColor(Color.parseColor("#0c0c0c"));
        }else {
            holder.cv_cardview.setBackgroundResource(R.drawable.btn_rounded_black);
            holder.nama_jabatan.setTextColor(Color.parseColor("#ebebeb"));
        }

    }

    @Override
    public int getItemCount() {
        return mJabatan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nama_jabatan;
        public ImageView tambah_uraian;
        public CardView cv_cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_jabatan = (TextView) itemView.findViewById(R.id.nama_jabatan);
            tambah_uraian = (ImageView) itemView.findViewById(R.id.tambah_uraian);
            cv_cardview = (CardView) itemView.findViewById(R.id.cv_jabatan);
        }
    }

    public void updateList(List<Jabatan> newList){
        mJabatan = new ArrayList<>();
        mJabatan.addAll(newList);
        notifyDataSetChanged();
    }

}