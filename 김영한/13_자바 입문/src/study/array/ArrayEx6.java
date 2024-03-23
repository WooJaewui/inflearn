package study.array;

import java.util.Scanner;

public class ArrayEx6 {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);

        System.out.println("입력받을 숫자의 개수를 입력하세요 : ");
        final int count = input.nextInt();

        int[] numbers = new int[count];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        System.out.println(count + "개의 정수를 입력하세요 : ");
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = input.nextInt();
            if (min > numbers[i]) {
                min = numbers[i];
            }

            if (max < numbers[i]) {
                max = numbers[i];
            }
        }

        System.out.println("max = " + max);
        System.out.println("min = " + min);
    }
}
