package study.array;

import java.util.Scanner;

public class ProductAdminEx {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);
        String[] productNames = new String[10];
        int[] productPrices = new int[10];
        int productCount = 0;
        int maxProducts = 10;

        while (true) {
            System.out.println("1. 상품 등록 | 2. 상품 목록 | 3. 종료");
            System.out.print("메뉴를 선택하세요 : ");
            int menu = input.nextInt();
            input.nextLine();

            if (menu == 1) {
                if (productCount >= maxProducts) {
                    System.out.println("더 이상 상품을 등록할 수 없습니다.");
                    continue;
                }

                System.out.print("상품 이름을 입력하세요 : ");
                productNames[productCount] = input.nextLine();

                System.out.print("상품 가격을 입력하세요 : ");
                productPrices[productCount] = input.nextInt();

                productCount++;
            } else if (menu == 2) {
                if (productCount == 0) {
                    System.out.println("등록된 상품이 없습니다.");
                    continue;
                }

                for (int j = 0; j < productCount; j++) {
                    System.out.println(productNames[j] + " : " + productPrices[j]);
                }
            } else if (menu == 3) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 메뉴를 선택하셨습니다.");
            }
        }
    }
}
