
import java.io.Serializable;
public class MyHashMap<K,V> implements Serializable{
  private V[] table;
  private DLList<K> keys;

  @SuppressWarnings("unchecked")
  public MyHashMap(){
		keys = new DLList<K>();
    table = (V[])new Object[50000];
  }

  public void put(K key,V value){
    if (!keys.contains(key)) {
      keys.add(key);
      int index = key.hashCode();
      table[index] = value;
    }
  }

  
  public V get(K key) {
    if (keys.contains(key)) {
      return table[key.hashCode()];
    }
    return null;
  }


  public DLList<K> getKeys() {
    return keys;
  }

  public String toString(){
    String str = "";
    for(int i = 0; i< keys.size(); i++){
      str += keys.get(i).toString() + "-";
      str += get(keys.get(i)).toString()+"_";
      // DLList<V> values = table[keys.get(i).hashCode()];
      // for (int j = 0; j<values.size();j++) {
      //   str += values.get(j) + ", ";
      // }
    //  str+="\n";
    }

    return str;
  }

}
