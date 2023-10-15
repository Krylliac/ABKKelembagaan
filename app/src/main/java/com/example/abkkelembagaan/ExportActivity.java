package com.example.abkkelembagaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abkkelembagaan.ABK.ExportJabatan;
import com.example.abkkelembagaan.Adapter.EksporAdapter;
import com.example.abkkelembagaan.Adapter.RincianAdapter;
import com.example.abkkelembagaan.Model.Jabatan;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private List<UraianTugas> mUraian;
    private List<RincianTugas> mRincian;
    private RecyclerView recyclerView;
    private EksporAdapter eksporAdapter;
    Button btn_ekspor_data;
    public static final ArrayList<String> uid_rincian = new ArrayList<>();
    Intent intent;
    String uid_jabatan;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DatabaseReference reference, referenceExport;
    public String txtJabatan, txtUnitKerja, key;
    RincianTugas rincianTugas;
    TextView error;
    ImageView img_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryExport));
            decor.setSystemUiVisibility(0);
        }

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExportActivity.this, ExportJabatan.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btn_ekspor_data = (Button) findViewById(R.id.ekspor_data);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        error = (TextView) findViewById(R.id.error);
        img_error = (ImageView) findViewById(R.id.img_not_found);

        rincianTugas = new RincianTugas();

        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(ExportActivity.this));
        mUraian = new ArrayList<>();
        mRincian = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Jabatan");

        reference.child(uid_jabatan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtJabatan = dataSnapshot.getValue(Jabatan.class).getNama_jabatan();
                txtUnitKerja = dataSnapshot.getValue(Jabatan.class).getUnit_kerja();
                collapsingToolbarLayout.setTitle(txtJabatan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child(uid_jabatan).child("UraianTugas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UraianTugas uraianTugas = snapshot.getValue(UraianTugas.class);
                    mUraian.add(uraianTugas);

                    DataSnapshot contentSnapshot = snapshot.child("RincianTugas");
                    Iterable<DataSnapshot> matchSnapShot = contentSnapshot.getChildren();
                    for (DataSnapshot match : matchSnapShot){
                        RincianTugas rincianTugas = match.getValue(RincianTugas.class);
                        mRincian.add(rincianTugas);
                    }
                }
                eksporAdapter = new EksporAdapter(getApplicationContext(), mUraian);
                recyclerView.setAdapter(eksporAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show();
            }
        });

        //Ekspor data

        btn_ekspor_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder data = new StringBuilder();
                data.append("Nama Jabatan   : ,"+ txtJabatan +"\n");
                data.append("Unit Kerja     : ,"+ txtUnitKerja+"\n\n");
                data.append("Uraian Tugas & Rincian Tugas, Sifat Pekerjaan, " +
                        "Jumlah/Beban Tugas, Waktu Penyelesaian, Satuan, Pegawai yang dibutuhkan \n");
                for (int i = 0; i < mUraian.size(); i++) {
                    data.append(mUraian.get(i).getNama_uraian_tugas()+"\n");
                    if(mRincian != null ){
                        for (int j = 0; j < mRincian.size() ; j++){
                            data.append("- "+mRincian.get(j).getNama_rincian_tugas() + ","
                                + mRincian.get(j).getSifat_pekerjaan() + ","
                                + mRincian.get(j).getBeban_kerja() + ","
                                + mRincian.get(j).getWaktu_penyelesaian() + ","
                                + mRincian.get(j).getNama_satuan() + ","
                                + mRincian.get(j).getPegawai_yang_dibutuhkan() +"\n");
                        }
                    }

                }

                try {
                    //Simpan File didalam device
                    FileOutputStream out = openFileOutput("abk.csv", Context.MODE_PRIVATE);

                    out.write((data.toString()).getBytes());
                    out.close();

                    //Eksporting
                    Context context = getApplicationContext();
                    File filelocation = new File(getFilesDir(), "abk.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.example.abkkelembagaan.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "HASIL ABK");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Simpan ke..."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_expor_data, menu);
        MenuItem mSearch = menu.findItem(R.id.menuSearchEkspor);
        androidx.appcompat.widget.SearchView mSearchView = (androidx.appcompat.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search/Filter Nama Jabatan");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Intent intent = new Intent(ExportActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        final List<UraianTugas> list1 = new ArrayList<>();
        for (UraianTugas uraianTugas: mUraian){
            if(uraianTugas.getNama_uraian_tugas().toLowerCase().contains(userInput)){
                list1.add(uraianTugas);
                error.setVisibility(View.INVISIBLE);
                img_error.setVisibility(View.INVISIBLE);
            } else{
                error.setVisibility(View.VISIBLE);
                img_error.setVisibility(View.VISIBLE);
            }
        }
        eksporAdapter.updateList(list1);
        recyclerView.setAdapter(eksporAdapter);

        return true;
    }
}
