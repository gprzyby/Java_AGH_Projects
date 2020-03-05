package sample.model;

import javafx.scene.input.DataFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Task implements Serializable {
    private static final long serialVersionUID = -10203928103L;

    public static final DataFormat TASK_DATA_FORMAT = new DataFormat(Task.class.getName());
    private String description;
    private LocalDate deadLine;
    private String title;
    private Priority priority;

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

    public Task(String title, Priority priotity, String date, String description) {
        this(title, priotity, date);
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
}
