package com.personal.android.wallpaper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.adapters.PhotoAdapter;
import com.personal.android.wallpaper.models.Photo;
import com.personal.android.wallpaper.utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FavouriteFragment extends Fragment {
    @BindView(R.id.favourite_fragment_notification)
    TextView notification;
    @BindView(R.id.favourite_fragment_recyclerview)
    RecyclerView recyclerView;

    private PhotoAdapter photosAdapter;
    private List<Photo> photos = new ArrayList<>();

    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);
        unbinder = ButterKnife.bind(this,view);
        //Recycler View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        photosAdapter = new PhotoAdapter(getActivity(),photos);
        recyclerView.setAdapter(photosAdapter);

        getPhotos();

        return view;
    }

    public void getPhotos(){
        RealmController realmController = new RealmController();
        photos.addAll(realmController.getPhotos());
        if(photos.size() == 0){
            notification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            photosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
