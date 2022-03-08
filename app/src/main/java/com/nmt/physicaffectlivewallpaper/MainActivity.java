package com.nmt.physicaffectlivewallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    private Button set_bg_btn;

    private int currentCatalog;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Catalog catalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //view= findViewById(R.id.viewid);

        set_bg_btn=findViewById(R.id.set_bg_btn);

        viewPager=findViewById(R.id.viewpaper);
        circleIndicator=findViewById(R.id.dot);
        catalog=new Catalog(this,getlistphoto());
        viewPager.setAdapter(catalog);






        circleIndicator.setViewPager(viewPager);
        catalog.registerDataSetObserver(circleIndicator.getDataSetObserver());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                currentCatalog=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        set_bg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                if (currentCatalog==0) {
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getApplication(), particlewallpaper.class));
                    startActivity(intent);
                }
                if (currentCatalog==1) {
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getApplication(), picturepixeleffect.class));
                    startActivity(intent);

                }
            }
        });

    }
    private List<Photo> getlistphoto()
    {
        List<Photo> list=new ArrayList<>();
        list.add(new Photo(R.raw.particlegif));
        list.add(new Photo(R.raw.picturegif));

        return list;
    }

}