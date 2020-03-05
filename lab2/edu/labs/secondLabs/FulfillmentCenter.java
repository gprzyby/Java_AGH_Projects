package edu.labs.secondLabs;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FulfillmentCenter implements Printable {
    private String name;
    private final double maxMass;
    private double mass = 0.D;
    private List<Item> listOfItems;

    public FulfillmentCenter(String name, double capacity) {
        this.name = name;
        this.maxMass = capacity;
        this.listOfItems = new LinkedList<>();
    }

    public void addProduct(Item newProduct) {
        //checking if newProducts could fit
        if(this.mass + newProduct.getMass() > maxMass) {
            System.err.println(super.toString() + " is full");
            return;
        }
        //adding mass from newProduct
        this.mass += newProduct.getMass();

        //checking if this type of products exits and adding it
        AtomicBoolean itemExist = new AtomicBoolean(false);
        Iterator<Item> iterator = listOfItems.iterator();
        iterator.forEachRemaining(e -> {
            if(e.compareTo(newProduct) == 0 &&
                    e.getCondition() == newProduct.getCondition()) {
                    e.addItem(newProduct);
                    itemExist.set(true);
            }
        });

        //adding products if it's different from others
        if ( !itemExist.get() ) {
            listOfItems.add(newProduct);
        }
    }

    public Item getProduct(Item pattern) throws NoSuchElementException {
        Iterator<Item> iterator = this.listOfItems.iterator();
        Item toReturn = null;
        boolean ifProductExists = false;

        //searching in loop if that items exist
        while (iterator.hasNext() && !ifProductExists) {
            Item temp = iterator.next();
            if (temp.compareTo(pattern) == 0 &&
                    temp.getCondition() == pattern.getCondition()) {
                    //checking if there is enough items in center
                    if(pattern.getAmount() > temp.getAmount()) throw new NoSuchElementException("There isn't enough items for that operation");
                    //noticing that product has been found, returning Item and reducing
                    ifProductExists = true;
                    double deltaMass = temp.getMass()/temp.getAmount();
                    toReturn = new Item(temp.getName(), temp.getCondition(), deltaMass,pattern.getAmount());
                    temp.setAmount(temp.getAmount() - pattern.getAmount());

                    //deleting if amount == 0
                    if(temp.getAmount() == 0) {
                        iterator.remove();
                    }
            }
        }
        if(!ifProductExists) {
            throw new NoSuchElementException("There isn't that item");
        }
        return toReturn;
    }

    public void removeProduct(Item pattern) throws NoSuchElementException {
        Iterator<Item> iterator = this.listOfItems.iterator();

        //finding items to remove
        while (iterator.hasNext()) {
            Item temp = iterator.next();

            //checking, removing product and exiting function
            if(temp.compareTo(pattern) == 0 &&
                    pattern.getCondition() == temp.getCondition()) {
                this.mass -= temp.getMass();
                iterator.remove();
                return;
            }
        }

        //if there isn't any similar item throws exception
        throw new NoSuchElementException("That items doesen't exists");
    }

    public Item search(String itemName) throws NoSuchElementException{
        return listOfItems.stream().filter(e -> {
            return itemName.compareTo(e.getName()) == 0;
        }).findFirst().orElseThrow(NoSuchElementException::new);
    }

    public Item[] searchPartial(String itemName) {
        return listOfItems.stream().filter(e -> {
            return e.getName().contains(itemName);
        }).toArray(Item[]::new);
    }

    public int countByCondition(ItemCondition condition) {
        return listOfItems.stream().filter(e -> {
            return e.getCondition() == condition;
        }).mapToInt(Item::getAmount).sum();
    }

    public void summary() {
        Iterator<? extends Printable> iterator = listOfItems.iterator();

        iterator.forEachRemaining(Printable::printToStandartOutput);
    }

    public List<Item> sortByName() {
        Stream<Item> stream = listOfItems.stream().sorted(Item::compareTo);
        return stream.collect(Collectors.toList());
    }

    public List<Item> sortByAmount() {
        Stream<Item> stream = listOfItems.stream().sorted((e,f) -> {
            return Integer.compare(e.getAmount(),f.getAmount()) * -1;
        });
        return stream.collect(Collectors.toList());
    }

    public Item max() {
        return Collections.max(listOfItems,Comparator.comparingInt(Item::getAmount));
    }

    @Override
    public void printToStandartOutput() {
        System.out.println(this.name + " with fill " + String.format("%.2f",(this.mass/this.maxMass)*100.D) + "%");
    }

    public String getName() {
        return name;
    }

    public double getMaxMass() {
        return maxMass;
    }

    public double getMass() {
        return mass;
    }
}
