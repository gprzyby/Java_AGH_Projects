package sample.model;

import javafx.scene.paint.Color;

public enum Priority {
    CRITICAL("Critical", Color.RED),
    HIGH("High", Color.ORANGERED),
    MEDIUM("Medium", Color.ORANGE),
    LOW("Low", Color.GREEN);

    private Color color;
    private String priorityName;

    Priority(String priorityName,Color color) {
        this.color = color;
        this.priorityName = priorityName;
    }

    public Color getColor() {
        return color;
    }

    public String getPriorityName() {
        return priorityName;
    }

    @Override
    public String toString() {
        return priorityName;
    }
}
