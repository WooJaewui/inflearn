package study.method;

public class MethodValue3 {
    public static void main(String[] args) {
        int number = 5;
        System.out.println("mainNumber = " + number);
        number = changeNumber(number);
        System.out.println("mainNumber = " + number);
    }

    public static int changeNumber(int number) {
        System.out.println("methodNumber = " + number);
        number = number * 2;
        System.out.println("methodNumber = " + number);

        return number;
    }
}
