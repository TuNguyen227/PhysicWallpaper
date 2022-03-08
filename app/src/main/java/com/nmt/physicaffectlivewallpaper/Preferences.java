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
import androidx.preference.SeekBarPreference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Preferences extends PreferenceActivity {
    public static String numofparticles="numofparticles";
    public static String image="getimage";
    public static String Red="Red";
    public static String Green="Green";
    public static String Blue="Blue";
    public static String lRed="lRed";
    public static String lGreen="lGreen";
    public static String lBlue="lBlue";
    public static String isTouch="isTouch";
    private static final int Pic_image=1;
    private SharedPreferences.OnSharedPreferenceChangeListener PreferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference imagePref=findPreference(image);

        PreferenceChangeListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals(numofparticles))
                {
                    Preference numofP=findPreference(s);
                    numofP.setSummary(sharedPreferences.getString(s,"")+ " Particles");
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                }
                if (s.equals(Red))
                {
                    Preference redvalue=findPreference(Red);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    redvalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(Green))
                {
                    Preference greenvalue=findPreference(Green);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    greenvalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(Blue))
                {
                    Preference bluevalue=findPreference(Blue);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    bluevalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(lRed))
                {
                    Preference lredvalue=findPreference(lRed);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    lredvalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(lGreen))
                {
                    Preference lgreenvalue=findPreference(lGreen);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    lgreenvalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(lBlue))
                {
                    Preference lbluevalue=findPreference(lBlue);
                    int a=sharedPreferences.getInt(s, Integer.parseInt("5"));
                    lbluevalue.setSummary(String.format("Value: %d",a));

                }
                if (s.equals(isTouch))
                {
                    Preference check=findPreference(isTouch);
                    boolean a=sharedPreferences.getBoolean(s,false);


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

                final Bitmap selectedImg=BitmapFactory.decodeStream(imageStream);

                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

                selectedImg.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] compresimag=byteArrayOutputStream.toByteArray();
                String encodeimage=Base64.encodeToString(compresimag,Base64.DEFAULT);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(image,encodeimage);
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

        Preference numofP=findPreference(numofparticles);
        numofP.setSummary(getPreferenceScreen().getSharedPreferences().getString(numofparticles,"")+ " Particles");
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(PreferenceChangeListener);
    }



}
