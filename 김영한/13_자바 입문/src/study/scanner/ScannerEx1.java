package study.scanner;

import java.util.Scanner;

public class ScannerEx1 {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);

        System.out.print("당신의 이름을 입력하세요 : ");
        final String name = input.nextLine();

        System.out.print("당신의 나이를 입력하세요 : ");
        final int age = input.nextInt();

        System.out.println("==================================");
        System.out.println("당신의 이름은 " + name + "이고, 나이는 " + age + "입니다.");
    }
}
