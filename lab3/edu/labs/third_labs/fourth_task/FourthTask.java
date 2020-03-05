package edu.labs.third_labs.fourth_task;

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
        return null;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new float[]{10.F,9.F,11.F,1.F}, 20.F)));
    }
}
