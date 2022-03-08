package com.nmt.physicaffectlivewallpaper;



import java.util.Random;

public class particle {
    private float x,y;
    private float radius;
    final private Random random=new Random();
    private boolean ischose;
    private float velocity;
    private final float randomdirection;
    private float velocity_x;
    private float velogity_y;
    private int positionX;
    private int positionY;
    private int R,G,B;
    private float baseX,baseY;


    public void setR(int r) {
        R = r;
    }

    public void setG(int g) {
        G = g;
    }

    public void setB(int b) {
        B = b;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public particle()
    {

        this.radius= random.nextInt(36)+5;
        this.x=0;
        this.y=0;
        this.velocity=0;
        this.velocity_x=0;
        this.velogity_y=0;
        this.ischose=false;
        this.randomdirection=random.nextInt(99); // random number for chosing direction
        this.positionX=(int)Math.floor(x);
        this.positionY=(int) Math.floor(y);


    }

    public float getBaseX() {
        return baseX;
    }

    public void setBaseX(float baseX) {
        this.baseX = baseX;
    }

    public float getBaseY() {
        return baseY;
    }

    public void setBaseY(float baseY) {
        this.baseY = baseY;
    }

    public float getRadius()
    {
        return radius;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public boolean isIschose()
    {
        return ischose;
    }

    public void setIschose(boolean b)
    {
        this.ischose=b;
    }

    // Get position of particle
    public float getx()
    {
        return x;
    }
    public float gety()
    {
        return y;
    }

    public void setx(float x)
    {
        this.x=x;
    }
    public void sety(float y)
    {
        this.y=y;
    }
    // Random position of particle
    public void randomspeed()
    {
        if (radius>=5)
            velocity= random.nextInt(6)+5;
        else
            velocity= random.nextInt(20)+11;

        this.velocity_x=velocity;
        this.velogity_y=velocity;
    }

    public void ranspeed()
    {
        this.velocity= random.nextFloat()*3+1;
        this.velogity_y=velocity;
    }


    public void initialize(int a,int b)
    {


        this.x= random.nextInt(a-150-150+1)+150;
        this.y= random.nextInt(b-150-150+1)+150;

    }

    public float getVelocity()
    {
        return velocity;
    }

    public float getVelocity_x()
    {
        return velocity_x;
    }
    public float getVelogity_y()
    {
        return velogity_y;
    }
    public void setVelocity_x()
    {
        this.velocity_x=-velocity_x;
    }
    public void setVelogity_y()
    {
        this.velogity_y=-velogity_y;
    }


    public void move(float x,float y) {

        if (this.randomdirection >= 0 && this.randomdirection < 25) {
            this.x = this.x + x;
            this.y = this.y + y;
        } else if (this.randomdirection >= 25 && this.randomdirection < 50) {
            this.x = this.x - x;
            this.y = this.y + y;
        } else if (this.randomdirection >= 50 && this.randomdirection < 75) {
            this.x = this.x - x;
            this.y = this.y - y;
        } else if (this.randomdirection >= 75 && this.randomdirection < 100) {
            this.x = this.x + x;
            this.y = this.y - y;
        }
    }

    public void setRadius(float x)
    {
        this.radius=x;
    }
}

