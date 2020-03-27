package com.personal.android.wallpaper.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.adapters.PhotoAdapter;
import com.personal.android.wallpaper.models.Collection;
import com.personal.android.wallpaper.models.Photo;
import com.personal.android.wallpaper.webServices.ApiInterface;
import com.personal.android.wallpaper.webServices.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {
    private final String TAG = CollectionFragment.class.getSimpleName();
    @BindView(R.id.collection_fragment_username)
    TextView username;
    @BindView(R.id.collection_fragment_description)
    TextView description;
    @BindView(R.id.collection_fragment_userAvatar)
    CircleImageView userAvatar;
    @BindView(R.id.collection_fragment_title)
    TextView title;

    @BindView(R.id.collections_fragment_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.collectionn_fragment_recyclerView)
    RecyclerView recyclerView;

    private List<Photo> photos = new ArrayList<>();
    private PhotoAdapter photosAdapter;

    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_collection_fragment,container,false);
        unbinder = ButterKnife.bind(this, view);

        //Recycle view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        photosAdapter = new PhotoAdapter(getActivity(),photos);
        recyclerView.setAdapter(photosAdapter);

        Bundle bundle = getArguments();
        int collectionId = bundle.getInt("collectionId");
        showprogressBar(true);
        getInformationOfCollection(collectionId);
        getPhotosOfCollection(collectionId);

        return view;
    }

    public void getInformationOfCollection(int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Collection> call = apiInterface.getInformationOfCollection(collectionId);
        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if(response.isSuccessful()){
                    Collection collection = response.body();
                    title.setText(collection.getTitle());
                    description.setText(collection.getDescription());
                    username.setText(collection.getUser().getUsername());
                    Glide
                            .with(getActivity())
                            .load(collection.getUser().getProfileImage().getSmall())
                            .into(userAvatar);
                } else {
                    Log.e(TAG,response.message());
                }
                showprogressBar(false);
            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                showprogressBar(false);
            }
        });
    }

    public void getPhotosOfCollection(int collectionId) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotosOfCollection(collectionId);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photos.addAll(response.body());
                    photosAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void showprogressBar(boolean ishow){
        if(ishow){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
