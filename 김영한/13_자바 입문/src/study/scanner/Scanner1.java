package study.scanner;

import java.util.Scanner;

public class Scanner1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("문자열을 입력하세요 : ");
        final String str = scanner.nextLine();
        System.out.println("----------------------");
        System.out.println("입력한 문자열 = " + str);

        System.out.println();

        System.out.print("정수를 입력하세요 : ");
        final int intValue = scanner.nextInt();
        System.out.println("----------------------");
        System.out.println("입력한 정수 = " + intValue);
    }
}
