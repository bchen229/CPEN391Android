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

public class PatientImageAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<>();
    private final LayoutInflater mInflater;

    public PatientImageAdapter(Context c) {
        mInflater = LayoutInflater.from(c);

        mItems.add(new Item("Donald Trump", R.drawable.donald_trump_square));
        mItems.add(new Item("Margaret Thatcher", R.drawable.thatcher_square));
        mItems.add(new Item("George Clooney", R.drawable.george_clooney_square));
        mItems.add(new Item("Jake Gyllenhaal", R.drawable.jake_square));

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