package deck.dto;

public class ActivateUserDTO {

    private Boolean active;

    private Long userId;

    public ActivateUserDTO() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
