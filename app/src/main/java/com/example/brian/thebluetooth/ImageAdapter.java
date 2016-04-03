package com.example.brian.thebluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private final List<Item> mItems = new ArrayList<>();
    private final LayoutInflater mInflater;

    public ImageAdapter(Context c) {
        mInflater = LayoutInflater.from(c);

        mItems.add(new Item("Home", R.drawable.home_dark));
        mItems.add(new Item("bluetooth_colour", R.drawable.bluetooth_colour));
        mItems.add(new Item("Map", R.drawable.map_colour));
        mItems.add(new Item("Messaging", R.drawable.messaging_colour));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).drawableId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ImageView picture;
        TextView name;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.grid_item, parent, false);
            view.setTag(R.id.picture, view.findViewById(R.id.picture));
            view.setTag(R.id.text, view.findViewById(R.id.text));
        }

        picture = (ImageView) view.getTag(R.id.picture);
        name = (TextView) view.getTag(R.id.text);

        Item item = (Item)getItem(position);

        picture.setImageResource(item.drawableId);
        name.setText(item.name);

        return view;
    }

    private static class Item {
        public final String name;
        public final int drawableId;

        Item(String name, int drawableId) {
            this.name = name;
            this.drawableId = drawableId;
        }
    }
}
