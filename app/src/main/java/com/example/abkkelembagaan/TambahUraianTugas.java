package com.example.abkkelembagaan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.abkkelembagaan.Model.UraianTugas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TambahUraianTugas extends AppCompatActivity {
    TextInputEditText uraian_tugas;
    Button btn_tambah;
    UraianTugas utugas;
    Intent intent;
    String uid_jabatan;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_uraian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryUraian));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahUraianTugas.this, TampilSemuaUraianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                startActivity(intent);
                finish();
            }
        });

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");

        uraian_tugas = (TextInputEditText) findViewById(R.id.uraiantugas);
        btn_tambah = (Button) findViewById(R.id.tambah_uraian_tugas);
        utugas = new UraianTugas();
        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        final String tgl_buat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txtUraiantugas = uraian_tugas.getText().toString();

                if(TextUtils.isEmpty(txtUraiantugas)){
                    Toast.makeText(getApplicationContext(), "Uraian Tugas Tidak Boleh Kosong harus diisi!", Toast.LENGTH_SHORT).show();
                }else {
                    utugas.setNama_uraian_tugas(txtUraiantugas);
                    utugas.setTanggal_buat(tgl_buat);
                    reference.child(uid_jabatan).child("UraianTugas").push().setValue(utugas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Tambah Data "+txtUraiantugas+" Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(TambahUraianTugas.this, TampilSemuaUraianTugas.class);
                    intent.putExtra("uid_jabatan", uid_jabatan);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
