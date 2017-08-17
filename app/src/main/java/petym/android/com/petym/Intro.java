package petym.android.com.petym;

import java.io.Serializable;

public class Intro implements Serializable {
    private String name;
    private String title;
    private int imageId;

    public Intro(String name, String title, int imageId) {
        this.name = name;
        this.title = title;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
