package edu.labs.third_labs.second_task;

import java.util.Arrays;
import java.util.List;

public class SecondTask {

    /**
     * Algorithm search for min number that doesn't exists in list
     * @param list
     * @return min number that doesn't exists in list
     * @throws ListSizeNotSupportedException when list size is more than 10000
     * @throws ElementIntervalException when list has element less than -1000000 or more than 1000000
     */
    public static int solution(List<Integer> list) throws ListSizeNotSupportedException, ElementIntervalException {
        int minExists = 1;

        //checking algorithm conditions
        if(list.size() > 1e5)
            throw new ListSizeNotSupportedException("List size must have less than 10000 elements");
        if(list.parallelStream().filter(e -> (e < -1000000 || e > 1000000)).count() > 0)
            throw new ElementIntervalException("Algorithm supports numbers between -1000000 to 1000000");

        //clearing list from negative numbers and sorting elements
        Integer[] tab = list.parallelStream().filter( e -> e > 0).sorted().toArray(Integer[]::new);

        //return 1 if filtered list is empty
        if (tab.length == 0) {
            return minExists;
        }

        //case when tab has first element not 1
        if(tab[0] > minExists) {
            return minExists;
        } else {
        }


        //moving along tab and checking if difference is more than 1
        for(int i = 0; i < tab.length - 1; ++i) {
            if(tab[i + 1] - tab[i] > 1) {
                return  minExists + 1;
            } else {
                minExists = tab[i + 1];
            }
        }
        //when list is sorted and each element is one more than previous return last element
        return minExists + 1;
    }

    public static void main(String[] args) {
        System.out.println(solution(Arrays.asList(1)));
    }
}
