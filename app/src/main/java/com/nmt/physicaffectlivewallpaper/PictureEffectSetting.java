package com.nmt.physicaffectlivewallpaper;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PictureEffectSetting extends PreferenceActivity {

    public static String picture="picture";
    private static final int Pic_image=1;
    private SharedPreferences.OnSharedPreferenceChangeListener PreferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pictureaffectpref);

        Preference imagePref=findPreference(picture);

        PreferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals(picture) )
                {
                    Preference numofP=findPreference(s);
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                }

            }
        };

        imagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent();
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select Image"),Pic_image);
                return true;
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Pic_image && resultCode==RESULT_OK && data!=null)
        {

            final Uri urimage=data.getData();

            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(urimage);

                final Bitmap selectedImg= BitmapFactory.decodeStream(imageStream);

                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(PictureEffectSetting.this);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

                selectedImg.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] compresimag=byteArrayOutputStream.toByteArray();
                String encodeimage= Base64.encodeToString(compresimag,Base64.DEFAULT);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(picture,encodeimage);
                editor.apply();
                Log.d("check",encodeimage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(PreferenceChangeListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(PreferenceChangeListener);
    }


}
