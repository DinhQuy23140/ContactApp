package com.example.contact;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class editDonvi extends AppCompatActivity {
    EditText edtmadonvi, edttendonvi, edtdiachi, edtdienthoai, edtemail, edtwebsite, edtmadonvicha;
    ImageView imglogo;
    TextView btnsave, btncancel, btndelete;
    String endcodeedImage;

    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> list_madonvicha = new ArrayList<>();
    ArrayList<String> list_madonvicha_temp = new ArrayList<>();
    ArrayAdapter<String> adapter;
    SQLiteDatabase db;
    String logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_donvi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);

        edtmadonvi = findViewById(R.id.tv_edit_madonvi);
        edttendonvi = findViewById(R.id.tv_edit_tendonvi);
        edtdiachi = findViewById(R.id.tv_edit_diachi);
        edtdienthoai = findViewById(R.id.tv_edit_sdt);
        edtemail = findViewById(R.id.tv_edit_email);
        edtwebsite = findViewById(R.id.tv_edit_website);
        //edtmadonvicha = findViewById(R.id.av_edit_madonvicha);
        imglogo = findViewById(R.id.iv_edit_avatar);
        btnsave = findViewById(R.id.btn_edit_save);
        btncancel = findViewById(R.id.btn_edit_cancel);
        btndelete = findViewById(R.id.btn_edit_delete);
        autoCompleteTextView = findViewById(R.id.av_edit_madonvicha);
        Cursor cursor = db.rawQuery("SELECT madonvi FROM tb_donvi", null);
        if (cursor.moveToFirst()) {
            do {
                String madonvi = cursor.getString(0);
                list_madonvicha.add(madonvi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list_madonvicha);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                autoCompleteTextView.showDropDown();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Donvi");
        if (bundle != null) {
            String madonvi = bundle.getString("madonvi");
            String tendonvi = bundle.getString("tendonvi");
            String diachi = bundle.getString("diachi");
            String dienthoai = bundle.getString("sdt");
            String email = bundle.getString("email");
            String website = bundle.getString("website");
            logo = bundle.getString("logo");
            String madonvicha = bundle.getString("madonvicha");

            edtmadonvi.setText(madonvi);
            edttendonvi.setText(tendonvi);
            edtdiachi.setText(diachi);
            edtdienthoai.setText(dienthoai);
            edtemail.setText(email);
            edtwebsite.setText(website);
            //edtmadonvicha.setText(madonvicha);
            autoCompleteTextView.setText(madonvicha);
            Bitmap bitmap = getImageView(logo);
            if (bitmap != null) {
                //imglogo.setImageBitmap(bitmap);
                //border imageview
                Glide.with(editDonvi.this)
                        .load(bitmap) // Replace with your image source
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imglogo);
            }
        }

        //change avatar event
        imglogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(pickPhoto);
            }
        });

        //save event

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String madonvi = edtmadonvi.getText().toString();
                String tendonvi = edttendonvi.getText().toString();
                String diachi = edtdiachi.getText().toString();
                String dienthoai = edtdienthoai.getText().toString();
                String email = edtemail.getText().toString();
                String website = edtwebsite.getText().toString();
                String madonvicha = autoCompleteTextView.getText().toString();
                if(endcodeedImage != null){
                    logo = endcodeedImage;
                }

                Bundle infDonvi = new Bundle();
                infDonvi.putString("madonvi", madonvi);
                infDonvi.putString("tendonvi", tendonvi);
                infDonvi.putString("email", email);
                infDonvi.putString("website", website);
                infDonvi.putString("diachi", diachi);
                infDonvi.putString("sdt", dienthoai);
                infDonvi.putString("madonvicha", madonvicha);
                infDonvi.putString("logo", logo);

                ContentValues contentValues = new ContentValues();
                contentValues.put("madonvi", madonvi);
                contentValues.put("tendonvi", tendonvi);
                contentValues.put("email", email);
                contentValues.put("website", website);
                contentValues.put("diachi", diachi);
                contentValues.put("sdt", dienthoai);
                contentValues.put("madonvicha", madonvicha);
                contentValues.put("logo", logo);

                db.update("tb_donvi", contentValues, "madonvi = ?", new String[]{madonvi});
                Toast.makeText(editDonvi.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                Intent exit = new Intent(editDonvi.this, ThongtinDonvi.class);
                exit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                exit.putExtra("Donvi", infDonvi);
                startActivity(exit);
            }
        });

        //cancel event
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //delete event
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(editDonvi.this);
                dialog.setTitle("Xác nhận xóa");
                dialog.setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?");
                dialog.setPositiveButton("Có", (dialog1, which) -> {
                    int n = db.delete("tb_donvi", "madonvi = ?", new String[]{edtmadonvi.getText().toString()});
                    if (n > 0) {
                        Toast.makeText(editDonvi.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        Intent viewDonvi = new Intent(editDonvi.this, DonViActivity.class);
                        viewDonvi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(viewDonvi);
                    }
                });
                dialog.setNegativeButton("Không", (dialog12, which) -> {dialog12.dismiss();});
                dialog.show();
            }
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

    private String enCodeImage(Bitmap bitmap){
        //set with
        int previewWith = 150;
        //set height
        int previewHeight = bitmap.getHeight() * previewWith / bitmap.getWidth();
        //scale image
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageuri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageuri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            //set img(bitmap)
                            //imglogo.setImageBitmap(bitmap);
                            Glide.with(editDonvi.this)
                                    .load(bitmap) // Replace with your image source
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(imglogo);
                            //bitmap->string
                            endcodeedImage = enCodeImage(bitmap);
                        }
                        catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );
}