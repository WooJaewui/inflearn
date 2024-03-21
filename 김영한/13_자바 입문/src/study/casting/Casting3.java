package study.casting;

public class Casting3 {
    public static void main(String[] args) {
        long maxIntValue = 2147483647; // int 최고값
        long maxIntOver = 2147483648L; // int 최고값

        System.out.println("maxIntValue = " + maxIntValue);
        System.out.println("maxIntOver = " + (int) maxIntOver);

        long minIntValue = -2147483648; // int 최고값
        long minIntOver = -2147483649L; // int 최고값

        System.out.println("minIntValue = " + minIntValue);
        System.out.println("minIntOver = " + (int) minIntOver);
    }
}
