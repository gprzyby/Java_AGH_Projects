package edu.labs.third_labs.third_task;

public class ThirdTask {

    public static int substring(String a,String b) {
        //a must have all letters from b
        for(char character : b.toCharArray()) {
            if(!a.contains(String.valueOf(character))) {
                throw new IllegalArgumentException("a must hava all letters form b");
            }
        }
        int beginningALength = a.length();
        int toRet = Math.floorDiv(b.length(),a.length());
        for(int i=1; i < toRet; ++i) {
            a += a;
        }

        for(int i=0; i < beginningALength; ++i)
        {
            if(a.contains(b)) {
                return toRet;
            }
            a += a;
            ++toRet;
        }
        throw new NoSolutionExistException("result not available");
    }

}
