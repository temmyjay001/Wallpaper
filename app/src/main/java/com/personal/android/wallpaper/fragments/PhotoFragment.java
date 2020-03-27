package com.personal.android.wallpaper.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.adapters.PhotoAdapter;
import com.personal.android.wallpaper.models.Photo;
import com.personal.android.wallpaper.webServices.ApiInterface;
import com.personal.android.wallpaper.webServices.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotoFragment extends Fragment {
    private final String TAG = PhotoFragment.class.getSimpleName();
    @BindView(R.id.photo_fragment_progressBar)
    ProgressBar progressbar;
    @BindView(R.id.photo_fragment_recyclerview)
    RecyclerView recylerView;

    private PhotoAdapter photoAdapter;
    private List<Photo> photos = new ArrayList<>();

    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        //RECYLCERVIEW
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recylerView.setLayoutManager(linearLayoutManager);

        photoAdapter = new PhotoAdapter(getActivity(),photos);
        recylerView.setAdapter(photoAdapter);

        showProgressBar(true);
        getPhotos();

        return view;
    }

    public void getPhotos(){
        try {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<List<Photo>> call = apiInterface.getPhotos();
            call.enqueue(new Callback<List<Photo>>() {
                @Override
                public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                    if (response.isSuccessful()) {
                        photos.addAll(response.body());
                        photoAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Fail: " + response.message());
                    }
                    showProgressBar(false);
                }

                @Override
                public void onFailure(Call<List<Photo>> call, Throwable t) {
                    Log.e(TAG, "Fail: " + t.getMessage());
                    showProgressBar(false);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showProgressBar(boolean isShow){
        if(isShow){
            progressbar.setVisibility(View.VISIBLE);
            recylerView.setVisibility(View.GONE);
        } else {
            progressbar.setVisibility(View.GONE);
            recylerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
