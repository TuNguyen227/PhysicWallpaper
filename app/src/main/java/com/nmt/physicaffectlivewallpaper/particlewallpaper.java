package com.nmt.physicaffectlivewallpaper;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.util.ArrayList;

public class particlewallpaper extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new wallpaperengine();
    }

    private class wallpaperengine extends Engine{
        private int wallpaper_width;
        private int wallpaper_height;
        private int num_particle;
        private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint linepaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Handler handler=new Handler();
        private boolean isVisible=true;
        private Bitmap background;
        private float a,b;
        private final int mindistance=500;
        private int ocpacity;
        private final ArrayList<particle> particleArrayList=new ArrayList<>(num_particle);
        private final SharedPreferences sharedPreferences;
        private int Red,Green,Blue;
        private int lRed,lGreen,lBlue;
        private boolean isTouch;

        private final Runnable drawFrames=new Runnable() {
            @Override
            public void run() {

                draw();
            }
        };

        public wallpaperengine()
        {
            Point size = new Point();
            WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            display.getRealSize(size);
            wallpaper_height= size.y;
            wallpaper_width= size.x;



            sharedPreferences= PreferenceManager.getDefaultSharedPreferences(particlewallpaper.this);

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            num_particle=Integer.parseInt(sharedPreferences.getString(Preferences.numofparticles,"5"));
            isTouch=sharedPreferences.getBoolean(Preferences.isTouch,true);
            String encodeimage=sharedPreferences.getString(Preferences.image,null);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            getParticleColor();
            Log.d("checkkmau", Red +" "+ Green +" "+ Blue);
            paint.setColor(Color.rgb(Red,Green,Blue));
            getLineleColor();
            Log.d("checkkmau1", lRed +" "+ lGreen +" "+ lBlue);

            linepaint.setStrokeWidth(5);
            linepaint.setColor(Color.rgb(lRed,lGreen,lBlue));

            if (encodeimage!=null)
            {
                byte[] b= Base64.decode(encodeimage,Base64.DEFAULT);
                Bitmap newimagebitmap=BitmapFactory.decodeByteArray(b,0,b.length);
                background=BitmapFactory.decodeByteArray(b,0,b.length);
            }
            else {
                background=BitmapFactory.decodeResource(getResources(), R.drawable.background1);
            }

            background=Bitmap.createScaledBitmap(background,wallpaper_width,wallpaper_height,true);
            handler.post(drawFrames);
        }

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.d("pref",s);
                if (Preferences.numofparticles.equals(s)) {
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    particleArrayList.clear();
                    Log.d("ini", String.valueOf(particleArrayList.size()));
                    num_particle = Integer.parseInt(sharedPreferences.getString(Preferences.numofparticles, null));
                    initArray(wallpaper_width, wallpaper_height);

                    Log.d("ini", String.valueOf(particleArrayList.size()));
                }
                if (Preferences.image.equals(s)) {
                    String encodeimage = sharedPreferences.getString(s, null);
                    byte[] b = Base64.decode(encodeimage, Base64.DEFAULT);
                    Bitmap newimagebitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    background.recycle();
                    background = newimagebitmap;
                    background = Bitmap.createScaledBitmap(background, wallpaper_width, wallpaper_height, true);

                }
                if (Preferences.Red.equals(s) || Preferences.Green.equals(s) || Preferences.Blue.equals(s))
                {
                    Red=sharedPreferences.getInt(Preferences.Red, Integer.parseInt("5"));
                    Green=sharedPreferences.getInt(Preferences.Green, Integer.parseInt("5"));
                    Blue=sharedPreferences.getInt(Preferences.Blue, Integer.parseInt("5"));
                    paint.setColor(Color.rgb(Red,Green,Blue));
                }
                if (Preferences.lRed.equals(s) || Preferences.lGreen.equals(s) || Preferences.lBlue.equals(s))
                {
                    lRed=sharedPreferences.getInt(Preferences.lRed, Integer.parseInt("2"));
                    lGreen=sharedPreferences.getInt(Preferences.lGreen, Integer.parseInt("2"));
                    lBlue=sharedPreferences.getInt(Preferences.lBlue, Integer.parseInt("2"));
                    linepaint.setColor(Color.rgb(lRed,lGreen,lBlue));
                }
                if (Preferences.isTouch.equals(s))
                {
                    isTouch=sharedPreferences.getBoolean(Preferences.isTouch,false);
                }



            }
        };
        public void getParticleColor()
        {
            Red=sharedPreferences.getInt(Preferences.Red, Integer.parseInt("139"));
            Green=sharedPreferences.getInt(Preferences.Green, Integer.parseInt("43"));
            Blue=sharedPreferences.getInt(Preferences.Blue, Integer.parseInt("226"));

        }
        public void getLineleColor()
        {
            lRed=sharedPreferences.getInt(Preferences.lRed, Integer.parseInt("218"));
            lGreen=sharedPreferences.getInt(Preferences.lGreen, Integer.parseInt("112"));
            lBlue=sharedPreferences.getInt(Preferences.lBlue, Integer.parseInt("214"));

        }

        public void draw()
        {



            SurfaceHolder holder=getSurfaceHolder();
            Canvas canvas=null;
            try {
                if (isVisible)
                {
                    canvas=holder.lockCanvas();


                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawBitmap(background,0,0,null);
                    for (int i=0;i<particleArrayList.size();i++) {
                        paint.setShadowLayer(particleArrayList.get(i).getRadius()*2,0,0, Color.WHITE);
                        canvas.drawCircle(particleArrayList.get(i).getx(), particleArrayList.get(i).gety(), particleArrayList.get(i).getRadius(), paint);

                    }
                    for (int i=0;i<particleArrayList.size();i++) {
                        for (int j = i + 1; j < particleArrayList.size(); j++) {

                            a = particleArrayList.get(i).getx() - particleArrayList.get(j).getx();
                            b = particleArrayList.get(i).gety() - particleArrayList.get(j).gety();
                            ocpacity= (int) (255-(Math.sqrt(a * a + b * b)/mindistance)*255);
                            if ((Math.sqrt(a * a + b * b) < mindistance) ) {
                                linepaint.setColor(Color.argb(ocpacity,lRed,lGreen,lBlue));
                                canvas.drawLine(particleArrayList.get(i).getx(), particleArrayList.get(i).gety(), particleArrayList.get(j).getx(), particleArrayList.get(j).gety(), linepaint);
                            }


                        }
                    }

                    update();
                }
            } finally {
                if (canvas !=null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawFrames);
            if (isVisible)
                handler.postDelayed(drawFrames,4);
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            initArray(wallpaper_width,wallpaper_height);
            Log.d("hei",String.valueOf(wallpaper_height));
            isTouch=sharedPreferences.getBoolean(Preferences.isTouch,true);

            // set paint for drawing front-behind connection

            handler.post(drawFrames);

        }


        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            if(event.getAction()==MotionEvent.ACTION_DOWN && isTouch)
            {
                particle particle=new particle();
                particle.setx((int)event.getX());
                particle.sety((int)event.getY());
                particle.randomspeed();
                Log.d("touch", particle.getVelocity_x() +" "+ particle.getVelogity_y() +" "+ particle.getVelocity());
                particleArrayList.add(particle);

            }

        }



        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            this.isVisible=visible;
            if (isVisible) {

                handler.post(drawFrames);
            }
            else
            {

                handler.removeCallbacks(drawFrames);
            }

        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            this.isVisible=false;

            handler.removeCallbacks(drawFrames);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            this.wallpaper_width=width;
            this.wallpaper_height=height;
            isTouch=sharedPreferences.getBoolean(Preferences.isTouch,true);
            super.onSurfaceChanged(holder, format, width, height);
        }

        public void initArray(int x, int y)
        {
            for(int i=0;i<num_particle;i++)
            {
                particle Particle=new particle();
                Particle.randomspeed();
                Particle.initialize(x,y);
                particleArrayList.add(Particle);
            }
        }

        public void update()
        {
            for (int i=0;i<particleArrayList.size();i++)
            {

                // Check edge of the screen
                if(particleArrayList.get(i).getx()>(wallpaper_width-particleArrayList.get(i).getRadius()))
                    particleArrayList.get(i).setVelocity_x();

                if (particleArrayList.get(i).gety()>(wallpaper_height-particleArrayList.get(i).getRadius()))
                    particleArrayList.get(i).setVelogity_y();

                if (particleArrayList.get(i).getx()<particleArrayList.get(i).getRadius())
                    particleArrayList.get(i).setVelocity_x();

                if (particleArrayList.get(i).gety()<particleArrayList.get(i).getRadius())
                    particleArrayList.get(i).setVelogity_y();


                particleArrayList.get(i).move(particleArrayList.get(i).getVelocity_x(),particleArrayList.get(i).getVelogity_y());

            }
        }



    }

}
