package deck.util;

import deck.storage.ImageStorageException;

public class CardGenerationUnavailable extends ImageStorageException {

    public CardGenerationUnavailable(String message) {
        super(message);
    }
}
