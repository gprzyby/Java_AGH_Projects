package edu.labs.secondLabs;

class Item implements Comparable<Item>, Printable {
    private String name;
    private ItemCondition condition;
    private double mass;
    private int amount;

    public Item(String name, ItemCondition condition, double mass, int amount) {
        this.name = name;
        this.condition = condition;
        this.mass = mass;
        this.amount = amount;
    }

    @Override
    public int compareTo(Item o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public void printToStandartOutput() {
        System.out.println("Item " + this.name + " with condition "
                + this.condition.toString() + " mass " + this.mass + " and amount " + this.amount);
    }

    public void addItem(Item o) {
        this.mass += o.mass;
        this.amount += o.amount;
    }

    public String getName() {
        return name;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public double getMass() {
        return mass;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
