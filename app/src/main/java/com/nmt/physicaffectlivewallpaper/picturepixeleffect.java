package com.nmt.physicaffectlivewallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import java.util.Random;

import java.util.ArrayList;

public class picturepixeleffect extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new picturepixeleffectengine();

    }
    public class picturepixeleffectengine extends Engine{
        private boolean isvisible;
        private final WindowManager windowManager=(WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        private final Point size=new Point();
        private final int numofparticle=50;
        private final Display display=windowManager.getDefaultDisplay();
        private final int wallpaper_height;
        private final int wallpaper_width;
        private final ArrayList<particle> particleArrayList=new ArrayList<>();
        private final ArrayList<particle> particleArrayList2=new ArrayList<>();
        private final Paint ppaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private Bitmap background;
        private float TouchX,TouchY;
        private final Boolean flag=false;
        private int indexW,indexH;
        private Bitmap heart;
        private final SharedPreferences sharedPreferences;
        private int heartmove=0;
        private final float angle=5;
        private final Matrix matrix;
        private Thread drawthread;
        private Bitmap happywomenday;
        private Bitmap heartrotate;
        private boolean ischange=false;
       private final Handler handler=new Handler();
       private final Thread drawframe=new Thread(new Runnable() {
           @Override
           public void run() {
               draw();
           }
       });

       public picturepixeleffectengine()
       {
           display.getRealSize(size);
           wallpaper_width=size.x;
           wallpaper_height=size.y;
           matrix=new Matrix();
           heart=BitmapFactory.decodeResource(getResources(),R.drawable.heart);
            heart=Bitmap.createScaledBitmap(heart,400,400,true);
           BitmapFactory.Options options=new BitmapFactory.Options();
           options.inPreferredConfig= Bitmap.Config.ARGB_8888;
           sharedPreferences= PreferenceManager.getDefaultSharedPreferences(picturepixeleffect.this);
           sharedPreferences.registerOnSharedPreferenceChangeListener(listener_changepicture);
            happywomenday=BitmapFactory.decodeResource(getResources(),R.drawable.happywomenday);
            happywomenday=Bitmap.createScaledBitmap(happywomenday,500,500,true);
           loadbackground();

           indexW=(wallpaper_width-background.getWidth())/2;
           indexH=(wallpaper_height-background.getHeight())/2;
           loadhappywomenday();
           getbitmappixel();

           handler.post(drawframe);
       }

        SharedPreferences.OnSharedPreferenceChangeListener listener_changepicture=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (PictureEffectSetting.picture.equals(key))
                {
                    String encodeimage=sharedPreferences.getString(key,null);
                    byte[] b= Base64.decode(encodeimage,Base64.DEFAULT);
                    Bitmap newimagebitmap=BitmapFactory.decodeByteArray(b,0,b.length);
                    background.recycle();
                    background=newimagebitmap;
                    background=Bitmap.createScaledBitmap(background,500,500,false);

                    indexW=(wallpaper_width-background.getWidth())/2;
                    indexH=(wallpaper_height-background.getHeight())/2;
                    particleArrayList.clear();
                    getbitmappixel();

                }
            }
        };

       public void loadbackground()
       {
           String encodeimage=sharedPreferences.getString(PictureEffectSetting.picture,null);
           if (encodeimage!=null)
           {
               byte[] b= Base64.decode(encodeimage,Base64.DEFAULT);
               Bitmap newimagebitmap=BitmapFactory.decodeByteArray(b,0,b.length);
               background=BitmapFactory.decodeByteArray(b,0,b.length);
               background=Bitmap.createScaledBitmap(background,500,500,false);
               ischange=false;
               heartmove=0;
           }
           else {
               background=BitmapFactory.decodeResource(getResources(),R.drawable.yuamikami);
               background=Bitmap.createScaledBitmap(background,500,500,false);

           }
       }

       public void getbitmappixel()
       {
           for (int i=0;i<happywomenday.getWidth();i+=4)
           {

               for (int j=0;j<background.getHeight();j+=4)
               {
                   int color=background.getPixel(i,j);
                    int R=(color & 0xff0000)>>16;
                    int G=(color&0x00ff00)>>8;
                    int B=(color&0x0000ff);

                        particle particle=new particle();
                        particle.setx(indexW+i);
                        particle.sety(indexH+j);
                        particle.setR(R);
                        particle.setG(G);
                        particle.setB(B);
                        particle.setRadius(5);
                        particle.setBaseX(particle.getx());
                        particle.setBaseY(particle.gety());
                        particleArrayList.add(particle);




               }

           }


       }

       public void loadhappywomenday()
       {
           for (int i=0;i<happywomenday.getWidth();i+=4)
           {

               for (int j=0;j<happywomenday.getHeight();j+=4)
               {
                   int color=happywomenday.getPixel(i,j);
                   int R=(color & 0xff0000)>>16;
                   int G=(color&0x00ff00)>>8;
                   int B=(color&0x0000ff);

                       particle particle=new particle();
                       particle.setx(indexW+i);
                       particle.sety(indexH+j);
                       particle.setR(R);
                       particle.setG(G);
                       particle.setB(B);
                       particle.setRadius(5);
                       particle.setBaseX(particle.getx());
                       particle.setBaseY(particle.gety());
                       particleArrayList2.add(particle);




               }

           }
       }

       public Double caculateBrightness(int R,int G,int B)
       {
           return Math.sqrt((R*R*0.299+G*G*0.587+B*B*0.114))/100;
       }

       public void draw()
       {
           SurfaceHolder surfaceHolder=getSurfaceHolder();
           Canvas canvas=null;
           try {
               if (isvisible)
               {
                   canvas=surfaceHolder.lockCanvas();
                   canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                   Canvas finalCanvas = canvas;
                   drawthread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                drawframe.join();
                                update();
                                matrix.postRotate(angle);
                                heartrotate=Bitmap.createBitmap(heart,0,0,heart.getWidth(),heart.getHeight(),matrix,true);
                                finalCanvas.drawBitmap(heartrotate,(wallpaper_width-400)/2,heartmove,null);

                                for (particle p:particleArrayList)
                                {
                                    ppaint.setColor(Color.rgb(p.getR(),p.getG(),p.getB()));
                                    finalCanvas.drawCircle(p.getx(),p.gety(),p.getRadius(),ppaint);
                                }


                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }

                        }
                    });
                    drawthread.run();



               }
           } finally {
               if (canvas!=null)
                   surfaceHolder.unlockCanvasAndPost(canvas);
           }

           handler.removeCallbacks(drawframe);
           Log.d("lllllllllll", TouchX +" "+ TouchY);
           handler.postDelayed(drawframe,5);

       }

       public void update()
       {
           for (int i=0;i<particleArrayList.size();i++)
           {
               float a=particleArrayList.get(i).getx()-(wallpaper_width-400)/2;
               float b=particleArrayList.get(i).gety()-heartmove-200;
               float distance=(float) Math.sqrt(a*a+b*b);
               float scale=distance/500;
               if (distance<500)
               {

                   particleArrayList.get(i).sety(particleArrayList.get(i).gety()+b*10*scale);
                   particleArrayList.get(i).setx(particleArrayList.get(i).getx()+a*10*scale);
                   particleArrayList.get(i).setR(particleArrayList2.get(i).getR());
                   particleArrayList.get(i).setG(particleArrayList2.get(i).getG());
                   particleArrayList.get(i).setB(particleArrayList2.get(i).getB());
                   Log.d("check", i + " " + particleArrayList.get(i).getx() + " " + particleArrayList.get(i).gety());
               }
               else
               {
                   if (particleArrayList.get(i).getx()!=particleArrayList.get(i).getBaseX())
                   {
                       float dc=particleArrayList.get(i).getx()-particleArrayList.get(i).getBaseX();
                       particleArrayList.get(i).setx(particleArrayList.get(i).getx()-dc/10);

                   }
                   if (particleArrayList.get(i).gety()!=particleArrayList.get(i).getBaseY())
                   {
                       float dc=particleArrayList.get(i).gety()-particleArrayList.get(i).getBaseY();
                       particleArrayList.get(i).sety(particleArrayList.get(i).gety()-dc/10);

                   }

               }
           }

           if (heartmove+heart.getHeight()>wallpaper_height) {
               heartmove = 0;
               ischange=true;
           }

               heartmove=heartmove+20;




           Log.d("checkhe",String.valueOf((heartmove+heart.getHeight())));
       }



        @Override
        public void onDestroy() {
            super.onDestroy();
            this.isvisible=false;
            handler.removeCallbacks(drawframe);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            this.isvisible=visible;
            if (isvisible)
            {
                handler.post(drawframe);
            }
            else
                handler.removeCallbacks(drawframe);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            ppaint.setStyle(Paint.Style.FILL_AND_STROKE);
            //init(wallpaper_width,wallpaper_height);
            ppaint.setColor(Color.WHITE);
            Log.d("debug",String.valueOf(wallpaper_width));
            isvisible=true;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.isvisible=false;
            handler.removeCallbacks(drawframe);
        }
        public void init(int x,int y)
        {
            for (int i=0;i<numofparticle;i++)
            {
                particle particle=new particle();
                particle.setRadius(5);
                particle.ranspeed();

                particle.initialize(wallpaper_width,wallpaper_height);
                particle.setBaseX(particle.getx());
                particle.setBaseY(particle.gety());
                Log.d("sssssssssssss", particle.getVelocity() +"  "+ particle.getRadius());

                particleArrayList.add(particle);
            }
        }


    }




}
