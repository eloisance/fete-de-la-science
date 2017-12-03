package project.istic.com.fetedelascience.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "event")
public class Event {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String apercu;

    public Event() {}

    public Event(String apercu) {
        this.apercu = apercu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApercu() {
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", apercu='" + apercu + '\'' +
                '}';
    }
}
