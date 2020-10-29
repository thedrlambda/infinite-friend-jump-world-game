import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

public class Engine implements Runnable {

  private static final int FPS = 30;
  private static final int SLEEP = 1000/FPS;
  public static final int TILE_SIZE = 20;

  private Graphics windowG;
  private Graphics2D g2d;
  private Actor player;
  private boolean[][] map;
  private BufferedImage bi;
  private ArrayList<Actor> actors;

  public Engine() {
    JFrame window = new JFrame("Infinite friend jump world game");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setSize(520, 1058);
    window.setLocation(3330, 0);
    window.setVisible(true);
    window.addKeyListener(new KeyListener() {
      public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_D)
          player.move(true);
        if (e.getKeyCode() == KeyEvent.VK_A)
          player.move(false);
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
          player.jump();
      }
      public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_D
          || e.getKeyCode() == KeyEvent.VK_A)
          player.stop();
      }
      public void keyTyped(KeyEvent e){}
    });
    windowG = window.getGraphics();
    actors = new ArrayList<>();
    actors.add(player = new Actor(100, 100, false));
    actors.add(new Actor(350, 100, true));
    actors.add(new Actor(250, 100, true));
    actors.add(new Actor(350, 300, true));
    actors.add(new Actor(150, 300, true));
    actors.add(new Actor(20, 300, true));
    map = new boolean[30][30];
    platform(0, 10, 7, 5, false);
    platform(6, 13, 7, 2, true);
    platform(16, 13, 5, 2, true);
    platform(10, 5, 5, 1, true);
    platform(9, 7, 2, 1, false);
    platform(13, 18, 3, 1, false);
    platform(13, 15, 1, 1, false);
    platform(12, 19, 1, 1, true);
    platform(11, 21, 1, 1, true);
    platform(4, 23, 7, 1, true);
    platform(0, 20, 4, 5, true);
    platform(16, 22, 7, 1, true);
    platform(23, 15, 3, 7, false);
    platform(14, 23, 2, 2, false);
    bi = new BufferedImage(window.getSize().width, window.getSize().height, BufferedImage.TYPE_INT_ARGB);
    g2d = bi.createGraphics();
    new Thread(this).start();
  }

  private void platform(int x, int y, int width, int height, boolean lip) {
    for(int dy = 0; dy < height; dy++)
      for(int dx = 0; dx < width; dx++)
        map[y+dy][x+dx] = true;
    if (lip) {
      map[y - 1][x] = true;
      map[y - 1][x + width - 1] = true;
    }
  }

  public void run() {
    while (true) {
      long before = System.currentTimeMillis();
      update();
      draw(g2d);
      windowG.drawImage(bi, 0, 0, null);
      long after = System.currentTimeMillis();
      try { Thread.sleep(SLEEP - (after - before)); } catch (Exception e) {}
    }
  }

  public void update() {
    for (Actor a : actors) {
      a.update(map, player);
    }
  }

  public void draw(Graphics2D g) {
    g.clearRect(0, 0, 520, 1058);
    /*
    g.setColor(Color.RED);
    g.fillRect(100, 100, 100, 100);
    */
    g.setColor(Color.BLUE);
    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[y].length; x++) {
        if (map[y][x]) {
          g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
      }
    }

    for (Actor a : actors) {
      a.draw(g);
    }
  }

}