package project.istic.com.fetedelascience.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Parcours implements Serializable, Parcelable {

    String name;
    List<Integer> listEvent;
    String idUser;

    public Parcours() {
    }

    public Parcours(String name, List<Integer> listEvent,String idUser) {
        setName(name);
        setListEvent(listEvent);
        setIdUser(idUser);

    }

    public Integer numberEvent(){
        return this.listEvent.size();
    }



    public String getName() {
        return name;
    }

    public List<Integer> getListEvent() {
        return listEvent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListEvent(List<Integer> listEvent) {
        this.listEvent = listEvent;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Parcours{" +
                "name='" + name + '\'' +
                ", listEvent=" + listEvent +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeList(this.listEvent);
        dest.writeString(this.idUser);
    }

    protected Parcours(Parcel in) {
        this.name = in.readString();
        this.listEvent = new ArrayList<Integer>();
        in.readList(this.listEvent, Integer.class.getClassLoader());
        this.idUser = in.readString();
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
}
