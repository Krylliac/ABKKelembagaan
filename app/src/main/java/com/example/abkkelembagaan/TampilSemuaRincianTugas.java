package com.example.abkkelembagaan;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.abkkelembagaan.Adapter.RincianAdapter;
import com.example.abkkelembagaan.Adapter.UraianAdapter;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.Model.UraianTugas;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TampilSemuaRincianTugas extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FloatingActionButton btn_rincian_tugas;
    private List<RincianTugas> mRincian;
    private RecyclerView recyclerView;
    private RincianAdapter rincianAdapter;
    public static final ArrayList<String> uid_rincian = new ArrayList<>();
    Intent intent;
    String uid_jabatan, uid_uraian;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Context context = this;
    TextView error;
    ImageView img_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_rincian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryRincian));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TampilSemuaRincianTugas.this, TampilSemuaRincianUraianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                startActivity(intent);
                finish();
            }
        });

        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");
        uid_uraian = intent.getStringExtra("uid_uraian");

        btn_rincian_tugas = (FloatingActionButton) findViewById(R.id.tambah_rincian_tugas);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        error = (TextView) findViewById(R.id.error);
        img_error = (ImageView) findViewById(R.id.img_not_found);

        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(TampilSemuaRincianTugas.this));
        mRincian = new ArrayList<>();

        btn_rincian_tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TampilSemuaRincianTugas.this, TambahRincianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                intent.putExtra("uid_uraian", uid_uraian);
                startActivity(intent);
                finish();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtTitle = "";
                txtTitle = dataSnapshot.getValue(UraianTugas.class).getNama_uraian_tugas();

                collapsingToolbarLayout.setTitle(txtTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child(uid_jabatan).child("UraianTugas").child(uid_uraian).child("RincianTugas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RincianTugas rincianTugas = snapshot.getValue(RincianTugas.class);
                    String uid_rincian = snapshot.getKey();
                    rincianTugas.setId_nama_jabatan(uid_jabatan);
                    rincianTugas.setId_uraian_tugas(uid_uraian);
                    rincianTugas.setId_rincian_tugas(uid_rincian);
                    mRincian.add(rincianTugas);
                }

                rincianAdapter = new RincianAdapter(context, mRincian);
                recyclerView.setAdapter(rincianAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_rincian, menu);
        MenuItem mSearch = menu.findItem(R.id.menuSearchRincian);
        androidx.appcompat.widget.SearchView mSearchView = (androidx.appcompat.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Cari Rincian Tugas");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Intent intent = new Intent(TampilSemuaRincianTugas.this, MainActivity.class);
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
        List<RincianTugas> list1 = new ArrayList<>();
        for (RincianTugas rincianTugas : mRincian){
            if(rincianTugas.getNama_rincian_tugas().toLowerCase().contains(userInput)){
                list1.add(rincianTugas);
                error.setVisibility(View.INVISIBLE);
                img_error.setVisibility(View.INVISIBLE);
            }else {
                error.setVisibility(View.VISIBLE);
                img_error.setVisibility(View.VISIBLE);
            }
        }
        rincianAdapter.updateList(list1);
        recyclerView.setAdapter(rincianAdapter);

        return true;
    }

}
