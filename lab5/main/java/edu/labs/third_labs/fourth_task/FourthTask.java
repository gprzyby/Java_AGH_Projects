package edu.labs.third_labs.fourth_task;

import edu.labs.third_labs.third_task.NoSolutionExistException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FourthTask {
    public static int[] solution(float[] tab, float sum) {
        float actSum = 0.F;
        int[] toRet = new int[] {0,0};

        Map<Float,Integer> map = new HashMap<>();
        for(int i = 0; i < tab.length; ++i) {
            actSum = sum - tab[i] ;

            if(map.containsKey(tab[i])) {
                toRet[0] = i;
                toRet[1] = map.get(tab[i]);
                return toRet;
            } else {
                map.put(actSum,i);
            }
        }
        throw new NoSolutionExistException();
    }

}
