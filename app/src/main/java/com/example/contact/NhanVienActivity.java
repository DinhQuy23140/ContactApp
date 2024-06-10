package com.example.contact;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class NhanVienActivity extends AppCompatActivity {

    EditText edt_search;
    ImageButton btn_img;
    ListView lv_nhanvien;

    ArrayList<Nhanvien> list = new ArrayList<>();
    AdapterNhanvien adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nhan_vien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edt_search = (EditText) findViewById(R.id.edt_search_nhanvien);
        btn_img = (ImageButton) findViewById(R.id.btn_view_add_nhanvien);
        lv_nhanvien = (ListView) findViewById(R.id.lv_nhanvien);
        adapter = new AdapterNhanvien(NhanVienActivity.this, list);
        lv_nhanvien.setAdapter(adapter);
        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);

        try{
            String sql = "CREATE TABLE IF NOT EXISTS tb_nhanvien (manhanvien TEXT PRIMARY KEY, hoten TEXT, chucvu TEXT, email TEXT, sdt TEXT, madonvicha TEXT, logo TEXT)";
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Error","Error occurred: " + e.getMessage());
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Xử lý sự kiện nhấn nút "Back" ở đây
                Intent intent = new Intent(NhanVienActivity.this, MainActivity.class);
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
                searchNhanvien();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhanVienActivity.this, addNhanvien.class);
                startActivity(intent);
            }
        });

        lv_nhanvien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Nhanvien nhanvien = list.get(position);
                String manhanvien = nhanvien.getManhanvien();
                String hoten = nhanvien.getHoten();
                String chucvu = nhanvien.getChucvu();
                String email = nhanvien.getEmail();
                String sdt = nhanvien.getSdt();
                String avata = nhanvien.getAvatar();
                String madv = nhanvien.getMadonvi();
                Bundle bundle = new Bundle();
                bundle.putString("manhanvien", manhanvien);
                bundle.putString("hoten", hoten);
                bundle.putString("chucvu", chucvu);
                bundle.putString("email", email);
                bundle.putString("sdt", sdt);
                bundle.putString("avata", avata);
                bundle.putString("madonvi", madv);
                Intent intent = new Intent(NhanVienActivity.this, ThongtinNhanvien.class);
                intent.putExtra("Nhanvien", bundle);
                startActivity(intent);
            }
        });

        loadNhanvien();
    }

    private void loadNhanvien(){
        list.clear();
        String value = edt_search.getText().toString();
        Cursor cursor = db.query("tb_nhanvien", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                String manv = cursor.getString(0);
                String hoten = cursor.getString(1);
                String chucvu = cursor.getString(2);
                String email = cursor.getString(3);
                String sdt = cursor.getString(4);
                String madv = cursor.getString(5);
                String logo = cursor.getString(6);
                list.add(new Nhanvien(manv, hoten, chucvu, email, sdt,logo, madv));
            }while(cursor.moveToNext());
            Log.e("Size","Size" +  String.valueOf(list.size()));
        }
        Collections.sort(list, (p1, p2) -> p1.getHoten().compareToIgnoreCase(p2.getHoten()));
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void searchNhanvien(){
        list.clear();
        String value = edt_search.getText().toString();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_nhanvien", null );
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(1).contains(value)){
                    String manv = cursor.getString(0);
                    String hoten = cursor.getString(1);
                    String chucvu = cursor.getString(2);
                    String email = cursor.getString(3);
                    String sdt = cursor.getString(4);
                    String logo = cursor.getString(6);
                    String madv = cursor.getString(5);
                    list.add(new Nhanvien(manv, hoten, chucvu, email, sdt,logo, madv));
                }
            }
            while(cursor.moveToNext());
        }
        Collections.sort(list, (p1, p2) -> p1.getHoten().compareToIgnoreCase(p2.getHoten()));
        cursor.close();
        adapter.notifyDataSetChanged();
    }

}