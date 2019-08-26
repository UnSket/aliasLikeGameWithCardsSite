package deck.image.generation;

import deck.storage.ImageStorageException;

public class CardGenerationUnavailable extends ImageStorageException {

    public CardGenerationUnavailable(String message) {
        super(message);
    }
}
