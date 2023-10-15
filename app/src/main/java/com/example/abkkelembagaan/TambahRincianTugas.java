package com.example.abkkelembagaan;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TambahRincianTugas extends AppCompatActivity {

    TextInputEditText etRinciantugas;
    Button btTambah;
    Toolbar toolbar;
    RincianTugas rTugas;
    DatabaseReference reference;
    Intent intent;
    String tgl_buat, uid_jabatan, uid_uraian;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_rincian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        etRinciantugas = (TextInputEditText)findViewById(R.id.rinciantugas);
        btTambah = (Button) findViewById(R.id.btTambah);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahRincianTugas.this, TampilSemuaRincianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                context.startActivity(intent);
                finish();
            }
        });

        tgl_buat = new SimpleDateFormat("dd/MM/yyyy  - HH:mm", Locale.getDefault()).format(new Date());
        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        rTugas = new RincianTugas();
        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        uid_uraian = intent.getStringExtra("uid_uraian");

        btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtRincianTugas = etRinciantugas.getText().toString();
                rTugas.setNama_rincian_tugas(txtRincianTugas);

                if(TextUtils.isEmpty(txtRincianTugas)){
                    Toast.makeText(getApplicationContext(), "Rincian Tugas Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else {
                    reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").push().setValue(rTugas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Data Rincian Tugas berhasil ditambah!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(TambahRincianTugas.this, TampilSemuaRincianTugas.class);
                    intent.putExtra("uid_jabatan", uid_jabatan);
                    intent.putExtra("uid_uraian", uid_uraian);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
