package net.vikingsen.gridadapters.sample.event;

/**
 * Created by Jordan Hansen
 */
public class TitleEvent {
    private final String title;

    public TitleEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
