package main;

import imomushi.Apple;
import imomushi.Block;
import imomushi.Caterpillar;
import imomushi.Enemy;
import imomushi.MoveShape;
import imomushi.Section;
import imomushi.Shape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JFrame;


public class ImomushiMain extends JFrame implements ActionListener {
    
    public static final int FWIDTH = 800;
    public static final int FHEIGHT = 600;
    private int block_num = 3;
    
    public static Timer time;
    
    // ArrayList<Shape> mapBlock;
    ArrayList<MoveShape> poizon;
    MoveShape enemy;
    MoveShape enemy_2;
    MoveShape apple;
    static Caterpillar caterpillar;
    ArrayList<Section> sections;
    
    Random rand;
    
    int speed = 100;
    int time_add = 0;
    int time_up = 5000;

    public static void main(String[] args) {
        ImomushiMain imomushi = new ImomushiMain();
        imomushi.addKeyListener(caterpillar);
    }
    
    public ImomushiMain() {
        rand = new Random();
//        mapBlock = new ArrayList<Shape>();
        poizon = new ArrayList<MoveShape>();
        sections = new ArrayList<Section>();
        
        this.setSize(FWIDTH, FHEIGHT);
        this.setVisible(true);
        
        caterpillar = new Caterpillar(400, 300, 20, 0);
        this.add(caterpillar, BorderLayout.CENTER);
        this.setVisible(true);
        
        sections.add(new Section(380, 300, 20, 0, 400, 300));
        this.add(sections.get(0), BorderLayout.CENTER);
        this.setVisible(true);

        sections.add(new Section(360, 300, 20, 0, 380, 300));
        this.add(sections.get(1), BorderLayout.CENTER);
        this.setVisible(true);
        
        for (int x = 0, y = 0, w = 20, h = 20; y < getHeight(); y += h) {
            Shape leftBlock = new Block(x, y, w, h);
            this.add(leftBlock, BorderLayout.CENTER);
            this.setVisible(true);
        }
        
        for (int x = getWidth() - 20, y = 0, w = 20, h = 20; y < getHeight(); y += h) {
            Shape rightBlock = new Block(x, y, w, h);
            this.add(rightBlock, BorderLayout.CENTER);
            this.setVisible(true);
        }

        for (int x = 20, y = 0, w = 20, h = 20; x < getWidth() - 20; x += w) {
            Shape rightBlock = new Block(x, y, w, h);
            this.add(rightBlock, BorderLayout.CENTER);
            this.setVisible(true);
        }

        for (int x = 20, y = getHeight() - 40, w = 20, h = 20; x < getWidth() - 20; x += w) {
            Shape rightBlock = new Block(x, y, w, h);
            this.add(rightBlock, BorderLayout.CENTER);
            this.setVisible(true);
        }
        
//        for (int i = 0; i < block_num; i++) {
//            mapBlock.add(new Block(rand.nextInt(getWidth() - 100) + 20, rand.nextInt(getHeight() - 100) + 20, 40, 40));
//            this.add(mapBlock.get(i), BorderLayout.CENTER);
//            this.setVisible(true);
//        }
        for (int i = 0; i < block_num; i++) {
            poizon.add(new Apple(rand.nextInt(getWidth() - 100) + 20, rand.nextInt(getHeight() - 100) + 20, 40, new Color(135, 0, 204)));
            this.add(poizon.get(i), BorderLayout.CENTER);
            this.setVisible(true);
        }
        
        enemy = new Enemy(rand.nextInt(getWidth()), rand.nextInt(getHeight()), 20, rand.nextInt(19));
        this.add(enemy, BorderLayout.CENTER);
        this.setVisible(true);
        
        enemy_2 = new Enemy(rand.nextInt(getWidth()), rand.nextInt(getHeight()), 20, rand.nextInt(19));
        this.add(enemy_2, BorderLayout.CENTER);
        this.setVisible(true);
        
        apple = new Apple(rand.nextInt(getWidth() - 100) + 20, rand.nextInt(getHeight() - 100) + 20, 20, Color.RED);
        this.add(apple, BorderLayout.CENTER);
        this.setVisible(true);
        
        time = new Timer(speed, this);
        time.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        caterpillar.move();
        
        if (caterpillar.getShapeX() < 39) {
            caterpillar.setDirection(0);
            caterpillar.decrease_section(sections);
        }
        else if (caterpillar.getShapeX() > getWidth() - 41) {
            caterpillar.setDirection(180);
            caterpillar.decrease_section(sections);
        }
        else if (caterpillar.getShapeY() < 39) {
            caterpillar.setDirection(90);
            caterpillar.decrease_section(sections);
        }
        else if (caterpillar.getShapeY() > getHeight() - 61) {
            caterpillar.setDirection(270);
            caterpillar.decrease_section(sections);
        }
        
        for (Section section : sections) {
            section.move();
        }
        for (int i = 0; i < sections.size(); i++) {
            if (i == 0) {
                sections.get(i).setFollowX(caterpillar.getShapeX());
                sections.get(i).setFollowY(caterpillar.getShapeY());
            }
            else {
                sections.get(i).setFollowX(sections.get(i - 1).getShapeX());
                sections.get(i).setFollowY(sections.get(i - 1).getShapeY());
            }
        }
        
        enemy.setSpeed(rand.nextInt(19));
        enemy.move();
        for (int i = 0; i < block_num; i++) {
            while(enemy.collision_detection(poizon.get(i)) || enemy.collision_detection(apple)) {
                enemy.setSpeed(rand.nextInt(19));
                enemy.move();
            }
        }
        enemy_2.setSpeed(rand.nextInt(19));
        enemy_2.move();
        for (int i = 0; i < block_num; i++) {
            while(enemy_2.collision_detection(poizon.get(i)) || enemy_2.collision_detection(apple)) {
                enemy_2.setSpeed(rand.nextInt(19));
                enemy_2.move();
            }
        }
        
        if (apple.collision_detection(caterpillar)) {
            Section newSection = new Section(sections.get(sections.size() - 1).getShapeX(),
                    sections.get(sections.size() - 1).getShapeY(), 20, 0,
                    sections.get(sections.size() - 1).getShapeX(),
                    sections.get(sections.size() - 1).getShapeY());
            this.add(newSection, BorderLayout.CENTER);
            this.setVisible(true);
            sections.add(newSection);
        }

        for (MoveShape poizonApple : poizon) {
            if (poizonApple.collision_detection(caterpillar))
                caterpillar.decrease_section(sections);
        }
        
        if (enemy.collision_detection(caterpillar)) {
            caterpillar.dying();
        }
        for (Section section : sections) {
            if (enemy.collision_detection(section)) {
                caterpillar.dying();
            }
        }

        if (enemy_2.collision_detection(caterpillar)) {
            caterpillar.dying();
        }
        for (Section section : sections) {
            if (enemy_2.collision_detection(section)) {
                caterpillar.dying();
            }
        }
        time_add += speed;
        if (time_add % 10000 == 0) {
            caterpillar.decrease_section(sections);
        }
        repaint();
        if (sections.size() == 0) {
            caterpillar.dying();
        }
    }
}
