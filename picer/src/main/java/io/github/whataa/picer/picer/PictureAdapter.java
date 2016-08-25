package io.github.whataa.picer.picer;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import io.github.whataa.finepic.R;
import io.github.whataa.picer.executor.PicLoader;

public class PictureAdapter extends BaseAdapter {

    private List<Picture> datas = new ArrayList<>();
    private int mode;
    private View.OnClickListener listener;

    public PictureAdapter(int mode, View.OnClickListener listener) {
        this.mode = mode;
        this.listener = listener;
    }

    public void notifyDataSetChanged(List<Picture> pics) {
        datas.clear();
        datas.addAll(pics);
        notifyDataSetChanged();
    }

    public String getItemPath(int i) {
        return datas.size() < i ? null : datas.get(i).getPath();
    }

    public void setWhichItemIsPreview(int i) {
        for (Picture picture : datas) {
            picture.setPriview(false);
        }
        datas.get(i).setPriview(true);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gridview, viewGroup, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
            holder.ivState.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {listener.onClick(view);}
            });
        } else {
            holder = (Holder) convertView.getTag();
        }
        Picture pic = (Picture) getItem(i);
        if (PicLoader.isInitial()) {
            PicLoader.instance().load(pic.getPath(), holder.ivPic);
        }
        if (mode <= 1) {
            holder.ivState.setVisibility(View.GONE);
        } else {
            holder.ivState.setVisibility(View.VISIBLE);
            if (pic.isChosen()) {
                holder.ivState.setImageResource(R.drawable.ic_check_box_black_26dp);
            } else {
                holder.ivState.setImageResource(R.drawable.ic_check_box_outline_blank_black_26dp);
            }
        }
        if (pic.isPriview()) {
            holder.ivPic.setAlpha(0.35f);
        } else {
            holder.ivPic.setAlpha(1f);
        }
        // don't forget to set Tag for holder.ivState.
        holder.ivState.setTag(i);
        return convertView;
    }

    private static class Holder {
        ImageView ivPic, ivState;

        Holder(View v) {
            ivPic = (ImageView) v.findViewById(R.id.item_girdview_pic);
            ivState = (ImageView) v.findViewById(R.id.item_gridview_state);
        }
    }
}
