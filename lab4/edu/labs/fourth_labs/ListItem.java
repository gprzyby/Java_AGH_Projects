package edu.labs.fourth_labs;

class ListItem {
    private String description;
    private String function;
    private int caretMove;

    ListItem(String funDescr, String functionToParse, int caretMove) {
        this.caretMove = caretMove;
        this.description = funDescr;
        this.function = functionToParse;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getDescription() {
        return description;
    }

    public String getFunction() {
        return function;
    }

    public int getCaretMove() {
        return caretMove;
    }
}
