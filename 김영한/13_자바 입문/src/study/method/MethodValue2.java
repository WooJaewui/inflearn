package study.method;

public class MethodValue2 {
    public static void main(String[] args) {
        int number = 5;
        System.out.println("mainNumber = " + number);

        changeNumber(number);
        System.out.println("mainNumber = " + number);
    }

    public static void changeNumber(int number) {
        System.out.println("methodNumber = " + number);

        number = number * 2;
        System.out.println("methodNumber = " + number);
    }
}
