package com.example.abkkelembagaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.abkkelembagaan.Adapter.JabatanAdapter;
import com.example.abkkelembagaan.Adapter.UraianJabatanAdapter;
import com.example.abkkelembagaan.Model.Jabatan;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TampilSemuaUraianJabatan extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    Context context = this;
    ImageView img_error;
    TextView error;

    private UraianJabatanAdapter uraianJabatanAdapter;
    private List<Jabatan> mJabatan;
    public static final List<String> uid = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_uraian_jabatan);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            decor.setSystemUiVisibility(0);
        }

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TampilSemuaUraianJabatan.this));
        error = (TextView) findViewById(R.id.error);
        img_error = (ImageView) findViewById(R.id.img_not_found);

        mJabatan = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Jabatan");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Jabatan jabatan = snapshot.getValue(Jabatan.class);
                    String key = snapshot.getKey();
                    jabatan.setId_jabatan(key);
                    mJabatan.add(jabatan);
                }
                uraianJabatanAdapter = new UraianJabatanAdapter(context, mJabatan);
                recyclerView.setAdapter(uraianJabatanAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_jabatan, menu);
        MenuItem mSearch = menu.findItem(R.id.menuSearch);
        androidx.appcompat.widget.SearchView mSearchView = (androidx.appcompat.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Cari Jabatan");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Intent intent = new Intent(TampilSemuaUraianJabatan.this, MainActivity.class);
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
        List<Jabatan> list1 = new ArrayList<>();
        for (Jabatan jabatan : mJabatan){
            if(jabatan.getNama_jabatan().toLowerCase().contains(userInput)){
                list1.add(jabatan);
                error.setVisibility(View.INVISIBLE);
                img_error.setVisibility(View.INVISIBLE);
            } else {
                error.setVisibility(View.VISIBLE);
                img_error.setVisibility(View.VISIBLE);
            }
        }
        uraianJabatanAdapter.updateList(list1);
        recyclerView.setAdapter(uraianJabatanAdapter);

        return true;
    }
}
