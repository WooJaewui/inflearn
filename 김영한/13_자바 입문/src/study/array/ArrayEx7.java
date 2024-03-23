package study.array;

import java.util.Scanner;

public class ArrayEx7 {
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in);
        int[][] scores = new int[4][3];
        String[] subjects = {"국어", "영어", "수학"};

        for (int i = 0; i < scores.length; i++) {
            System.out.println((i + 1) + "번 학생의 성적을 입력하세요 : ");

            for (int j = 0; j < subjects.length; j++) {
                System.out.print(subjects[j] + " 점수 : ");
                scores[i][j] = input.nextInt();
            }
        }

        for (int i = 0; i < scores.length; i++) {
            int sum = scores[i][0] + scores[i][1] + scores[i][2];
            double average = (double) sum / scores[i].length;
            System.out.println((i+1) + "번 학생의 총점 : " + sum + ", 평균 : " + average);
        }
    }
}
