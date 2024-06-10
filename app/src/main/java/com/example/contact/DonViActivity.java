package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DonViActivity extends AppCompatActivity {
    ImageButton btn_view_add;
    EditText edt_search;
    ListView lv_donvi;
    SQLiteDatabase db;
    ArrayList<Donvi> listDonVi = new ArrayList<>();
    AdapterDonvi adapterDonVi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_don_vi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lv_donvi = (ListView) findViewById(R.id.lv_donvi);
        adapterDonVi = new AdapterDonvi(DonViActivity.this, listDonVi);
        lv_donvi.setAdapter(adapterDonVi);
        edt_search = (EditText) findViewById(R.id.edt_search_donvi);
        btn_view_add = (ImageButton) findViewById(R.id.btn_view_add);

        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);
        try{
            String sql = "CREATE TABLE IF NOT EXISTS tb_donvi (madonvi TEXT PRIMARY KEY, tendonvi TEXT, email TEXT, website TEXT, diachi TEXT, sdt TEXT, madonvicha TEXT, logo TEXT)";
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Error","Error occurred: " + e.getMessage());
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý sự kiện nhấn nút "Back" ở đây
                Intent intent = new Intent(DonViActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btn_view_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonViActivity.this, addDonVi.class);
                startActivity(intent);
            }
        });

        lv_donvi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Donvi donvi = listDonVi.get(position);
                Intent intent = new Intent(DonViActivity.this, ThongtinDonvi.class);
                Bundle bundle = new Bundle();
                bundle.putString("madonvi", donvi.getMadonvi());
                bundle.putString("tendonvi", donvi.getTendonvi());
                bundle.putString("email", donvi.getEmail());
                bundle.putString("website", donvi.getWebsite());
                bundle.putString("diachi", donvi.getDiachi());
                bundle.putString("sdt", donvi.getSdt());
                bundle.putString("madonvicha", donvi.getMadonvicha());
                bundle.putString("logo", donvi.getLogo());
                intent.putExtra("Donvi", bundle);
                startActivity(intent);
            }
        });
        loadData();
    }

    @Override
    public void onBackPressed() {
        // Xử lý sự kiện nhấn nút "Back" ở đây
        super.onBackPressed(); // Gọi hàm này để thực hiện hành vi mặc định của nút "Back"
    }


    public void loadData(){
        Cursor cursor = db.query("tb_donvi", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            String madonvi = cursor.getString(0);
            String tendonvi = cursor.getString(1);
            String email = cursor.getString(2);
            String website = cursor.getString(3);
            String diachi = cursor.getString(4);
            String dienthoai = cursor.getString(5);
            String madonvicha = cursor.getString(6);
            String logo = cursor.getString(7);
            Donvi  donvi = new Donvi(madonvi, tendonvi, email, website, diachi, dienthoai, madonvicha, logo);
            listDonVi.add(donvi);
        }
        Collections.sort(listDonVi, (p1, p2) -> p1.getTendonvi().compareToIgnoreCase(p2.getTendonvi()));
        Log.d("LoadData", "Number of items loaded: " + listDonVi.size());
        adapterDonVi.notifyDataSetChanged();
        cursor.close();
    }

    public void search(){
        listDonVi.clear();
        String value = edt_search.getText().toString();
        Cursor cursor = db.query("tb_donvi", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getString(1).toLowerCase().contains(value.toLowerCase())){
                String madonvi = cursor.getString(0);
                String tendonvi = cursor.getString(1);
                String email = cursor.getString(2);
                String website = cursor.getString(3);
                String diachi = cursor.getString(4);
                String dienthoai = cursor.getString(5);
                String madonvicha = cursor.getString(6);
                String logo = cursor.getString(7);
                Donvi  donvi = new Donvi(madonvi, tendonvi, email, website, diachi, dienthoai, madonvicha, logo);
                listDonVi.add(donvi);
            }
        }
        Collections.sort(listDonVi, (p1, p2) -> p1.getTendonvi().compareToIgnoreCase(p2.getTendonvi()));
        Log.d("LoadData", "Number of items loaded: " + listDonVi.size());
        adapterDonVi.notifyDataSetChanged();
        cursor.close();
    }
}