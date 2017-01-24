package flappyBird;

import com.sun.xml.internal.ws.handler.HandlerException;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Created by abdullah on 1/22/2017.
 */
public class FlappyBird implements ActionListener, KeyListener {

    public static FlappyBird flappyBird;
    public final int WIDTH=800,HEIGHT=800;
    public Renderer renderer;

    public Rectangle bird;
    public ArrayList<Rectangle> column;
    public Random rand;

    public int ticks, yMotion, score;
    public boolean gameOver, started;

    public FlappyBird(){
        Timer timer = new Timer(20,this);
        JFrame jframe = new JFrame();

        renderer = new Renderer();
        rand = new Random();

        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH,HEIGHT);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird");
        jframe.setVisible(true);

        bird = new Rectangle(WIDTH/2 -10, HEIGHT/2 -10,20,20);
        column = new ArrayList<Rectangle>();

        addColums(true);
        addColums(true);
        addColums(true);
        addColums(true);

        timer.start();
    }

    public void addColums(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if(start) {
            column.add(new Rectangle(WIDTH + width + column.size() * 300, HEIGHT - height - 120, width, height));
            column.add(new Rectangle(WIDTH + width + (column.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            column.add(new Rectangle(column.get(column.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            column.add(new Rectangle(column.get(column.size() - 1).x , 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x,column.y,column.width,column.height);
    }

    public void jump(){
        if(gameOver){
            gameOver = false;

            bird = new Rectangle(WIDTH/2 -10, HEIGHT/2 -10,20,20);
            column.clear();
            yMotion=0;
            score=0;

            addColums(true);
            addColums(true);
            addColums(true);
            addColums(true);
        }

        if(!started){
            started=true;
        } else if(!gameOver){
            if(yMotion > 0){
                yMotion = 0;
            }

            yMotion -= 10;
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks++;

        if(started && !gameOver) {

            for (int i = 0; i < column.size(); i++) {
                Rectangle columns = column.get(i);
                columns.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < column.size(); i++) {
                Rectangle columns = column.get(i);

                if (columns.x + columns.width < 0) {
                    column.remove(columns);

                    if (columns.y == 0) {
                        addColums(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle columns : column) {
                if(bird.x + bird.width / 2 > columns.x + columns.width / 2 - 10 && bird.x + bird.width /2 < columns.x + columns.width /2 + 10 ){
                    score++;
                }
                if (columns.intersects(bird)) {
                    gameOver = true;
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
            /*if(bird.y + yMotion >= HEIGHT -120){
                bird.y = HEIGHT - 120 - bird.height;
            }*/
        }
        renderer.repaint();
    }

    public void repaint(Graphics g) {
        g.setColor(Color.cyan);
        g.fillRect(0,0,WIDTH,HEIGHT);

        g.setColor(Color.ORANGE);
        g.fillRect(0,HEIGHT-120,WIDTH,120);

        g.setColor(Color.GREEN);
        g.fillRect(0,HEIGHT-120,WIDTH,20);

        g.setColor(Color.RED);
        g.fillRect(bird.x,bird.y,bird.width,bird.height);

        for(Rectangle columns: column){
            paintColumn(g,columns);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", 1, 80));

        if(!started){
            g.drawString("Space to Start",WIDTH / 2 -250, HEIGHT / 2 -50);
        }

        if(gameOver){
            g.drawString("Game Over!",WIDTH / 2 -250, HEIGHT / 2 -50);
        }

        if(!gameOver && started){
            g.drawString(String.valueOf(score/2), WIDTH/2 - 25,100);
        }
    }

    public static void main(String[] args){
        flappyBird = new FlappyBird();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_SPACE)
            jump();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
