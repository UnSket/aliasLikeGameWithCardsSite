package deck.dto;

public class DeckDTO {

    private String name;
    private String description;
    private int imagesOnCard;

    public DeckDTO() {
    }

    public DeckDTO(String name, String description, int imagesOnCard) {
        this.name = name;
        this.description = description;
        this.imagesOnCard = imagesOnCard;
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

    public int getImagesOnCard() {
        return imagesOnCard;
    }

    public void setImagesOnCard(int imagesOnCard) {
        this.imagesOnCard = imagesOnCard;
    }
}
