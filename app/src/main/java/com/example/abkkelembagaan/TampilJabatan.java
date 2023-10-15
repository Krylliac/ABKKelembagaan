package com.example.abkkelembagaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.abkkelembagaan.Model.Jabatan;
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

public class TampilJabatan extends AppCompatActivity {
    Intent intent;
    TextInputEditText nama_jabatan, unit_kerja, ikhtisar_jabatan;
    DatabaseReference reference;
    Jabatan jabatan;
    final Context context = this;
    String uid_jabatan;
    String tgl_buat;
    String jam_buat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_jabatan);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nama_jabatan = (TextInputEditText) findViewById(R.id.nama_jabatan);
        unit_kerja = (TextInputEditText) findViewById(R.id.unit_kerja);

        jabatan = new Jabatan();
        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        tgl_buat = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(new Date());

        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        reference.child(uid_jabatan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtJabatan;
                String txtUnit;

                if(dataSnapshot.exists()){
                    txtJabatan = dataSnapshot.getValue(Jabatan.class).getNama_jabatan();
                    txtUnit = dataSnapshot.getValue(Jabatan.class).getUnit_kerja();

                    nama_jabatan.setText(txtJabatan);
                    unit_kerja.setText(txtUnit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_jabatan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.simpan_jabatan:
                HashMap<String, Object> hasil = new HashMap<>();
                hasil.put("nama_jabatan", nama_jabatan.getText().toString());
                hasil.put("unit_kerja", unit_kerja.getText().toString());
                hasil.put("tanggal_buat", tgl_buat);

                //jabatan.setNama_jabatan(nama_jabatan.getText().toString());
                //jabatan.setUnit_kerja(unit_kerja.getText().toString());
                //jabatan.setTanggal_buat(tgl_buat);
                reference.child(uid_jabatan).updateChildren(hasil);
                Toast.makeText(getApplicationContext(), "Data Berhasil diupdate", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TampilJabatan.this, TampilSemuaJabatan.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return  true;
            default:
                return  super.onOptionsItemSelected(item);

        }
    }
}
