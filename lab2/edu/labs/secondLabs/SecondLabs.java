package edu.labs.secondLabs;

public class SecondLabs {

    public static void main(String[] args) {
        FulfillmentCenterContainer center = new FulfillmentCenterContainer();
        center.addCenter("zibex", 200.D);
        center.summary();
        center.findEmpty().get(0).addProduct(
                new Item("apples",ItemCondition.NEW,10D,100) );
        center.getCenter("zibex").addProduct(new Item("apples", ItemCondition.NEW,100D,1000));
        center.getCenter("zibex").addProduct(new Item("oil", ItemCondition.NEW, 50D,80));
        center.getCenter("zibex").addProduct(new Item("mango", ItemCondition.NEW, 10D,20));
        center.getCenter("zibex").search("apples").printToStandartOutput();
        center.getCenter("zibex").getProduct(new Item("apples",ItemCondition.NEW,110D,1100));
        System.out.println("Amount of new items" + center.getCenter("zibex").countByCondition(ItemCondition.NEW));
        center.getCenter("zibex").removeProduct(new Item("oil",ItemCondition.NEW,0D,0));
        center.getCenter("zibex").max().printToStandartOutput();
        center.getCenter("zibex").sortByAmount().toString();
        center.getCenter("zibex").sortByName().toString();
//        center.getCenter("zibex").search("apples").printToStandartOutput();
        center.summary();
    }
}