package com.example.abkkelembagaan.ABK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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

public class EditDetailAbkRincian extends AppCompatActivity {
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
    HashMap<String, Object> hasil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_abk_rincian);

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
                Intent intent = new Intent(EditDetailAbkRincian.this, TampilSemuaAbkRincian.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                intent.putExtra("uid_rincian", uid_rincian);
                startActivity(intent);
                finish();
            }
        });

        bebanKerja = (TextInputEditText) findViewById(R.id.beban_kerja);
        waktuPenyelesaian = (TextInputEditText) findViewById(R.id.waktu_penyelesaian);
        spinner_sifat_pekerjaan = (Spinner) findViewById(R.id.spinner_sifat_pekerjaan);
        spinner_satuan = (Spinner) findViewById(R.id.spinner_satuan);

        hasil = new HashMap<>();
        rTugas = new RincianTugas();
        sifatPekerjaan = new SifatPekerjaan();
        satuan = new Satuan();
        spinnerDataListSifat = new ArrayList<>();
        spinnerDataListSatuan = new ArrayList<>();
        adapter = new ArrayAdapter<String>(EditDetailAbkRincian.this, R.layout.support_simple_spinner_dropdown_item, spinnerDataListSifat);
        adapterSatuan = new ArrayAdapter<String>(EditDetailAbkRincian.this, R.layout.support_simple_spinner_dropdown_item, spinnerDataListSatuan);
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
                String txtBebanKerja = dataSnapshot.getValue(RincianTugas.class).getBeban_kerja();
                String txtWaktuPenyelesaian = dataSnapshot.getValue(RincianTugas.class).getWaktu_penyelesaian();

                bebanKerja.setText(txtBebanKerja);
                waktuPenyelesaian.setText(txtWaktuPenyelesaian);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        retrieve_data_sifat_pekerjaan();
        retrieve_data_satuan();
    }

    public void retrieve_data_sifat_pekerjaan(){
        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtSifatPekerjaan = dataSnapshot.getValue(RincianTugas.class).getSifat_pekerjaan();
                spinnerDataListSifat.add(txtSifatPekerjaan);
                if(txtSifatPekerjaan.equals("Tahunan")){
                    spinnerDataListSifat.add("Bulanan");
                    spinnerDataListSifat.add("Mingguan");
                    spinnerDataListSifat.add("Harian");
                } else if(txtSifatPekerjaan.equals("Bulanan")){
                    spinnerDataListSifat.add("Tahunan");
                    spinnerDataListSifat.add("Mingguan");
                    spinnerDataListSifat.add("Harian");
                } else if(txtSifatPekerjaan.equals("Mingguan")){
                    spinnerDataListSifat.add("Tahunan");
                    spinnerDataListSifat.add("Bulanan");
                    spinnerDataListSifat.add("Harian");
                } else {
                    spinnerDataListSifat.add("Tahunan");
                    spinnerDataListSifat.add("Bulanan");
                    spinnerDataListSifat.add("Mingguan");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void retrieve_data_satuan(){
        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtSatuan = dataSnapshot.getValue(RincianTugas.class).getNama_satuan();
                spinnerDataListSatuan.add(txtSatuan);
                if(txtSatuan.equals("Hari")){
                    spinnerDataListSatuan.add("Jam");
                } else {
                    spinnerDataListSatuan.add("Hari");
                }
                adapterSatuan.notifyDataSetChanged();
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
        switch (item.getItemId()) {
            case R.id.menuSimpan:
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
                        && TextUtils.isEmpty(txtWaktuPenyelesaian) && TextUtils.isEmpty(txtSatuan)) {
                    Toast.makeText(getApplicationContext(), "Semua Field Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtBebanKerja)) {
                    Toast.makeText(getApplicationContext(), "Jumlah/Beban Tugas Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtSifatPekerjaan)) {
                    Toast.makeText(getApplicationContext(), "Sifat Pekerjaan Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtWaktuPenyelesaian)) {
                    Toast.makeText(getApplicationContext(), "Waktu Penyelesaian Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(txtSatuan)) {
                    Toast.makeText(getApplicationContext(), "Satuan Tidak Boleh Kosong, Harus Di isi!", Toast.LENGTH_SHORT).show();
                } else {
                    if (txtSifatPekerjaan.equals(tempTahunan) && txtSatuan.equals(tempSatuanHari)) {
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
                    } else if (txtSifatPekerjaan.equals(tempBulanan) && txtSatuan.equals(tempSatuanHari)) {
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
                    } else if (txtSifatPekerjaan.equals(tempMingguan) && txtSatuan.equals(tempSatuanHari)) {
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
                    } else if (txtSifatPekerjaan.equals(tempHarian) && txtSatuan.equals(tempSatuanHari)) {
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
                    } else if (txtSifatPekerjaan.equals(tempTahunan) && txtSatuan.equals(tempSatuanJam)) {
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
                    } else if (txtSifatPekerjaan.equals(tempBulanan) && txtSatuan.equals(tempSatuanJam)) {
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
                    } else if (txtSifatPekerjaan.equals(tempMingguan) && txtSatuan.equals(tempSatuanJam)) {
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
                    } else if (txtSifatPekerjaan.equals(tempHarian) && txtSatuan.equals(tempSatuanJam)) {
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
                        Toast.makeText(getApplicationContext(), "Edit Data Rincian ABK Gagal!", Toast.LENGTH_SHORT).show();
                    }

                    reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").child(uid_rincian).updateChildren(hasil).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Edit Data Rincian ABK Berhasil!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(EditDetailAbkRincian.this, TampilSemuaAbkRincian.class);
                    intent.putExtra("uid_jabatan", uid_jabatan);
                    intent.putExtra("uid_uraian", uid_uraian);
                    intent.putExtra("uid_rincian", uid_rincian);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
