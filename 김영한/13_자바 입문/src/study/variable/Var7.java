package study.variable;

import jdk.swing.interop.SwingInterOpUtils;

public class Var7 {
    public static void main(String[] args) {
        int a = 100;                // 정수
        double b = 10.5;            // 실수
        boolean c = true;           // true, false
        char d = 'A';               // 문자 하나
        String e = "Hello Java";    // 문자열을 다루기 위한 특별한 타입

        /* 타입 오류 발생
            int f = "백원";
            int g = "100";
         */

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
    }
}
