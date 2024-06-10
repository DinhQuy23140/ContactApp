package com.example.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AdapterNhanvien extends ArrayAdapter<Nhanvien> {

    private Context context;
    private List<Nhanvien> itemList;
    public AdapterNhanvien(@NonNull Context context, List<Nhanvien> listNhanvien) {
        super(context, R.layout.danhsachdonvi , listNhanvien);
        this.context = context;
        this.itemList = listNhanvien;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.danhsachdonvi, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_tendonvi_view);
            viewHolder.img_logo = convertView.findViewById(R.id.img_logo_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Nhanvien currentItem = itemList.get(position);
        viewHolder.tv_name.setText(currentItem.getHoten());
        Bitmap imageBitmap = getImageView(currentItem.getAvatar());
        Glide.with(context)
                .load(imageBitmap) // Replace with your image source
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(viewHolder.img_logo);
        return convertView;
    }

    private Bitmap getImageView(String encodeImage) {
        if (encodeImage == null || encodeImage.isEmpty()) {
            // Trả về một hình ảnh mặc định hoặc null nếu encodeImage là null hoặc trống
            return null;
        }
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    static class ViewHolder{
        TextView tv_name;
        ImageView img_logo;
    }
}
