package sample.model;

interface SerializableToCSV<T> {
    String toCSVFormat(char separator);
    T getFromCSVLine(String data, char separator, int offset) throws CSVFromatException;
    String getCodedTitle();
}
