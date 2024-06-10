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
import java.util.Arrays;

public class EditNhanvien extends AppCompatActivity {

    TextView btn_cancel_edit, tv_save, tv_delete;
    ImageView iv_avata;
    EditText edt_manhanvien, edt_hoten, edt_email, edt_sdt;
    AutoCompleteTextView acv_madonvi, acv_chucvu;
    Bundle bundle;
    String endcodeedImage, avatar;
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
    ArrayList<String> list_madonvi = new ArrayList<>();
    ArrayAdapter<String> adaptor_chucvu, adapter_madonvi;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_nhanvien);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);
        edt_manhanvien = findViewById(R.id.edt_view_nv_manhanvien);
        edt_hoten = findViewById(R.id.edt_view_nv_tennhanvien);
        acv_chucvu = findViewById(R.id.act_view_nv_chucvu);
        edt_email = findViewById(R.id.edt_view_nv_email);
        edt_sdt = findViewById(R.id.edt_view_nv_sdt);
        acv_chucvu = findViewById(R.id.act_view_nv_chucvu);
        adaptor_chucvu = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, universityPositions);
        acv_chucvu.setAdapter(adaptor_chucvu);
        acv_chucvu.setThreshold(1);
        acv_madonvi = findViewById(R.id.act_view_madonvi);
        tv_save = findViewById(R.id.btn_edit_save_nhanvien);
        tv_delete = findViewById(R.id.delete_nhanvien);
        iv_avata = findViewById(R.id.iv_edit_nv_avatar);
        btn_cancel_edit = findViewById(R.id.btn_cancel_edit_nhanvien);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("Nhanvien");
        if (bundle != null) {
            String manhanvien = bundle.getString("manhanvien");
            String tennhanvien = bundle.getString("hoten");
            String chucvu = bundle.getString("chucvu");
            String email = bundle.getString("email");
            String sdt = bundle.getString("sdt");
            String avata = bundle.getString("avata");
            String madonvi = bundle.getString("madonvi");
            edt_manhanvien.setText(manhanvien);
            edt_hoten.setText(tennhanvien);
            acv_chucvu.setText(chucvu);
            edt_email.setText(email);
            edt_sdt.setText(sdt);
            acv_madonvi.setText(madonvi);
            Bitmap bitmap = getImageView(avata);
            //iv_avata.setImageBitmap(bitmap);
            if(bitmap != null) {
                Glide.with(EditNhanvien.this)
                        .load(bitmap) // Replace with your image source
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(iv_avata);
            }
            else{
                Glide.with(EditNhanvien.this)
                        .load(R.drawable.user_ic) // Replace with your image source
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(iv_avata);
            }
        }

        Cursor cursor = db.rawQuery("SELECT madonvi FROM tb_donvi", null);
        if(cursor.moveToFirst()){
            do{
                list_madonvi.add(cursor.getString(0));
            }while (cursor.moveToNext());
            cursor.close();
            adapter_madonvi = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list_madonvi);
            acv_madonvi.setAdapter(adapter_madonvi);
            acv_madonvi.setThreshold(1);
            Toast.makeText(this,"Size" + list_madonvi.size(), Toast.LENGTH_SHORT).show();
        }

        edt_hoten.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && edt_hoten.getText().toString().isEmpty()) {
                showMessage("Tên nhân viên không được để trống");
            }
        });

        edt_manhanvien.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && edt_manhanvien.getText().toString().isEmpty()) {
                showMessage("Mã nhân viên không được để trống");
            }
        });

        edt_sdt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && edt_sdt.getText().toString().length() < 10) {
                showMessage("Số điện thoại không hợp lệ");
            }
        });

        iv_avata.setOnClickListener(v -> {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    pickImage.launch(pickPhoto);
        });
        acv_chucvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acv_chucvu.showDropDown();
            }
        });

        acv_madonvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acv_madonvi.showDropDown();
            }
        });

        btn_cancel_edit.setOnClickListener(v -> finish());

        tv_save.setOnClickListener(v ->{
            if(edt_hoten.getText().toString().isEmpty() || edt_manhanvien.getText().toString().isEmpty() || edt_sdt.getText().toString().length()<10){
                showMessage("Thông tin không hợp lệ, vui lòng kiểm tra lại");
            }
            else{
                String manhanvien = bundle.getString("manhanvien");
                if(endcodeedImage != null){
                    avatar = endcodeedImage;
                    //Toast.makeText(this, avatar, Toast.LENGTH_SHORT).show();
                }
                else{
                    avatar = bundle.getString("avata");
                }

                Bundle toInfNhanvien = new Bundle();
                toInfNhanvien.putString("manhanvien", edt_manhanvien.getText().toString());
                toInfNhanvien.putString("hoten", edt_hoten.getText().toString());
                toInfNhanvien.putString("chucvu", acv_chucvu.getText().toString());
                toInfNhanvien.putString("email", edt_email.getText().toString());
                toInfNhanvien.putString("sdt", edt_sdt.getText().toString());
                toInfNhanvien.putString("madonvi", acv_madonvi.getText().toString());
                toInfNhanvien.putString("avata", avatar);

                ContentValues values = new ContentValues();
                values.put("manhanvien", edt_manhanvien.getText().toString());
                values.put("hoten", edt_hoten.getText().toString());
                values.put("chucvu", acv_chucvu.getText().toString());
                values.put("email", edt_email.getText().toString());
                values.put("sdt", edt_sdt.getText().toString());
                values.put("madonvicha", acv_madonvi.getText().toString());
                values.put("logo", avatar);

                db.update("tb_nhanvien", values, "manhanvien = ?", new String[]{manhanvien});
                showMessage("Cập nhật thành công");

                Intent to_nhanvienActivity = new Intent(EditNhanvien.this, ThongtinNhanvien.class);
                to_nhanvienActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                to_nhanvienActivity.putExtra("Nhanvien", toInfNhanvien);
                startActivity(to_nhanvienActivity);
            }
        });

        tv_delete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditNhanvien.this);
            dialog.setTitle("Xác nhận xóa");
            dialog.setMessage("Bạn có chắc chắn muốn xóa nhân viên này không?");
            dialog.setPositiveButton("Có", (dialog1, which) -> {
                String manhanvien = bundle.getString("manhanvien");
                if(db.delete("tb_nhanvien", "manhanvien = ?", new String[]{manhanvien}) > 0){
                    showMessage("Xóa thành công");
                    Intent to_nhanvienActivity = new Intent(EditNhanvien.this, NhanVienActivity.class);
                    to_nhanvienActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(to_nhanvienActivity);
                }
            });
            dialog.setNegativeButton("Không", (dialog12, which) -> {dialog12.dismiss();});
            dialog.show();
        });

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                            Glide.with(EditNhanvien.this)
                                    .load(bitmap) // Replace with your image source
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(iv_avata);
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