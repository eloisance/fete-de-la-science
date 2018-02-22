package project.istic.com.fetedelascience.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;


public class Parcours implements Serializable, Parcelable {

    String name;
    List<String> listEvent;

    public Parcours() {
    }

    public Parcours(String name, List<String> listEvent) {
        setName(name);
        setListEvent(listEvent);

    }

    public Integer numberEvent(){
        return this.listEvent.size();
    }



    public String getName() {
        return name;
    }

    public List<String> getListEvent() {
        return listEvent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListEvent(List<String> listEvent) {
        this.listEvent = listEvent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.listEvent);
    }

    protected Parcours(Parcel in) {
        this.name = in.readString();
        this.listEvent = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Parcours> CREATOR = new Parcelable.Creator<Parcours>() {
        @Override
        public Parcours createFromParcel(Parcel source) {
            return new Parcours(source);
        }

        @Override
        public Parcours[] newArray(int size) {
            return new Parcours[size];
        }
    };

    @Override
    public String toString() {
        return "Parcours{" +
                "name='" + name + '\'' +
                ", listEvent=" + listEvent +
                '}';
    }
}
