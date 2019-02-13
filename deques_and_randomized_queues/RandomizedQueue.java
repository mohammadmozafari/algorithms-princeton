import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    //fields
    private Item[] items;
    private int end;

    //private class
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        Item[] iteration;
        int pointer;

        public RandomizedQueueIterator()
        {
            pointer = end;
            iteration = (Item[])(new Object[pointer]);
            System.arraycopy(items, 0, iteration, 0, pointer);
        }

        @Override
        public boolean hasNext()
        {
            return pointer != 0;
        }

        @Override
        public Item next()
        {
            if (pointer == 0) throw new NoSuchElementException();

            int randomIndex = StdRandom.uniform(pointer);
            Item result = iteration[randomIndex];
            iteration[randomIndex] = iteration[--pointer];

            return result;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }

    //private methods
    private void resize(int newSize)
    {
        Item[] newArray = (Item[])(new Object[newSize]);
        System.arraycopy(items, 0, newArray, 0, end);
        items = newArray;
    }

    //public methods
    public RandomizedQueue()
    {
        items = (Item[])(new Object[1]);
        end = 0;
    }

    public boolean isEmpty()
    {
        return end == 0;
    }

    public int size()
    {
        return end;
    }

    public void enqueue(Item item)
    {
        if (item == null) throw new IllegalArgumentException();

        items[end++] = item;
        if (end == items.length) resize(items.length * 2);
    }

    public Item dequeue()
    {
        if (isEmpty()) throw new NoSuchElementException();

        int randomIndex = StdRandom.uniform(end);
        Item result = items[randomIndex];
        items[randomIndex] = items[--end];

        if (end == items.length / 4) resize(items.length / 2);
        return result;
    }

    public Item sample()
    {
        if (isEmpty()) throw new NoSuchElementException();

        int randomIndex = StdRandom.uniform(end);
        return items[randomIndex];
    }


    @Override
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args)
    {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);

        for (Object num : queue)
        {
            System.out.println((int)num);
        }
    }
}
