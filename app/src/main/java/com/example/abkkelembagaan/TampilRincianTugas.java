package com.example.abkkelembagaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TampilRincianTugas extends AppCompatActivity {
    Intent intent;
    TextInputEditText rincian_tugas;
    DatabaseReference reference;
    RincianTugas rincianTugas;
    final Context context = this;
    String uid_jabatan, uid_uraian, uid_rincian;
    String tgl_buat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_rincian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rincian_tugas = (TextInputEditText) findViewById(R.id.rincian_tugas);
        rincianTugas = new RincianTugas();
        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        uid_uraian = intent.getStringExtra("uid_uraian");
        uid_rincian = intent.getStringExtra("uid_rincian");
        tgl_buat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());

        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtRincian = "";
                txtRincian = dataSnapshot.getValue(RincianTugas.class).getNama_rincian_tugas();

                rincian_tugas.setText(txtRincian);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_uraian_tugas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSimpan:
                HashMap<String, Object> hasil = new HashMap<>();
                hasil.put("nama_rincian_tugas", rincian_tugas.getText().toString());
                hasil.put("tanggal_buat", tgl_buat);
                //uTugas.setNama_uraian_tugas(uraian_tugas.getText().toString());
                //uTugas.setTanggal_buat(tgl_buat);
                reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).updateChildren(hasil);
                Toast.makeText(getApplicationContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TampilRincianTugas.this, TampilSemuaRincianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                intent.putExtra("uid_rincian", uid_rincian);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
