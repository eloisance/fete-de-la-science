package project.istic.com.fetedelascience.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "event")
public class Event implements Parcelable {

    public static final String TITLE_FIELD_NAME = "title";
    public static final String ID_FIELD_NAME = "id";
    public static final String VILLE_FIELD_NAME = "ville";
    public static final String ADRESSE_FIELD_NAME = "adresse";
    public static final String LIEN_FIELD_NAME = "lien";
    public static final String DESCRIPTION_FIELD_NAME = "description";
    public static final String DATE_FIELD_NAME = "resume_dates_fr";
    public static final String DATE_DEBUT_FIELD_NAME = "date_debut";


    @DatabaseField(id = true)
    @SerializedName("identifiant")
    private int id;

    @DatabaseField
    @SerializedName("titre_fr")
    private String title;

    @DatabaseField
    @SerializedName("ville")
    private String ville;

    @DatabaseField
    @SerializedName("adresse")
    private String adresse;

    @DatabaseField
    @SerializedName("lien")
    private String lien;

    @DatabaseField
    @SerializedName("description_longue_fr")
    private String description;

    @DatabaseField
    @SerializedName("date_debut")
    private String date_debut;

    @DatabaseField
    @SerializedName("date_fin")
    private String date_fin;

    @DatabaseField
    @SerializedName("resume_dates_fr")
    private String resume_dates_fr;

    @DatabaseField
    private Double longitude;

    @DatabaseField
    private Double latitude;

    /**
     * Peut être null
     */
    @DatabaseField
    private String apercu;

    public Event() {}

    public Event(int id, String title, String ville, String adresse, String lien, String description, String date_debut, String date_fin, String apercu,String resume_dates_fr) {
        setId(id);
        setTitle(title);
        setVille(ville);
        setAdresse(adresse);
        setLien(lien);
        setDescription(description);
        setDate_debut(date_debut);
        setDate_fin(date_fin);
        setApercu(apercu);
        setResume_dates_fr(resume_dates_fr);
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public String getResume_dates_fr() {
        return resume_dates_fr;
    }

    public void setResume_dates_fr(String resume_dates_fr) {
        this.resume_dates_fr = resume_dates_fr;
    }
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ville='" + ville + '\'' +
                ", adresse='" + adresse + '\'' +
                ", lien='" + lien + '\'' +
                ", description='" + description + '\'' +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", apercu='" + apercu + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.ville);
        dest.writeString(this.adresse);
        dest.writeString(this.lien);
        dest.writeString(this.description);
        dest.writeString(this.date_debut);
        dest.writeString(this.date_fin);
        dest.writeString(this.resume_dates_fr);
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
        dest.writeString(this.apercu);
    }

    protected Event(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.ville = in.readString();
        this.adresse = in.readString();
        this.lien = in.readString();
        this.description = in.readString();
        this.date_debut = in.readString();
        this.date_fin = in.readString();
        this.resume_dates_fr = in.readString();
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.apercu = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
