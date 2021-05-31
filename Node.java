import java.io.Serializable;

public class Node<E> implements Serializable{
  private E data;
  private Node<E> next;
  private Node<E> prev;

	public Node(E data){
		this.data = data;
		next = null;
    prev = null;
	}

	public E get(){
		return data;
	}
	public Node<E> next(){
		return next;
	}
  public Node<E> prev(){
		return prev;
	}
	public void setNext(Node<E> n){
		next = n;
	}
  public void setPrev(Node<E> p){
		prev = p;
	}
	public void setData(E e){
    data = e;
  }
}
