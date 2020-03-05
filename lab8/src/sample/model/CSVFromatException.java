package sample.model;

import java.util.zip.DataFormatException;

public class CSVFromatException extends DataFormatException {
    CSVFromatException() {
        super();
    }

    CSVFromatException(String message) {
        super(message);
    }
}
