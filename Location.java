import java.io.Serializable;
public class Location implements Serializable{
  private int x;
  private int y;

  public Location(int x, int y){
    this.x = x;
    this.y = y;
  }

  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
  public boolean equals(Object o){
    Location l = (Location) o;
    if (l.getX()== x && l.getY() == y){
      return true;
    }
    return false;
  }
  public int hashCode(){
    int tmp = 3;
    return 53*x+y;
  }
  public String toString(){
    return x + "."+y;
  }
}
