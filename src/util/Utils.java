package util;

import java.util.Scanner;

public class Utils {
    private static Scanner num = new Scanner(System.in);
    private static Scanner str = new Scanner(System.in);

    public static Integer getNum() {
        return num.nextInt();
    }

    public static Integer getNum(String s) {
        System.out.print(s);
        return getNum();
    }

    public static String getStr() {
        return str.nextLine();
    }

    public static String getStr(String s) {
        System.out.print(s);
        return getStr();
    }

}
