package study.method;

public class Overloading2 {
    public static void main(String[] args) {
        myMethod(1, 1.5);
        myMethod(1.2, 2);
        myMethod(5, 2);
    }
    public static void myMethod(int a, int b) {
        System.out.println("int a, int b");
    }

    public static void myMethod(int a, double b) {
        System.out.println("int a, double b");
    }

    public static void myMethod(double a, int b) {
        System.out.println("double a, int b");
    }
}
