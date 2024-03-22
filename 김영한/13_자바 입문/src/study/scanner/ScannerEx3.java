package study.scanner;

import java.util.Scanner;

public class ScannerEx3 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("음식 이름을 입력해주세요 : ");
        final String foodName = scanner.nextLine();

        System.out.print("음식의 가격을 입력해주세요 : ");
        final int foodPrice = scanner.nextInt();

        System.out.print("음식의 수량을 입력해주세요 : ");
        final int foodQuantity = scanner.nextInt();

        System.out.println("==========================");

        final int totalPrice = (foodPrice * foodQuantity);
        System.out.println(foodName + " " + foodQuantity + "개를 주문하셨습니다. 총 가격은 " + totalPrice + "원입니다.");
    }
}
