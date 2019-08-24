package deck.dto;

public class EditDeckDTO {

    private Long id;
    private String name;
    private String description;

    public EditDeckDTO() {
    }

    public EditDeckDTO(Long id, String name, String description, int imagesOnCard) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

}
