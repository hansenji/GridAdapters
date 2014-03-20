package net.vikingsen.gridadapters.sample.event;

/**
 * Created by Jordan Hansen
 */
public class GridSelectedEvent {
    private final int id;

    public GridSelectedEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
