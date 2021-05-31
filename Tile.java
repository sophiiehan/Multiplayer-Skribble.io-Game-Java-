import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;

public class Tile implements Serializable{

  private int red;
  private int green;
  private int blue;
  private Color color;

  public Tile(int r, int g, int b){
    red = r;
    green = g;
    blue = b;
    color = new Color(r,g,b);
  }

  public void drawMe(Graphics g, int x, int y){

    g.setColor(color);
    g.fillRect(x,y,15,15);
    g.setColor(Color.BLACK);
    g.drawRect(x,y,15,15);

  }
  public void reset(){
    color = new Color(255, 255, 255);
  }
  public void setColor(Color c){
    color = c;
    red = c.getRed();
    green = c.getGreen();
    blue = c.getBlue();
  }
  public Color getColor(){
    return color;
  }
  public String toString(){
    return red+"."+green+"."+blue;
  }
}
