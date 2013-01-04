Slab
====

There is a good description [here](http://www.insightfullogic.com/blog/2013/jan/3/slab-guaranteed-heap-alignment-jvm/)

tl;dr: Offheap Java Tuples that look like POJOs with guaranteed memory alignment.

Code Example
------------

    // Define your DataType
    public interface GameEvent extends Cursor {
      public int getId();
      public void setId(int value);
      public long getStrength();
      public void setStrength(long value);
    }

    // Create an allocator for your DataType
    Allocator<GameEvent> eventAllocator = Allocator.of(GameEvent.class);

    // Allocate 100 off heap GameEvent instances - sequentially in memory
    GameEvent event = eventAllocator.allocate(100);

    // Move to the index of the instance that you want to read from or write to
    event.move(1);

    // set and get values like a normal POJO
    event.setId(6);
    assertEquals(6, event.getId());
