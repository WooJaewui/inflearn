package study.scanner;

import java.util.Scanner;

public class ScannerWhileEx2 {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("상품의 가격을 입력하세요 (-1을 입력하면 종료) : ");
            final int price = scanner.nextInt();

            if (price == -1) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            System.out.print("구매하려는 수량을 입력하세요 : ");
            final int quantity = scanner.nextInt();

            final int totalCost = price * quantity;
            System.out.println("총 비용 : " + totalCost);
        }

    }
}
