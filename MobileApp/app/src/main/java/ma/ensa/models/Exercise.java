package ma.ensa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String base64Image;
    private String base64Video;
    private float exerciseScore;

    public Exercise() {
    }

    protected Exercise(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        base64Image = in.readString();
        base64Video = in.readString();
        exerciseScore = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(base64Image);
        dest.writeString(base64Video);
        dest.writeFloat(exerciseScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Video() {
        return base64Video;
    }

    public void setBase64Video(String base64Video) {
        this.base64Video = base64Video;
    }

    public float getExerciseScore() {
        return exerciseScore;
    }

    public void setExerciseScore(float exerciseScore) {
        this.exerciseScore = exerciseScore;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "base64Image='" + base64Image.substring(0,3) + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", base64Video='" + base64Video.substring(0,3) + '\'' +
                '}';
    }
}