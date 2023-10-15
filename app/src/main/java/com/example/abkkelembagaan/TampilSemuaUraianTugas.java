package com.example.abkkelembagaan;

import android.content.Context;
import android.content.Intent;
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

import com.example.abkkelembagaan.Adapter.JabatanAdapter;
import com.example.abkkelembagaan.Adapter.UraianAdapter;
import com.example.abkkelembagaan.Model.Jabatan;
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

public class TampilSemuaUraianTugas extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FloatingActionButton btn_tambah;
    Toolbar toolbar;
    Intent intent;
    String uid_jabatan;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView error;
    ImageView img_error;

    private RecyclerView recyclerView;
    private UraianAdapter uraianAdapter;
    private List<UraianTugas> mUraian;
    public static final List<String> uid = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_uraian_tugas);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryUraian));
            decor.setSystemUiVisibility(0);
        }

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        btn_tambah = (FloatingActionButton) findViewById(R.id.tambah_uraian);
        img_error = (ImageView) findViewById(R.id.img_not_found);
        error = (TextView) findViewById(R.id.error);

        recyclerView.setHasFixedSize(true);
        intent = getIntent();
        uid_jabatan = intent.getStringExtra("uid_jabatan");


        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(TampilSemuaUraianTugas.this));

        mUraian = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

        reference.child(uid_jabatan).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txtTitle = "";
                txtTitle = dataSnapshot.getValue(Jabatan.class).getNama_jabatan();

                collapsingToolbarLayout.setTitle(txtTitle);

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
                    String key = snapshot.getKey();
                    uraianTugas.setUraian_id(key);
                    uraianTugas.setId_jabatan(uid_jabatan);
                    mUraian.add(uraianTugas);
                }
                uraianAdapter = new UraianAdapter(context, mUraian);
                recyclerView.setAdapter(uraianAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show();
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TampilSemuaUraianTugas.this, TambahUraianTugas.class);
                intent.putExtra("uid_jabatan", uid_jabatan);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_uraian, menu);
        MenuItem mSearch = menu.findItem(R.id.menuSearchUraian);
        androidx.appcompat.widget.SearchView mSearchView = (androidx.appcompat.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Cari Uraian Tugas");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Intent intent = new Intent(TampilSemuaUraianTugas.this, MainActivity.class);
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
        List<UraianTugas> list2 = new ArrayList<>();
        for (UraianTugas uraianTugas : mUraian){
            if(uraianTugas.getNama_uraian_tugas().toLowerCase().contains(userInput)){
                list2.add(uraianTugas);
                error.setVisibility(View.INVISIBLE);
                img_error.setVisibility(View.INVISIBLE);
            } else {
                error.setVisibility(View.VISIBLE);
                img_error.setVisibility(View.VISIBLE);
            }
        }
        uraianAdapter.updateListUraian(list2);
        recyclerView.setAdapter(uraianAdapter);

        return true;
    }
}
