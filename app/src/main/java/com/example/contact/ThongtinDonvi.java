package com.example.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;

public class ThongtinDonvi extends AppCompatActivity {

    TextView tvmadonvi, tvtendonvi, tvdiachi, tvdienthoai, tvemail, tvwebsite, tvmadonvicha, tv_view_edit_donvi;
    ImageView ivlogo;
    ListView lv_donvicon, lv_nhanviendonvi;
    LinearLayout to_call_app, to_sendmess, to_sendemail;
    ArrayList<Donvi> listDonvicon = new ArrayList<>();
    AdapterDonvi adapterDonvi;
    AdapterNhanvien adapterNhanvien;
    ArrayList<Nhanvien> listNhanvien = new ArrayList<>();
    SQLiteDatabase db;
    private static final int REQUEST_CALL_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thongtin_donvi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);

        tvmadonvi = findViewById(R.id.tv_madonvi);
        tvtendonvi = findViewById(R.id.tv_tendonvi);
        tvdiachi = findViewById(R.id.tv_diachi);
        tvdienthoai = findViewById(R.id.tv_sdt);
        tvemail = findViewById(R.id.tv_email);
        tvwebsite = findViewById(R.id.tv_website);
        tvmadonvicha = findViewById(R.id.tv_madonvicha);
        ivlogo = findViewById(R.id.iv_logo);
        tv_view_edit_donvi = findViewById(R.id.tv_view_edit_donvi);
        to_call_app = findViewById(R.id.to_call_app);
        to_sendmess = findViewById(R.id.to_sendmess);
        to_sendemail = findViewById(R.id.to_sendemail);
        lv_nhanviendonvi = (ListView) findViewById(R.id.lv_nhanviendonvi);
        adapterNhanvien = new AdapterNhanvien(this, listNhanvien);
        lv_nhanviendonvi.setAdapter(adapterNhanvien);
        lv_donvicon = (ListView) findViewById(R.id.lv_donvicon);
        adapterDonvi = new AdapterDonvi(this, listDonvicon);
        lv_donvicon.setAdapter(adapterDonvi);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Donvi");
        if (bundle != null) {
            String madonvi = bundle.getString("madonvi");
            String tendonvi = bundle.getString("tendonvi");
            String diachi = bundle.getString("diachi");
            String dienthoai = bundle.getString("sdt");
            String email = bundle.getString("email");
            String website = bundle.getString("website");
            String logo = bundle.getString("logo");
            String getmadonvicha = bundle.getString("madonvicha");

            tvmadonvi.setText(madonvi);
            tvtendonvi.setText(tendonvi);
            tvdiachi.setText(diachi);
            tvdienthoai.setText(dienthoai);
            tvemail.setText(email);
            tvwebsite.setText(website);
            tvmadonvicha.setText(getmadonvicha);

            Bitmap imageBitmap = getImageView(logo);
            Glide.with(ThongtinDonvi.this)
                    .load(imageBitmap) // Replace with your image source
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(ivlogo);

            Cursor cursor = db.rawQuery("SELECT * FROM tb_donvi WHERE madonvicha = ?", new String[]{madonvi});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String madonvicon = cursor.getString(0);
                    String tendonvicon = cursor.getString(1);
                    String emaildonvicon = cursor.getString(2);
                    String websitedonvicon = cursor.getString(3);
                    String diachidonvicon = cursor.getString(4);
                    String sdtdonvicon = cursor.getString(5);
                    String madonvicha_ = cursor.getString(6);
                    String logodonvicon = cursor.getString(7);
                    Donvi donvicon = new Donvi(madonvicon, tendonvicon, emaildonvicon, websitedonvicon, diachidonvicon, sdtdonvicon, madonvicha_, logodonvicon);
                    listDonvicon.add(donvicon);
                }while (cursor.moveToNext());
                Collections.sort(listDonvicon, (p1, p2) -> p1.getTendonvi().compareToIgnoreCase(p2.getTendonvi()));
                cursor.close();
                adapterDonvi.notifyDataSetChanged();
            }

            Cursor cursor_nv = db.rawQuery("SELECT * FROM tb_nhanvien WHERE madonvicha = ?", new String[]{madonvi});
            if(cursor_nv.moveToFirst()){
                do{
                    String manv = cursor_nv.getString(0);
                    String hotennv = cursor_nv.getString(1);
                    String chucvunv = cursor_nv.getString(2);
                    String emailnv = cursor_nv.getString(3);
                    String sdtnv = cursor_nv.getString(4);
                    String madv = cursor_nv.getString(5);
                    String logonv = cursor_nv.getString(6);
                    listNhanvien.add(new Nhanvien(manv, hotennv, chucvunv, emailnv, sdtnv,logonv, madv));
                }while(cursor_nv.moveToNext());
                Log.e("Size","Size" +  String.valueOf(listNhanvien.size()));
            }
            Collections.sort(listNhanvien, (p1, p2) -> p1.getHoten().compareToIgnoreCase(p2.getHoten()));
            cursor_nv.close();
            adapterNhanvien.notifyDataSetChanged();
        }

        tv_view_edit_donvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentViewEdit = new Intent(ThongtinDonvi.this, editDonvi.class);
                intentViewEdit.putExtra("Donvi", bundle);
                startActivity(intentViewEdit);
            }
        });

        lv_donvicon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Donvi donvi = listDonvicon.get(position);
                Intent viewDonvi = new Intent(ThongtinDonvi.this, ThongtinDonvi.class);
                Bundle bundle = new Bundle();
                bundle.putString("madonvi", donvi.getMadonvi());
                bundle.putString("tendonvi", donvi.getTendonvi());
                bundle.putString("email", donvi.getEmail());
                bundle.putString("website", donvi.getWebsite());
                bundle.putString("diachi", donvi.getDiachi());
                bundle.putString("sdt", donvi.getSdt());
                bundle.putString("madonvicha", donvi.getMadonvicha());
                bundle.putString("logo", donvi.getLogo());
                viewDonvi.putExtra("Donvi", bundle);
                startActivity(viewDonvi);
            }
        });

        lv_nhanviendonvi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Nhanvien nhanvien = listNhanvien.get(position);
                Intent viewNhanvien = new Intent(ThongtinDonvi.this, ThongtinNhanvien.class);
                Bundle infNhanvien = new Bundle();
                String manhanvien = nhanvien.getManhanvien();
                String hoten = nhanvien.getHoten();
                String chucvu = nhanvien.getChucvu();
                String email = nhanvien.getEmail();
                String sdt = nhanvien.getSdt();
                String avata = nhanvien.getAvatar();
                String madv = nhanvien.getMadonvi();
                Bundle bundle = new Bundle();
                infNhanvien.putString("manhanvien", manhanvien);
                infNhanvien.putString("hoten", hoten);
                infNhanvien.putString("chucvu", chucvu);
                infNhanvien.putString("email", email);
                infNhanvien.putString("sdt", sdt);
                infNhanvien.putString("avata", avata);
                infNhanvien.putString("madonvi", madv);
                Intent intent = new Intent(ThongtinDonvi.this, ThongtinNhanvien.class);
                intent.putExtra("Nhanvien", infNhanvien);
                startActivity(intent);

            }
        });

        to_call_app.setOnClickListener(v -> {
            String phoneNumber = tvdienthoai.getText().toString();
            makePhoneCall(phoneNumber);
        });
        to_sendmess.setOnClickListener(v -> {
            String phoneNumber = tvdienthoai.getText().toString();
            String message = "";
            openMessagingApp(phoneNumber, message);
        });
        to_sendemail.setOnClickListener(v -> {
            String emailAddress = tvemail.getText().toString();
            String subject = "";
            String body = "";
            openEmailApp(emailAddress, subject, body);
        });
    }

    private Bitmap getImageView(String encodeImage) {
        if (encodeImage == null || encodeImage.isEmpty()) {
            // Trả về một hình ảnh mặc định hoặc null nếu encodeImage là null hoặc trống
            return null;
        }
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void makePhoneCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }
    private void openMessagingApp(String phoneNumber, String message) {
        // Create an intent to open the messaging app with pre-filled phone number and message text
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber)); // Use "sms:" or "smsto:"
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private void openEmailApp(String emailAddress, String subject, String body) {
        // Create an intent to open the email app with pre-filled email address, subject, and body
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}