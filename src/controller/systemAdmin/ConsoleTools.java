package controller.systemAdmin;

import utils.Tools;

import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public final class ConsoleTools {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleTools() {
    }

    public static void printTitle(String title) {
        System.out.println();
        System.out.println("===== " + title + " =====");
    }

    public static String input(String tip) {
        System.out.print(tip);
        return SCANNER.nextLine().trim();
    }

    public static int inputInt(String tip) {
        while (true) {
            try {
                return Integer.parseInt(input(tip));
            } catch (NumberFormatException e) {
                System.out.println("请输入数字！");
            }
        }
    }

    public static Integer inputIntOrNull(String tip) {
        String s = input(tip);
        return s.isEmpty() ? null : Integer.valueOf(s);
    }

    public static Date parseDate(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return Tools.DATE_FORMAT.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String fmt(Date date) {
        return date == null ? "" : Tools.DATE_FORMAT.format(date);
    }

    public static String keep(String tip, String oldValue) {
        String s = input(tip);
        return s.isEmpty() ? oldValue : s;
    }

    public static Date keepDate(String tip, Date oldValue) throws ParseException {
        String s = input(tip);
        return s.isEmpty() ? oldValue : Tools.DATE_FORMAT.parse(s);
    }

    public static boolean confirm(String tip) {
        return "y".equalsIgnoreCase(input(tip).trim());
    }

    public static String sexText(Integer sex) {
        if (sex == null) {
            return "";
        }
        return sex == 0 ? "男" : "女";
    }

    public static String auditText(Integer status, int passValue) {
        if (status == null || status == 0) {
            return "待审核";
        }
        return status == passValue ? "通过" : "不通过";
    }

    public static String statusText(Integer status, String onText, String offText) {
        if (status == null) {
            return "";
        }
        return status == 1 ? onText : offText;
    }

    public static void printError(Exception e) {
        System.out.println("操作失败：" + e.getMessage());
    }

}
