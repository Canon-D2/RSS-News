package com.example.bt3_rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/** CUSTOM LISTVIEW NÂNG CAO **/
public class Customadapter extends ArrayAdapter<Docbao> {

    public Customadapter(Context context, int resource, List<Docbao> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.dong_layout_listview, null);
        }
        Docbao p = getItem(position);
        if (p != null) {
            // Ánh xạ và gán giá trị
            TextView txttitle = (TextView) view.findViewById(R.id.textviewtitle);
            txttitle.setText(p.title);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            // Sử dụng thư viện Picasso -> load hình ảnh
            Picasso.with(getContext()).load(p.image).into(imageView);
        }
        return view;
    }
}