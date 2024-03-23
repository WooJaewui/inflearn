package study.array;

import java.util.Scanner;

public class ArrayEx4 {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);
        int[] numbers = new int[5];

        System.out.println("5개의 정수를 입력하세요 : ");
        int sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = input.nextInt();
            sum += numbers[i];
        }

        double average = (double) sum / numbers.length;
        System.out.println("sum = " + sum);
        System.out.println("average = " + average);
    }
}
