package study.array;

import java.util.Scanner;

public class ArrayEx5 {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);
        System.out.print("입력받을 숫자의 개수를 입력하세요 : ");
        final int count = input.nextInt();

        int[] numbers = new int[count];
        int sum = 0;
        double average;

        System.out.println(count + "개의 정수를 입력하세요 : ");
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = input.nextInt();
            sum += numbers[i];
        }

        average = (double) sum / numbers.length;

        System.out.println("sum = " + sum);
        System.out.println("average = " + average);
    }
}
