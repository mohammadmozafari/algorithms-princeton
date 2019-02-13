import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private Node start, end;
    private int counter;

    //private classes
    private class Node
    {
        Node next;
        Node previous;
        Item item;
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node current = start;
        @Override
        public boolean hasNext()
        {
            return current != null;
        }

        @Override
        public Item next()
        {
            if (current == null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    //private methods
    private Node insert(Item item, Node next, Node prev)
    {
        if (item == null)
            throw new IllegalArgumentException();

        Node node = new Node();
        node.item = item;
        node.next = next;
        node.previous = prev;
        counter++;

        if (next != null)
            next.previous = node;
        if (prev != null)
            prev.next = node;

        return node;
    }

    private Item remove(Node node)
    {
        if (node.previous != null)
            node.previous.next = node.next;
        if (node.next != null)
            node.next.previous = node.previous;

        counter--;
        return node.item;
    }

    //constructor and public methods
    public Deque()
    {
        start = end = null;
        counter = 0;
    }

    public boolean isEmpty()
    {
        return counter == 0;
    }

    public int size()
    {
        return counter;
    }

    public void addFirst(Item item)
    {
        Node node = insert(item, start, null);
        start = node;
        if (end == null) end = node;
    }

    public void addLast(Item item)
    {
        Node node = insert(item, null, end);
        end = node;
        if (start == null) start = node;
    }

    public Item removeFirst()
    {
        if (start == null) throw new NoSuchElementException();
        Node temp = start.next;
        if (start == end)
            end = temp;
        Item item = remove(start);
        start = temp;
        return item;
    }

    public Item removeLast()
    {
        if (end == null) throw new NoSuchElementException();
        Node temp = end.previous;
        if (start == end)
            start = temp;
        Item item = remove(end);
        end = temp;
        return item;
    }

    @Override
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    public static void main(String[] args)
    {
        Deque<String> strings = new Deque<>();
        strings.addFirst("hello");
        strings.addFirst("mohammad");
        strings.addLast("amazing");
        strings.addFirst("secondHello");
//        strings.addFirst(null);
//        System.out.println(strings.removeLast());

        for (String s : strings)
        {
            System.out.println(strings.size() + " : " + s);
        }
    }
}
