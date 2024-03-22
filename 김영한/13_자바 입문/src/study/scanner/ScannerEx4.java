package study.scanner;

import java.util.Scanner;

public class ScannerEx4 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("구구단의 단 수를 입력해주세요 : ");
        final int number = scanner.nextInt();

        System.out.println("====================================");
        System.out.println(number + "단의 구구단 : ");
        System.out.println();
        for(int i=1; i<=9; i++) {
            System.out.println(number + " x " + i + " = " + (number * i));
        }
    }
}
