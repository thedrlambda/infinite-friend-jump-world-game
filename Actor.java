import java.awt.*;
import javax.swing.*;
public class Actor {

  public static final float FRIEND_DISTANCE = 0.2f;
  public static final float MAX_SPEED = 3;
  public static final int LEFT = -1;
  public static final int STOP = 0;
  public static final int RIGHT = 1;
  private float x, y;
  private float velX, velY;
  private int movement;
  private boolean ai;
  private boolean friend;

  public Actor(int x, int y, boolean ai) {
    this.x = x;
    this.y = y;
    this.ai = ai;
    velX = (float)Math.random() * MAX_SPEED;
  }

  public void draw(Graphics2D g) {
    if(!ai) 
      g.setColor(Color.MAGENTA);
    else if(friend)
      g.setColor(Color.GREEN);
    else
      g.setColor(Color.GRAY);
    g.fillRect(Math.round(x) - Engine.TILE_SIZE / 2, Math.round(y) - 2 * Engine.TILE_SIZE, Engine.TILE_SIZE, 2 * Engine.TILE_SIZE);
  }

  public void update(boolean[][] map, Actor player) {
    if (Math.abs(x - player.x) < FRIEND_DISTANCE && Math.abs(y - player.y) < FRIEND_DISTANCE)
      friend = true;
    if (velY == 0) {
      if (ai) {
        if (Math.random() < 0.01)
          jump();
      } else {
      velX += ((MAX_SPEED * movement) - velX) / 3;
      if (velX > MAX_SPEED)
        velX = MAX_SPEED;
      if (velX < -MAX_SPEED)
        velX = -MAX_SPEED;
      }
    }

    x += velX;
    y += velY;
    velY += 1;
    checkCollisionVertical(0, map);
    checkCollisionVertical(Engine.TILE_SIZE, map);

    checkCollisionHorizontal(true, map);
    checkCollisionHorizontal(false, map);
  }

  private void checkCollisionHorizontal(boolean right, boolean[][] map){
    int my1 = (int)(y - Engine.TILE_SIZE / 2) / Engine.TILE_SIZE;
    int my2 = (int)(y - 3 * Engine.TILE_SIZE / 2) / Engine.TILE_SIZE;
    int mx = (int)(x + (right ? 1 : -1) * Engine.TILE_SIZE / 2) / Engine.TILE_SIZE;
    if (
      0 <= my2 && my1 < map.length 
      && 0 <= mx && mx < map[my1].length 
      && (map[my1][mx] || map[my2][mx])
      ) {
      if (right) {
        x -= (x + Engine.TILE_SIZE / 2) % Engine.TILE_SIZE;
      } else {
        x += Engine.TILE_SIZE - (x - Engine.TILE_SIZE / 2) % Engine.TILE_SIZE;
      }
      velX = -velX;
    }
  }

  private void checkCollisionVertical(int dy, boolean[][] map){
    int my = (int)(y - 2 * dy) / Engine.TILE_SIZE;
    int mx = (int)x / Engine.TILE_SIZE;
    if (
      0 <= my && my < map.length
      && 0 <= mx && mx < map[my].length
      && map[my][mx]) {
      y += dy - y % Engine.TILE_SIZE;
      velY = 0;
    }
  }

  public void jump(){
    if (velY == 0) {
      velY = -11.5f;
    }
  }
  public void stop(){
    movement = STOP;
  }
  public void move(boolean right){
    movement = right ? RIGHT : LEFT;
  }

}