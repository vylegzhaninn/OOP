package vylegzhanin.pizzeria.supportive;

public class Storage {
    private final Order[] storage;
    private int i;

    public Storage(int Capacity) {
        storage = new Order[Capacity];
        i = 0;
    }

    public synchronized void add(Order order) {
        if (!isFull()){
            storage[i++] = order;
        }else {
            throw new ArrayIndexOutOfBoundsException("Склад переполнен");
        }
    }

    public synchronized Order get(){
        if (!isEmpty()) {
            return storage[--i];
        }else{
            throw new ArrayIndexOutOfBoundsException("Склад пуст");
        }
    }

    public synchronized boolean isEmpty(){
        return i < 1;
    }

    public synchronized boolean isFull(){
        return storage.length <= i;
    }
}
