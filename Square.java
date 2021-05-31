import java.awt.Graphics;
import java.awt.Color;
import java.io.Serializable;

public class Square implements Serializable{

  private int red;
  private int green;
  private int blue;
  private Color color;

  public Square(){
    color = new Color(255,255,255);
  }

  public void drawMe(Graphics g, int x, int y){

    g.setColor(color);
    g.fillRect(x,y,15,15);
    g.setColor(Color.BLACK);
    g.drawRect(x,y,15,15);

  }

  public void setColor(Color c){
    color = c;
  }
}
