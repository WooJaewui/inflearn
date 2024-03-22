package study.scanner;

import java.util.Scanner;

public class ScannerWhile2 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("첫 번째 숫자와 두 번째 숫자 모두 0을 입력하면 프로그램을 종료합니다.");

        while(true) {
            System.out.print("첫 번째 숫자를 입력하세요 = ");
            final int num1 = scanner.nextInt();

            System.out.print("두 번째 숫자를 입력하세요 = ");
            final int num2 = scanner.nextInt();

            if (num1 == 0 && num2 == 0) {
                System.out.println("프로그램 종료");
                break;
            }

            int sum = num1 + num2;
            System.out.println("두 숫자의 합은 = " + sum);
            System.out.println("=========================");
        }
    }
}
