package edu.labs.secondLabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FulfillmentCenterContainer {
    private Map<String,FulfillmentCenter> container = new HashMap<>();

    public FulfillmentCenterContainer() {

    }

    public void addCenter(String name, double maxMass) {
         container.put(name,new FulfillmentCenter(name,maxMass));
    }

    public FulfillmentCenter getCenter(String key) {
        return container.get(key);
    }

    public void removeCenter(String key) {
        container.remove(key);
    }

    public List<FulfillmentCenter> findEmpty() {
        return container.values().stream().filter(e -> {
                    return e.getMass() <= 0;
                }).collect(Collectors.toList());
    }

    public void summary() {
        container.forEach((e,f) -> {f.printToStandartOutput();});
    }
}
