import java.awt.*;
import java.util.Random;

public class Tile {
    private int x, y, length;
    private Boolean isAlive;
    private Color color, dead;
    private Random r = new Random();

    public Tile(int x, int y, int length, Color color, Color dead){
        this.x = x;
        this.y = y;
        this.length = length;
        this.color = color;
        this.dead = dead;
        isAlive = (int) (Math.random()*10) ==0;


    }

    public void tick(){
        isAlive = !isAlive;

    }

    public void render(Graphics g){
        if(isAlive)
            g.setColor(color);
        else
            g.setColor(dead);
        g.fillRect(x,y,length,length);
    }

    public boolean isAlive(){
        return isAlive;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setState(){
        isAlive = true;
    }

}
