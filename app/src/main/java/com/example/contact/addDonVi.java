package com.example.contact;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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

public class addDonVi extends AppCompatActivity {
    EditText edt_madonvi, edt_tendonvi, edt_email, edt_website, edt_diachi, edt_sdt;
    AutoCompleteTextView acv_madonvicha;
    ImageView img_logo;
    TextView tv_add_img;
    Button btn_add_donvi;
    String endcodeedImage;
    SQLiteDatabase db;
    ArrayList<String> options = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Boolean checkselectimg = false, checkInput = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_don_vi);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edt_madonvi = (EditText) findViewById(R.id.edt_madonvi);
        edt_tendonvi = (EditText) findViewById(R.id.edt_tendonvi);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_website = (EditText) findViewById(R.id.edt_website);
        edt_diachi = (EditText) findViewById(R.id.edt_diachi);
        edt_sdt = (EditText) findViewById(R.id.edt_sdt);
        acv_madonvicha = (AutoCompleteTextView) findViewById(R.id.acv_madonvicha);
        img_logo = (ImageView) findViewById(R.id.img_logo);
        tv_add_img = (TextView) findViewById(R.id.tv_add_img);
        btn_add_donvi = (Button) findViewById(R.id.btn_add_donvi);

        //open database
        db = openOrCreateDatabase("contact.db", MODE_PRIVATE, null);

        //create table
        try{
            String sql = "CREATE TABLE IF NOT EXISTS tb_donvi (madonvi TEXT PRIMARY KEY, tendonvi TEXT, email TEXT, website TEXT, diachi TEXT, sdt TEXT, madonvicha TEXT, logo TEXT)";
            db.execSQL(sql);
        }catch (Exception e){
            Log.e("Error", "Error: " + e.getMessage());
        }

        //get data from database
        Cursor cursor = db.rawQuery("SELECT madonvi FROM tb_donvi", null);
        if (cursor.moveToFirst()) {
            do {
                String madonvi = cursor.getString(0);
                //add data to arraylist
                options.add(madonvi);
            } while (cursor.moveToNext());
            //Toast.makeText(this, "Đã lấy dữ liệu: " + options.size(), Toast.LENGTH_SHORT).show();
        }
        cursor.close();

        //set adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, options);
        acv_madonvicha.setAdapter(adapter);
        //set focus autoCompleteTextView
        acv_madonvicha.setThreshold(1);

        //set event

        //validation
        edt_madonvi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edt_madonvi.getText().toString().isEmpty()){
                    showToast("Mã đơn vị không được để trống");
                    checkInput = false;
                }
                else{
                    checkInput = true;
                }
            }
        });

        edt_tendonvi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && edt_tendonvi.getText().toString().isEmpty()){
                    showToast("Tên đơn vị không được trống");
                    checkInput = false;
                }
                else{
                    checkInput = true;
                }
            }
        });


        edt_sdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && (edt_sdt.getText().toString().isEmpty() || edt_sdt.getText().toString().length() < 10)){
                    showToast("Số điện thoại không hợp lệ");
                    checkInput = false;
                }
                else{checkInput = true;}
            }
        });

        acv_madonvicha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                acv_madonvicha.showDropDown();
            }
        });

        btn_add_donvi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput){
                    String madonvi = edt_madonvi.getText().toString();
                    String tendonvi = edt_tendonvi.getText().toString();
                    String email = edt_email.getText().toString();
                    String website = edt_website.getText().toString();
                    String diachi = edt_diachi.getText().toString();
                    String sdt = edt_sdt.getText().toString();
                    String madonvicha = acv_madonvicha.getText().toString();
                    String logo;
                    if(endcodeedImage == null){
                        //vecto icon to string
                        logo = layerDrawableToBase64(addDonVi.this ,R.drawable.user_ic);
                    }
                    else{
                        logo = endcodeedImage;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("madonvi", madonvi);
                    contentValues.put("tendonvi", tendonvi);
                    contentValues.put("email", email);
                    contentValues.put("website", website);
                    contentValues.put("diachi", diachi);
                    contentValues.put("sdt", sdt);
                    contentValues.put("madonvicha", madonvicha);
                    contentValues.put("logo", logo);
                    if(db.insert("tb_donvi", null, contentValues) == -1){
                        showToast("Thêm đơn vị thất bại");
                    }
                    else{
                        showToast("Thêm đơn vị thành công");
                        Intent intent = new Intent(addDonVi.this, DonViActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                else{
                    showToast("Vui lòng kiểm tra lại dữ liệu");
                }
            }
        });

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        tv_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });


    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isPhoneNumberExists(SQLiteDatabase db, String phoneNumber) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tb_donvi WHERE sdt = ?", new String[]{phoneNumber});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
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
                            Glide.with(addDonVi.this)
                                    .load(bitmap) // Replace with your image source
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    //view
                                    .into(img_logo);
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

//    private Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
//    }
//    public void buttonRotate(View view) {
//        Bitmap originalBitmap = ((BitmapDrawable) img_logo.getDrawable()).getBitmap();
//        Bitmap rotatedBitmap = rotateImage(originalBitmap, 90);
//        img_logo.setImageBitmap(rotatedBitmap);
//        endcodeedImage = enCodeImage(rotatedBitmap);
//    }

}