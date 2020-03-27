package com.personal.android.wallpaper.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.models.Photo;
import com.personal.android.wallpaper.utils.Functions;
import com.personal.android.wallpaper.utils.RealmController;
import com.personal.android.wallpaper.webServices.ApiInterface;
import com.personal.android.wallpaper.webServices.ServiceGenerator;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenPhotoActivity extends AppCompatActivity {
    private static final String TAG = FullScreenPhotoActivity.class.getSimpleName();
    @BindView(R.id.fullscreen_photo_photo)
    ImageView fullscreen_photo;
    @BindView(R.id.fullscreen_photo_username)
    TextView username;
    @BindView(R.id.fullscreen_photo_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.fullscreen_photo_fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.fullscreen_photo_fab_favourite)
    FloatingActionButton fabFavourite;
    @BindView(R.id.fullscreen_photo_fab_wallpaper)
    FloatingActionButton fabWallpaper;

    private Bitmap photoBitmap;

    private Unbinder unbinder;
    private Photo photo;

    @BindDrawable(R.drawable.ic_check_favourites)
    Drawable icFavourite;
    @BindDrawable(R.drawable.ic_favourited)
    Drawable icFavourited;
    private RealmController realmController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        String photoId = intent.getStringExtra("photoId");
        getPhoto(photoId);

        realmController = new RealmController();
        if (realmController.isPhotoExist(photoId)){
            fabFavourite.setImageDrawable(icFavourited);
        }
    }

    private void getPhoto(String id){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call = apiInterface.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()){
                    photo = response.body();
                    updateUi(photo);
                } else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void updateUi(Photo photo) {
        try {
            username.setText(photo.getUser().getUsername());
            Glide.with(FullScreenPhotoActivity.this)
                    .load(photo.getUser().getProfileImage().getSmall())
                    .into(userAvatar);
            Glide.with(FullScreenPhotoActivity.this)
                    .asBitmap()
                    .load(photo.getUrl().getFull())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            fullscreen_photo.setImageBitmap(resource);
                            photoBitmap = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fullscreen_photo_fab_favourite)
    public void setFabFavourite(){
        if(realmController.isPhotoExist(photo.getId())){
            realmController.deletePhoto(photo);
            fabFavourite.setImageDrawable(icFavourite);
            Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_LONG).show();
        } else {
            realmController.savePhoto(photo);
            fabFavourite.setImageDrawable(icFavourited);
            Toast.makeText(this, "Added to Favourite", Toast.LENGTH_LONG).show();
        }
        fabMenu.close(true);
    }

    @OnClick(R.id.fullscreen_photo_fab_wallpaper)
    public void setFabWallpaper(){
        if (photoBitmap != null){
            if(Functions.setWallpaper(FullScreenPhotoActivity.this,photoBitmap)){
                Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Wallpaper set fail ", Toast.LENGTH_SHORT).show();
            }
        }
        fabMenu.close(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
