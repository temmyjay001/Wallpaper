package com.personal.android.wallpaper.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.personal.android.wallpaper.R;
import com.personal.android.wallpaper.fragments.CollectionsFragment;
import com.personal.android.wallpaper.fragments.FavouriteFragment;
import com.personal.android.wallpaper.fragments.PhotoFragment;
import com.personal.android.wallpaper.utils.Functions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class  MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottomNav)
    BottomNavigationView bottomNavigationView;

    Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        unbinder = ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Functions.changeMainFragment(MainActivity.this, new PhotoFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_photos:
                fragment = new PhotoFragment();
                break;

            case R.id.navigation_collections:
                fragment = new CollectionsFragment();
                break;

            case R.id.navigation_favourites:
                fragment = new FavouriteFragment();
                break;
        }
        Functions.changeMainFragment(MainActivity.this,fragment);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
