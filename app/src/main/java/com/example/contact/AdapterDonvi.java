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

import java.util.ArrayList;
import java.util.List;

public class AdapterDonvi extends ArrayAdapter<Donvi> {
    private Context context;
    private List<Donvi> itemList;
    public AdapterDonvi(@NonNull Context context, ArrayList<Donvi> itemList) {
        super(context, R.layout.danhsachdonvi, itemList);
        this.context = context;
        this.itemList = itemList;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.danhsachdonvi, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_tendonvi_view);
//            viewHolder.tv_sdt = convertView.findViewById(R.id.tv_phone_view);
//            viewHolder.tv_email = convertView.findViewById(R.id.tv_email_view);
            viewHolder.img_logo = convertView.findViewById(R.id.img_logo_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Donvi currentItem = itemList.get(position);
        viewHolder.tv_name.setText(currentItem.getTendonvi());
//        viewHolder.tv_sdt.setText(currentItem.getSdt());
//        viewHolder.tv_email.setText(currentItem.getEmail());
        Bitmap imageBitmap = getImageView(currentItem.getLogo());
        Glide.with(context)
                .load(imageBitmap) // Replace with your image source
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(viewHolder.img_logo);
//        viewHolder.img_logo.setImageBitmap(getImageView(currentItem.getLogo()));

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

    static class ViewHolder {
        TextView tv_name;
        TextView tv_sdt;
        TextView tv_email;
        ImageView img_logo;
    }
}
