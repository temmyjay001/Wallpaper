package com.personal.android.wallpaper.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.adapters.CollectionAdapter;
import com.personal.android.wallpaper.models.Collection;
import com.personal.android.wallpaper.utils.Functions;
import com.personal.android.wallpaper.webServices.ApiInterface;
import com.personal.android.wallpaper.webServices.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CollectionsFragment extends Fragment {
    private final String TAG = Collection.class.getSimpleName();
    @BindView(R.id.collections_fragment_gridview)
    GridView gridView;
    @BindView(R.id.collections_fragment_progressBar)
    ProgressBar progressBar;

    private CollectionAdapter collectionAdapter;
    private List<Collection> collections = new ArrayList<>();

    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        collectionAdapter = new CollectionAdapter(getActivity(), collections);
        gridView.setAdapter(collectionAdapter);

        showprogressBar(true);
        getCollections();
        return view;
    }

    @OnItemClick(R.id.collections_fragment_gridview)
    public void setGridView(int position){
        Collection collection = collections.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("collectionId",collection.getId());
        CollectionFragment collectionFragment = new CollectionFragment();
        collectionFragment.setArguments(bundle);
        Functions.changeMainFragmentWithBack(getActivity(),collectionFragment);
    }

    public void getCollections(){
        try {
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<List<Collection>> call = apiInterface.getCollections();
            call.enqueue(new Callback<List<Collection>>() {
                @Override
                public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                    if (response.isSuccessful()) {
                        collections.addAll(response.body());
                        collectionAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, response.message());
                    }
                    showprogressBar(false);
                }

                @Override
                public void onFailure(Call<List<Collection>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    showprogressBar(false);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showprogressBar(boolean ishow){
        if(ishow){
            progressBar.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
