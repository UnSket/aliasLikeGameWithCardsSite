package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckDTO {

    @Nullable
    private Long id;
    private String name;
    private String description;
    @Nullable
    private Integer imagesOnCard;
    @Nullable
    private Integer textSize;
    private Boolean isNumerated;

    public DeckDTO() {
    }

    public DeckDTO(String name, String description, Integer imagesOnCard, Integer textSize) {
        this.name = name;
        this.description = description;
        this.imagesOnCard = imagesOnCard;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Integer getImagesOnCard() {
        return imagesOnCard;
    }

    public void setImagesOnCard(int imagesOnCard) {
        this.imagesOnCard = imagesOnCard;
    }

    public Integer getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Boolean getIsNumerated() {
        return isNumerated;
    }

    public void setIsNumerated(Boolean numerated) {
        isNumerated = numerated;
    }
}
