import java.io.Serializable;

public class DLList<E> implements Serializable {
	private Node<E> head;
	private Node<E> tail;
	private int size;
	public DLList() {
		head = null;
		tail = null;
		size = 0;
	}
	public void add(E element) {
		Node<E> newNode = new Node(element);
		if (size==0) {
			head = newNode;
			tail = newNode;
		} else {
			tail.setNext(newNode);
			newNode.setPrev(tail);
			newNode.setNext(null);
			tail = newNode;
		}
		size++;
	}
	public E get(int location) {
		Node<E> current = head;
		int x = 0;
		while (x<location){
			x++;
			current=current.next();
		}
		return current.get();
	}
	public void remove(E data) {
		Node<E> current = head;
    if (tail.get().toString().equals(data.toString())) {
      tail = tail.prev();
      tail.setNext(null);
      size--;
    } else if (head.get().toString().equals(data.toString())) {
      head = current.next();
      head.setPrev(null);
      size--;
    } else {
			while (current.next().next() != null) {
				if (current.next().get().toString().equals(data.toString())) {
				  current.setNext(current.next().next());
				  current.next().setPrev(current);
          size--;
				  break;

				}
				current = current.next();
			}

		}

	}



  public String toString() {
    String list = "";
		Node<E> current = head;
		for (int i =0; i<size; i++) {
			String str = current.get().toString();
			list+=str + " ";
			current = current.next();
		}
		return list;
  }
	public int size() {
		return size;
	}
  public boolean contains(E data) {
    Node<E> lookFor = new Node<E>(data);
    Node<E> current = head;
    for(int i = 0; i<size; i++) {
      if(current.get().equals(lookFor.get())) {
        return true;
      }
      current = current.next();
    }

    return false;
  }

}
