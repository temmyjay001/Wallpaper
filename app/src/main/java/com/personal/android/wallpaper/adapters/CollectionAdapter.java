package com.personal.android.wallpaper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.models.Collection;
import com.personal.android.wallpaper.utils.SquareImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionAdapter extends BaseAdapter {
    private Context context;
    private List<Collection> collections;

    public CollectionAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int i) {
        return collections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return collections.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.collection_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ButterKnife.bind(this,view);
        Collection collection = collections.get(i);
        if (collection.getTitle() != null){
            viewHolder.title.setText(collection.getTitle());
        }
        viewHolder.totalPhotos.setText(String.valueOf(collection.getTotalPhotos()) + " Photo(s)");

        Glide
                .with(context)
                .load(collection.getCoverPhotos().getUrl().getRegular())
                .into(viewHolder.CollectionPhoto);

        return view;
    }

    static class ViewHolder{
        @BindView(R.id.collection_item_title)
        TextView title;
        @BindView(R.id.collection_item_total_photos)
        TextView totalPhotos;
        @BindView(R.id.collections_item_photo)
        SquareImage CollectionPhoto;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
