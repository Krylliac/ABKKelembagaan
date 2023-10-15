package com.example.abkkelembagaan.ABK;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.Satuan;
import com.example.abkkelembagaan.Model.SifatPekerjaan;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TambahRincianTugas;
import com.example.abkkelembagaan.TampilSemuaRincianTugas;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TambahDetailAbkRincian extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextInputEditText bebanKerja, waktuPenyelesaian;
    Spinner spinner_sifat_pekerjaan, spinner_satuan;
    Intent intent;
    DatabaseReference reference, referenceSifatPekerjaan, referenceSatuan;
    String uid_jabatan, uid_uraian, uid_rincian;
    SifatPekerjaan sifatPekerjaan;
    Satuan satuan;
    ValueEventListener listener, listenerSatuan;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterSatuan;
    ArrayList<String> spinnerDataListSifat, spinnerDataListSatuan;
    androidx.appcompat.widget.Toolbar toolbar;
    RincianTugas rTugas;
    Button btTambah;
    HashMap<String, Object> hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_detail_abk_rincian);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahDetailAbkRincian.this, TampilSemuaAbkRincian.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                intent.putExtra("uid_rincian", uid_rincian);
                startActivity(intent);
                finish();
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        bebanKerja = (TextInputEditText) findViewById(R.id.beban_kerja);
        waktuPenyelesaian = (TextInputEditText) findViewById(R.id.waktu_penyelesaian);
        spinner_sifat_pekerjaan = (Spinner) findViewById(R.id.spinner_sifat_pekerjaan);
        spinner_satuan = (Spinner) findViewById(R.id.spinner_satuan);
        btTambah = (Button) findViewById(R.id.btTambah);

        hasil = new HashMap<>();
        rTugas = new RincianTugas();
        sifatPekerjaan = new SifatPekerjaan();
        satuan = new Satuan();
        spinnerDataListSifat = new ArrayList<>();
        spinnerDataListSatuan = new ArrayList<>();
        adapter = new ArrayAdapter<String>(TambahDetailAbkRincian.this, R.layout.support_simple_spinner_dropdown_item, spinnerDataListSifat);
        adapterSatuan = new ArrayAdapter<String>(TambahDetailAbkRincian.this, R.layout.support_simple_spinner_dropdown_item, spinnerDataListSatuan);
        spinner_sifat_pekerjaan.setAdapter(adapter);
        spinner_satuan.setAdapter(adapterSatuan);

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        uid_uraian = intent.getStringExtra("uid_uraian");
        uid_rincian = intent.getStringExtra("uid_rincian");
        reference = FirebaseDatabase.getInstance().getReference("Jabatan");
        referenceSifatPekerjaan = FirebaseDatabase.getInstance().getReference("SifatPekerjaan");
        referenceSatuan = FirebaseDatabase.getInstance().getReference("Satuan");

        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtTitle = "";
                txtTitle = dataSnapshot.getValue(RincianTugas.class).getNama_rincian_tugas();

                collapsingToolbarLayout.setTitle(txtTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        retrieve_data_sifat_pekerjaan();
        retrieve_data_satuan();

        btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtSifatPekerjaan = spinner_sifat_pekerjaan.getSelectedItem().toString();
                String txtBebanKerja = bebanKerja.getText().toString();
                String txtWaktuPenyelesaian = waktuPenyelesaian.getText().toString();
                String txtSatuan = spinner_satuan.getSelectedItem().toString();
                String tempTahunan = "Tahunan";
                String tempBulanan = "Bulanan";
                String tempMingguan = "Mingguan";
                String tempHarian = "Harian";
                String tempSatuanHari = "Hari";
                String tempSatuanJam = "Jam";
                Float pegawai_yang_dibutuhkan, perhitungan_hasil;

                if (TextUtils.isEmpty(txtBebanKerja) && TextUtils.isEmpty(txtSifatPekerjaan)
                    && TextUtils.isEmpty(txtWaktuPenyelesaian) && TextUtils.isEmpty(txtSatuan)){
                    Toast.makeText(getApplicationContext(), "Semua Field Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(txtBebanKerja)){
                    Toast.makeText(getApplicationContext(), "Jumlah/Beban Tugas Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(txtSifatPekerjaan)) {
                    Toast.makeText(getApplicationContext(), "Sifat Pekerjaan Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(txtWaktuPenyelesaian)){
                    Toast.makeText(getApplicationContext(), "Waktu Penyelesaian Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(txtSatuan)) {
                    Toast.makeText(getApplicationContext(), "Satuan Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else {
                    if(txtSifatPekerjaan.equals(tempTahunan) && txtSatuan.equals(tempSatuanHari)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(5.5));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(1250));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 5.5);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 1250;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    }else if(txtSifatPekerjaan.equals(tempBulanan) && txtSatuan.equals(tempSatuanHari)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(5.5));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(105));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 5.5);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 105;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    } else if(txtSifatPekerjaan.equals(tempMingguan) && txtSatuan.equals(tempSatuanHari)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(5.5));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(26.5));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 5.5);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 26.5;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    } else if(txtSifatPekerjaan.equals(tempHarian) && txtSatuan.equals(tempSatuanHari)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(5.5));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(5.5));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 5.5);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 5.5;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    }else if(txtSifatPekerjaan.equals(tempTahunan) && txtSatuan.equals(tempSatuanJam)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(1));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(1250));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 1);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 1250;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    }else if(txtSifatPekerjaan.equals(tempBulanan) && txtSatuan.equals(tempSatuanJam)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(1));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(105));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 1);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 105;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    }else if(txtSifatPekerjaan.equals(tempMingguan) && txtSatuan.equals(tempSatuanJam)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(1));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(26.5));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 1);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 26.5;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    }else if(txtSifatPekerjaan.equals(tempHarian) && txtSatuan.equals(tempSatuanJam)){
                        hasil.put("sifat_pekerjaan", txtSifatPekerjaan);
                        hasil.put("beban_kerja", txtBebanKerja);
                        hasil.put("waktu_penyelesaian", txtWaktuPenyelesaian);
                        hasil.put("nama_satuan", txtSatuan);
                        hasil.put("satuan", String.valueOf(1));
                        hasil.put("value_sifat_pekerjaan", String.valueOf(5.5));

                        perhitungan_hasil = Float.valueOf(txtBebanKerja) * (Float.valueOf(txtWaktuPenyelesaian) * (float) 1);
                        pegawai_yang_dibutuhkan = perhitungan_hasil / (float) 5.5;

                        hasil.put("perhitungan_hasil", String.valueOf(perhitungan_hasil));
                        hasil.put("pegawai_yang_dibutuhkan", String.valueOf(pegawai_yang_dibutuhkan));
                    } else {
                        Toast.makeText(getApplicationContext(), "Tambah Data Rincian ABK Gagal!", Toast.LENGTH_SHORT).show();
                    }

                    reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).updateChildren(hasil).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Tambah Data Rincian ABK Berhasil!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(TambahDetailAbkRincian.this, TampilSemuaAbkRincian.class);
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

    public void retrieve_data_sifat_pekerjaan(){
        listener = referenceSifatPekerjaan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerDataListSifat.add("- Pilih Sifat Pekerjaan -");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    spinnerDataListSifat.add(snapshot.getValue(SifatPekerjaan.class).getNama_sifat_pekerjaan());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieve_data_satuan(){
        listenerSatuan = referenceSatuan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerDataListSatuan.add("- Pilih Satuan Waktu Penyelesaian-");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    spinnerDataListSatuan.add(snapshot.getValue(Satuan.class).getNama_satuan());
                }
                adapterSatuan.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
