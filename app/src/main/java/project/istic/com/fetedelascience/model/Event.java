package project.istic.com.fetedelascience.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "event")
public class Event {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField()
    @SerializedName("title_fr")
    private String title;

    @DatabaseField
    private String apercu;

    public Event() {}

    public Event(String title, String apercu) {
        setTitle(title);
        setApercu(apercu);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                ", title='" + title + '\'' +
                ", apercu='" + apercu + '\'' +
                '}';
    }
}
