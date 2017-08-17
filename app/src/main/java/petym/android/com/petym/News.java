package petym.android.com.petym;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String detail;
    private String date;

    public News(String title, String detail, String date) {
        this.title = title;
        this.detail = detail;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
