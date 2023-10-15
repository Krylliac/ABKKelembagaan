package com.example.abkkelembagaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abkkelembagaan.Model.Jabatan;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.SifatPekerjaan;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditRincianTugas extends AppCompatActivity {
    RincianTugas rTugas;
    DatabaseReference reference;
    String tgl_buat;
    Button btSimpan;
    TextInputEditText rincian_tugas;
    Intent intent;
    String uid_jabatan, uid_uraian, uid_rincian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rincian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rincian_tugas = (TextInputEditText) findViewById(R.id.rinciantugas);
        btSimpan = (Button) findViewById(R.id.btSimpan);

        tgl_buat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());
        rTugas = new RincianTugas();

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_rincian");
        uid_uraian = intent.getStringExtra("uid_uraian");
        uid_rincian = intent.getStringExtra("uid_rincian");
        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtRincian = "";

                txtRincian = dataSnapshot.getValue(RincianTugas.class).getNama_rincian_tugas();

                rincian_tugas.setText(txtRincian);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data yang akan diupdate", Toast.LENGTH_SHORT).show();
            }
        });



        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtRincianTugas = rincian_tugas.getText().toString();

                if(TextUtils.isEmpty(txtRincianTugas)){
                    Toast.makeText(getApplicationContext(), "Semua Field Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("nama_rincian_tugas", txtRincianTugas);
                    reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Data Rincian Tugas berhasil diupdate!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(EditRincianTugas.this, TampilSemuaRincianTugas.class);
                    intent.putExtra("uid_jabatan", uid_jabatan);
                    intent.putExtra("uid_uraian", uid_uraian);
                    intent.putExtra("uid_rincian", uid_rincian);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
