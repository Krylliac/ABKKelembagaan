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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abkkelembagaan.Model.Jabatan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TambahJabatan extends AppCompatActivity {

    TextInputEditText namaJabatan, unitKerja;
    Button tambah_jabatan;
    Jabatan uJabatan;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jabatan);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahJabatan.this, TampilSemuaJabatan.class);
                startActivity(intent);
                finish();
            }
        });


        namaJabatan = (TextInputEditText) findViewById(R.id.namaJabatan);
        unitKerja = (TextInputEditText) findViewById(R.id.unitKerja);
        tambah_jabatan = (Button)findViewById(R.id.tambah_jabatan);
        uJabatan = new Jabatan();

        reference = FirebaseDatabase.getInstance().getReference("Jabatan");

        final String tgl_buat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());

        tambah_jabatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txtNamaJabatan = namaJabatan.getText().toString();
                String txtUnitKerja = unitKerja.getText().toString();
                if (TextUtils.isEmpty(txtNamaJabatan)){
                    Toast.makeText(getApplicationContext(), "Nama Jabatan Tidak Boleh Kosong harus diisi!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txtUnitKerja)){
                    Toast.makeText(getApplicationContext(), "Unit Kerja Tidak Boleh Kosong harus diisi!",Toast.LENGTH_SHORT).show();
                } else {
                    uJabatan.setNama_jabatan(txtNamaJabatan);
                    uJabatan.setUnit_kerja(txtUnitKerja);
                    uJabatan.setTanggal_buat(tgl_buat);
                    reference.push().setValue(uJabatan).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Tambah Data "+txtNamaJabatan+" Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(TambahJabatan.this, TampilSemuaJabatan.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
