package edu.labs.third_labs.second_task;

public class ListSizeNotSupportedException extends IllegalArgumentException {
    public ListSizeNotSupportedException() { }
    public ListSizeNotSupportedException(String message) {
        super(message);
    }
}
