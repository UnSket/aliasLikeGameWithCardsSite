package deck.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "IMAGE_META")
public class ImageMeta implements Serializable {

    private long id;

    private long ownerId;

    private String link;

    private int height;
    private int width;

    public ImageMeta() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
