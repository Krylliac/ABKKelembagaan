package com.example.abkkelembagaan.ABK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TampilDetailAbkRincian extends AppCompatActivity {
    TextView rincian_tugas, sifat_pekerjaan,beban_kerja,
            waktu_penyelesaian, satuan, pegawai_dibutuhkan;
    Intent intent;
    DatabaseReference reference;
    Toolbar toolbar;
    RincianTugas rTugas;
    Context context = this;
    String uid_jabatan, uid_uraian, uid_rincian;
    HashMap<String, Object> hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_detail_abk_rincian);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        uid_uraian = intent.getStringExtra("uid_uraian");
        uid_rincian = intent.getStringExtra("uid_rincian");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TampilDetailAbkRincian.this, TampilSemuaAbkRincian.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                intent.putExtra("uid_rincian", uid_rincian);
                startActivity(intent);
                finish();
            }
        });

        hasil = new HashMap<>();
        rincian_tugas = (TextView) findViewById(R.id.rincian_tugas);
        sifat_pekerjaan = (TextView) findViewById(R.id.sifat_pekerjaan);
        beban_kerja = (TextView) findViewById(R.id.beban_kerja);
        waktu_penyelesaian = (TextView) findViewById(R.id.waktu_penyelesaian);
        satuan = (TextView) findViewById(R.id.satuan);
        pegawai_dibutuhkan = (TextView) findViewById(R.id.pegawai_dibutuhkan);

        rTugas = new RincianTugas();
        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtRincian = "";
                String txtSifatPekerjaan = "";
                String txtBebanKerja = "";
                String txtWaktuPenyelesaian = "";
                String txtSatuan = "";
                String txtPegawaiDibutuhkan = "";
                Double tempPegawaiDibutuhkan;

                txtRincian = dataSnapshot.getValue(RincianTugas.class).getNama_rincian_tugas();
                txtSifatPekerjaan = dataSnapshot.getValue(RincianTugas.class).getSifat_pekerjaan();
                txtBebanKerja = dataSnapshot.getValue(RincianTugas.class).getBeban_kerja();
                txtWaktuPenyelesaian = dataSnapshot.getValue(RincianTugas.class).getWaktu_penyelesaian();
                txtSatuan = dataSnapshot.getValue(RincianTugas.class).getNama_satuan();
                txtPegawaiDibutuhkan = dataSnapshot.getValue(RincianTugas.class).getPegawai_yang_dibutuhkan();
                tempPegawaiDibutuhkan = Double.valueOf(txtPegawaiDibutuhkan);

                rincian_tugas.setText(txtRincian);
                sifat_pekerjaan.setText(txtSifatPekerjaan);
                beban_kerja.setText(txtBebanKerja);
                waktu_penyelesaian.setText(txtWaktuPenyelesaian);
                satuan.setText(txtSatuan);
                pegawai_dibutuhkan.setText(String.format("%.3f", tempPegawaiDibutuhkan));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_rincian_tugas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuEdit:
                Intent intent = new Intent(TampilDetailAbkRincian.this, EditDetailAbkRincian.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                intent.putExtra("uid_rincian", uid_rincian);
                startActivity(intent);
                startActivity(intent);
                finish();
                return  true;
            case  R.id.menuHapus:
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("");
                alert
                        .setMessage("Yakin Hapus Data ")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hasil.put("sifat_pekerjaan", null);
                                hasil.put("beban_kerja", null);
                                hasil.put("waktu_penyelesaian", null);
                                hasil.put("nama_satuan", null);
                                hasil.put("satuan", null);
                                hasil.put("value_sifat_pekerjaan", null);
                                hasil.put("perhitungan_hasil", null);
                                hasil.put("pegawai_yang_dibutuhkan", null);

                                reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).updateChildren(hasil);
                                Intent intent = new Intent(TampilDetailAbkRincian.this, TampilSemuaAbkRincian.class);
                                intent.putExtra("uid_jabatan", uid_jabatan);
                                intent.putExtra("uid_uraian", uid_uraian);
                                intent.putExtra("uid_rincian", uid_rincian);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
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

                Button btPostive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                Button btNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btPostive.setTextColor(context.getResources().getColor(R.color.colorTextAlert));
                btNegative.setTextColor(context.getResources().getColor(R.color.colorTextAlert));

                return true;
            default:
                return  super.onOptionsItemSelected(item);

        }
    }
}
