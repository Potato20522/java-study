package com.potato.tank;

import java.awt.*;

public class Bullet {
    private static final int SPEED = 10;
    private static int WIDTH=5, HEIGHT = 5;
    private int x, y;
    private Dir dir;

    public Bullet(int x, int y, Dir dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x,y,WIDTH,HEIGHT);
        move();
    }
    private void move() {
        switch (dir){
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }
    }
}
