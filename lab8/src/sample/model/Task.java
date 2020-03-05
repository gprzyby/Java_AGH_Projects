package sample.model;

import javafx.scene.input.DataFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.zip.DataFormatException;

public class Task implements Serializable, SerializableToCSV<Task> {
    private static final long serialVersionUID = -10203928103L;
    private static final int CSV_CELL_SIZE = 5;
    public static final DataFormat TASK_DATA_FORMAT = new DataFormat(Task.class.getName());

    private String description;
    private LocalDate deadLine;
    private String title;
    private Priority priority;

    public Task() {

    }

    public Task(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
    }

    public Task(String title, Priority priority, String date) throws DateTimeParseException {
        //calling previously defined constructor
        this(title, priority);

        //defining date using DateFormatter static method
        this.deadLine = DateFormatter.parseToDate(date);

    }

    public Task(String title, Priority priority, String date, String description) {
        this(title, priority, date);
        this.description = description;
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task)super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;
        if(!(obj instanceof Task)) return false;
        Task task = (Task) obj;
        return task.title.equals(this.title) &&
                task.deadLine.equals(this.deadLine) &&
                task.priority == this.priority &&
                task.description.equals(this.description);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return deadLine;
    }

    public void setDate(String date) throws DateTimeParseException {
        this.deadLine = DateFormatter.parseToDate(date);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return this.title ;
    }

    public static int getCellUsageAmount() {
        return CSV_CELL_SIZE;
    }

    @Override
    public String getCodedTitle() {
        return "Title;Data Format;Data;Priority;Description";
    }

    @Override
    public String toCSVFormat(char separator) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(title);
        buffer.append(separator);
        buffer.append(DateFormatter.getDataFormat());
        buffer.append(separator);
        buffer.append(DateFormatter.parseToString(deadLine));
        buffer.append(separator);
        buffer.append(priority.getPriorityName());
        buffer.append(separator);
        buffer.append(description);
        return buffer.toString();
    }

    @Override
    public Task getFromCSVLine(String data, char separator, int offset) throws CSVFromatException{
        String[] splitted = data.split(String.valueOf(separator));
        if(splitted.length != CSV_CELL_SIZE + offset) throw new CSVFromatException("Wrong cell amount");
        this.title = splitted[offset];
        String dateFormat = splitted[1 + offset];
        String date = splitted[2 + offset];
        try {
            this.deadLine = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
        } catch (DateTimeParseException exc) {
            throw new CSVFromatException("Wrong date format");
        }
        this.priority = Priority.getPriotiryByName(splitted[3 + offset]);
        if(this.priority == null) throw new CSVFromatException("Wrong Priority Name");
        this.description = splitted[4 + offset];
        return this;
    }
}
