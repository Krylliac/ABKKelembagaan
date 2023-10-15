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

import com.example.abkkelembagaan.Model.UraianTugas;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TampilSemuaJabatan;
import com.example.abkkelembagaan.TampilSemuaUraianJabatan;
import com.example.abkkelembagaan.TampilSemuaUraianTugas;
import com.example.abkkelembagaan.TampilUraianTugas;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UraianAdapter extends RecyclerView.Adapter<UraianAdapter.ViewHolder> {
    private Context mContext;
    private List<UraianTugas> mTugas;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

    public UraianAdapter(Context mContext, List<UraianTugas> mTugas){
        this.mTugas = mTugas;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UraianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_uraian_tugas, parent, false);

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
        holder.edit_uraian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TampilUraianTugas.class);
                intent.putExtra("uid_jabatan", uraianTugas.getId_jabatan());
                intent.putExtra("uid_uraian", uraianTugas.getUraian_id());
                mContext.startActivity(intent);
            }
        });
        holder.hapus_uraian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("");
                alert
                        .setMessage("Yakin Hapus Data ")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference.child(uraianTugas.getId_jabatan()).child("UraianTugas").child(uraianTugas.getUraian_id()).removeValue();
                                Intent intent = new Intent(mContext, TampilSemuaUraianTugas.class);
                                intent.putExtra("uid_jabatan", uraianTugas.getId_jabatan());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

                Button btPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btPositive.setTextColor(mContext.getResources().getColor(R.color.colorTextAlert));
                btNegative.setTextColor(mContext.getResources().getColor(R.color.colorTextAlert));
            }
        });

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            holder.cv_uraian.setBackgroundResource(R.drawable.btn_rounded_light);
            holder.uraian_tugas.setTextColor(Color.parseColor("#0c0c0c"));
        }else {
            holder.cv_uraian.setBackgroundResource(R.drawable.btn_rounded_black);
            holder.uraian_tugas.setTextColor(Color.parseColor("#ebebeb"));
        }
    }

    @Override
    public int getItemCount() {
        return mTugas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView uraian_tugas;
        public CardView cv_uraian;
        public ImageView hapus_uraian, edit_uraian;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            uraian_tugas = (TextView) itemView.findViewById(R.id.uraian_tugas);
            cv_uraian = (CardView)itemView.findViewById(R.id.cv_uraian_tugas);
            hapus_uraian = (ImageView) itemView.findViewById(R.id.hapus_uraian);
            edit_uraian = (ImageView) itemView.findViewById(R.id.edit_uraian);

        }
    }

    public void updateListUraian(List<UraianTugas> listUraian){
        mTugas = new ArrayList<>();
        mTugas.addAll(listUraian);
        notifyDataSetChanged();

    }

}