package com.example.abkkelembagaan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.abkkelembagaan.ABK.ExportJabatan;
import com.example.abkkelembagaan.ABK.TampilSemuaAbkJabatan;
import com.example.abkkelembagaan.Adapter.EksporJabatanAdapter;

public class MainActivity extends AppCompatActivity {

    CardView cvJabatan, cvRinciantugas, cvUraiankerja, cvSettings, cvAbout, cvEkspor, cvAbk;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDashboard));
            decor.setSystemUiVisibility(0);
        }

        cvJabatan = (CardView)findViewById(R.id.cvJabatan);
        cvRinciantugas = (CardView)findViewById(R.id.cvRincianTugas);
        cvUraiankerja = (CardView)findViewById(R.id.cvUraiankerja);
        cvSettings = (CardView) findViewById(R.id.cvSettings);
        //cvAbout = (CardView)findViewById(R.id.cvAbout);
        cvEkspor = (CardView)findViewById(R.id.cvExport);
        cvAbk = (CardView) findViewById(R.id.cvAbk);

        cvJabatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TampilSemuaJabatan.class);
                startActivity(intent);
            }
        });
        cvRinciantugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RincianJabatan.class);
                startActivity(intent);
            }
        });
        cvUraiankerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TampilSemuaUraianJabatan.class);
                startActivity(intent);
            }
        });
        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        //cvAbout.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        //        startActivity(intent);
        //    }
        //});
        cvEkspor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExportJabatan.class);
                startActivity(intent);
            }
        });
        cvAbk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TampilSemuaAbkJabatan.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setIcon(R.drawable.warning);
        alertDialog.setTitle("Menutup Aplikasi");
        alertDialog.setMessage("Apakah Anda Yakin Keluar Dari Aplikasi?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();

        Button btPositive = alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE);
        btPositive.setTextColor(this.getResources().getColor(R.color.colorTextAlert));
        Button btNegative = alertDialog1.getButton(DialogInterface.BUTTON_NEGATIVE);
        btNegative.setTextColor(this.getResources().getColor(R.color.colorTextAlert));


    }
}
