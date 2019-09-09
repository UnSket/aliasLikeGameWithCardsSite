package deck.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ImageTextConfigDTO {

    long id;

    @Min(0)
    @Max(120)
    int size;

    public ImageTextConfigDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
