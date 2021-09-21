import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 23957328;
    public static final int WIDTH = 1080, HEIGHT = WIDTH/12 * 9;
    private Thread thread;
    private boolean running = false;
    private Tile[][] grid;
    private int length = 5;
    private Scanner keyboard = new Scanner(System.in);
    //private Tile test;

    public Game(){

        Color lifeC = Color.WHITE;
        Color dead = Color.BLACK;
        new Window(WIDTH, HEIGHT, "Game of Life", this);
        grid = new Tile[(WIDTH-32)/length][(HEIGHT-60)/length];

        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = new Tile(i*length, j*length, length, lifeC, dead);
            }
        }

        //this.addKeyListener(new KeyInput(grid));
        //test = new Tile(50, 50, length, lifeC);

    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;

    }

    public synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000/ amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            boolean cap;
//            if(delta >= 1) cap = true;
//            else cap = false;
            while(delta >= 1){ //ticks amountOfTicks per second
                tick();
                delta--;
            }
            if(running) { // renders if running
                render();
                frames++;
            }

            if(System.currentTimeMillis() - timer > 1000){ // every second prints out FPS
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();

    }

    public void tick(){
        int[][] numsA = new int[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[0].length; j++){
                numsA[i][j] = numAround(grid, grid[i][j]);
            }
        }
        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[0].length; j++){
                if(change(grid[i][j], numsA[i][j])) {
                    grid[i][j].tick();
                }
            }
        }

    }

    public void render(){
        this.requestFocus();
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, WIDTH, HEIGHT);

        for(int i = 0; i < grid.length; i ++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j].render(g);
            }
        }

        //test.render(g);




        g.dispose();
        bs.show();
    }
   // returns true if tile should change states
    public int numAround(Tile[][] grid, Tile tile){
        int numAround = 0;
        int tileX =  tile.getX()/length;
        int tileY = tile.getY()/length;
        for(int i = -1; i <= 1; i ++){
            for(int j = -1; j <= 1; j++){
                if(i!= 0 || j != 0){
                    int numX = (tileX+i+grid.length)%grid.length;
                    int numY = (tileY+j+grid[0].length)%grid[0].length;
                    if(grid[numX][numY].isAlive()) //num%grid.length
                        numAround++;
                }

            }
        }
        return numAround;

    }

    public boolean change(Tile tile, int numAround){
        if(tile.isAlive()){   // check if alive
            if(numAround < 2 || numAround > 3)  //kills cell if it has less than
                return true;                    //2 neighbors or more than 3 neihbors
            else
                return false;
        }
        else{
            if(numAround == 3) //live cell if it has 3 neighbors
                return true;
            else
                return false;
        }
    }

    public static void main(String args[]){



        new Game();


    }
}
