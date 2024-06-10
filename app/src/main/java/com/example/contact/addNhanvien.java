package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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
import java.util.Arrays;

public class addNhanvien extends AppCompatActivity {

    TextView tv_cancel, tv_select_avatar;
    EditText edt_manhanvien, edt_hoten, edt_chucvu, edt_email, edt_sdt;
    AutoCompleteTextView av_madonvi, av_chucvu;
    ImageView iv_avatar;
    Button btn_add_nhanvien;
    String endcodeedImage;
    String logo;
    ArrayList<String> universityPositions = new ArrayList<>(Arrays.asList(
            "Hiệu trưởng",
            "Phó Hiệu trưởng",
            "Trưởng khoa",
            "Phó trưởng khoa",
            "Trưởng bộ môn",
            "Giáo sư",
            "Phó Giáo sư",
            "Giảng viên",
            "Trợ giảng",
            "Nghiên cứu viên",
            "Trưởng phòng đào tạo",
            "Cố vấn học tập",
            "Thư ký khoa",
            "Quản lý sinh viên",
            "Thư ký học vụ",
            "Thủ thư",
            "Nhân viên hành chính",
            "Quản lý ký túc xá",
            "Chuyên viên hỗ trợ tài chính",
            "Nhân viên IT",
            "Trưởng phòng nghiên cứu",
            "Trưởng phòng hợp tác quốc tế",
            "Chuyên viên tuyển sinh",
            "Giám đốc trung tâm"
    ));
    ArrayList<String> listMadonvi = new ArrayList<>();
    ArrayAdapter<String> universityAdapter, madonviAdapter;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_nhanvien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv_cancel = findViewById(R.id.tv_cancel_add_nhanvien);
        tv_select_avatar = findViewById(R.id.tv_add_nhanvien_select);
        iv_avatar = findViewById(R.id.iv_add_nhanvien_avatar);
        edt_manhanvien = findViewById(R.id.edt_manhanvien);
        edt_hoten = findViewById(R.id.edt_hoten);
        av_chucvu = findViewById(R.id.acv_add_nhanvien_chucvu);
        edt_email = findViewById(R.id.edt_email_nhanvien);
        edt_sdt = findViewById(R.id.edt_sdt_nhanvien);
        av_madonvi = findViewById(R.id.acv_add_nhanvien_madonvi);
        btn_add_nhanvien = (Button) findViewById(R.id.btn_add_nhanvien);
        universityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, universityPositions);
        av_chucvu.setAdapter(universityAdapter);

        madonviAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listMadonvi);
        av_madonvi.setAdapter(madonviAdapter);

        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);
        try{
            String sql = "CREATE TABLE IF NOT EXISTS tb_nhanvien (manhanvien TEXT PRIMARY KEY, hoten TEXT, chucvu TEXT, email TEXT, sdt TEXT, madonvicha TEXT, logo TEXT)";
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Error","Error occurred: " + e.getMessage());
        }

        Cursor cursor = db.rawQuery("SELECT madonvi FROM tb_donvi", null);
        if(cursor.moveToFirst()){
            do{
                listMadonvi.add(cursor.getString(0));
            }while (cursor.moveToNext());
            cursor.close();
            madonviAdapter.notifyDataSetChanged();
        }
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImage.launch(intent);
            }
        });

        tv_select_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImage.launch(intent);
            }
        });

        edt_manhanvien.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edt_manhanvien.getText().toString().isEmpty()){
                    showError("Mã nhân viên không được để trống");
                }
            }
        });

        edt_hoten.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edt_hoten.getText().toString().isEmpty()){
                    showError("Họ và tên không được để trống");
                }
            }
        });

        edt_sdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && (edt_sdt.getText().toString().isEmpty() || edt_sdt.getText().toString().length() < 10)){
                    showError("Số điện thoại không được để trống và phải có 10 số");
                }
            }
        });

        av_chucvu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                av_chucvu.showDropDown();
            }
        });

        edt_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && (edt_email.getText().toString().isEmpty() || !!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()));
            }
        });

        av_madonvi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                av_madonvi.showDropDown();
                if(!hasFocus && av_madonvi.getText().toString().isEmpty()){
                    showError("Mã đơn vị không được để trống");
                }
            }
        });

        btn_add_nhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_manhanvien.getText().toString().isEmpty() && !edt_manhanvien.getText().toString().isEmpty() && (!edt_sdt.getText().toString().isEmpty() || edt_sdt.getText().toString().length() >= 10)){
                    ContentValues values = new ContentValues();
                    values.put("manhanvien", edt_manhanvien.getText().toString());
                    values.put("hoten", edt_hoten.getText().toString());
                    values.put("chucvu", av_chucvu.getText().toString());
                    values.put("email", edt_email.getText().toString());
                    values.put("sdt", edt_sdt.getText().toString());
                    if(endcodeedImage == null){
                        logo = layerDrawableToBase64(addNhanvien.this, R.drawable.user_ic);
                    }
                    else{
                        logo = endcodeedImage;
                        //Toast.makeText(addNhanvien.this, "Đã chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                    values.put("madonvicha", av_madonvi.getText().toString());
                    values.put("logo", logo);
                    if(db.insert("tb_nhanvien", null, values) == -1){
                        showError("Mã nhân viên đã tồn tại");
                    }
                    else{
                        Toast.makeText(addNhanvien.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addNhanvien.this, NhanVienActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    public void showError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                            //img_logo.setImageBitmap(bitmap);
                            endcodeedImage = enCodeImage(bitmap);
                            //activity
                            Glide.with(addNhanvien.this)
                                    .load(bitmap) // Replace with your image source
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    //view
                                    .into(iv_avatar);
                        }
                        catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
    );

    public String layerDrawableToBase64(Context context, int drawableId) {
        // Lấy LayerDrawable từ resource ID
        LayerDrawable layerDrawable = (LayerDrawable) context.getDrawable(drawableId);

        // Tạo một bitmap với kích thước của LayerDrawable
        int width = layerDrawable.getIntrinsicWidth();
        int height = layerDrawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Tạo một canvas với bitmap đó
        Canvas canvas = new Canvas(bitmap);

        // Vẽ LayerDrawable lên canvas
        layerDrawable.setBounds(0, 0, width, height);
        layerDrawable.draw(canvas);

        // Chuyển đổi bitmap thành chuỗi base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    public String vectorToBase64(Context context, int vectorResourceId) {
        VectorDrawable vectorDrawable = (VectorDrawable) context.getDrawable(vectorResourceId);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}