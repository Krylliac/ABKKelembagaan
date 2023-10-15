package com.example.abkkelembagaan;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailRincianTugas extends AppCompatActivity {
    TextView uraian_tugas, rincian_tugas, satuan_hasil, norma_waktu, sifat_pekerjaan,
            sifat_pekerjaan_menit, sifat_pekerjaan_tahun, volume_tugas, beban_kerja,
            beban_kerja_tahun, pegawai_dibutuhkan, nama_jabatan, unit_kerja;
    Intent intent;
    DatabaseReference reference;
    Toolbar toolbar;
    RincianTugas rTugas;
    Context context = this;
    String uid_rincian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rincian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

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
                Intent intent = new Intent(DetailRincianTugas.this, EditRincianTugas.class);
                intent.putExtra("uid_rincian", uid_rincian);
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
                                reference.child(uid_rincian).removeValue();
                                Intent intent = new Intent(DetailRincianTugas.this, TampilSemuaRincianTugas.class);
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
