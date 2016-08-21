package io.github.whataa.picer.picer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.whataa.finepic.R;


public class FolderAdapter extends BaseAdapter {

    private List<Folder> datas = new ArrayList<>();

    public String getCurrentFolderPath(int i) {
        return datas.size() < i ? null : datas.get(i).getPath();
    }

    public void notifyDataSetChanged(List<Folder> folders) {
        datas.clear();
        datas.addAll(folders);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_popupwindow, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Folder folder = (Folder) getItem(i);
        holder.tvName.setText(folder.getName());
        if (folder.isCurrent()) {
            holder.tvName.setTextColor(Color.GREEN);
        } else {
            holder.tvName.setTextColor(viewGroup.getContext().getResources().getColor(R.color.colorFont));
        }
        return view;
    }

    private static class Holder {
        TextView tvName;

        Holder(View v) {
            tvName = (TextView) v.findViewById(R.id.item_popupwindow_text);
        }
    }
}
