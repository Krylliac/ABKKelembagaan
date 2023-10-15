package com.example.abkkelembagaan.ABK;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abkkelembagaan.Adapter.EksporAdapter;
import com.example.abkkelembagaan.Adapter.EksporJabatanAdapter;
import com.example.abkkelembagaan.Adapter.RincianAdapter;
import com.example.abkkelembagaan.MainActivity;
import com.example.abkkelembagaan.Model.Jabatan;
import com.example.abkkelembagaan.Model.RincianTugas;
import com.example.abkkelembagaan.R;
import com.example.abkkelembagaan.TampilSemuaJabatan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExportJabatan extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private List<Jabatan> mJabatan;
    private RecyclerView recyclerView;
    private EksporJabatanAdapter eksporJabatanAdapter;
    public static final ArrayList<String> uid_rincian = new ArrayList<>();
    TextView error;
    ImageView img_error;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_jabatan);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryExport));
            decor.setSystemUiVisibility(0);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExportJabatan.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        error = (TextView) findViewById(R.id.error);
        img_error = (ImageView) findViewById(R.id.img_not_found);

        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(ExportJabatan.this));
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

                eksporJabatanAdapter = new EksporJabatanAdapter(getApplicationContext(), mJabatan);
                recyclerView.setAdapter(eksporJabatanAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(getApplicationContext(), "Gagal Menampilkan Data", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(ExportJabatan.this, MainActivity.class);
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
        final List<Jabatan> list1 = new ArrayList<>();
        for (Jabatan jabatan: mJabatan){
            if(jabatan.getNama_jabatan().toLowerCase().contains(userInput)){
                list1.add(jabatan);
                error.setVisibility(View.INVISIBLE);
                img_error.setVisibility(View.INVISIBLE);
            } else {
                error.setVisibility(View.VISIBLE);
                img_error.setVisibility(View.VISIBLE);
            }
        }
        eksporJabatanAdapter.updateList(list1);
        recyclerView.setAdapter(eksporJabatanAdapter);

        return true;
    }


}
