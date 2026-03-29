package restaurant.util;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner sc = new Scanner(System.in);


    public static int readIntPositive(String message) {
        int value;
        while (true) {
            try {
                System.out.print(message);
                value = Integer.parseInt(sc.nextLine());
                if (value > 0) return value;
                System.out.println("Lỗi: Giá trị phải là số nguyên dương!");
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng chỉ nhập số nguyên!");
            }
        }
    }


    public static double readDoublePositive(String message) {
        double value;
        while (true) {
            try {
                System.out.print(message);
                value = Double.parseDouble(sc.nextLine());
                if (value > 0) return value;
                System.out.println("Lỗi: Số tiền phải lớn hơn 0!");
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số thập phân hợp lệ!");
            }
        }
    }


    public static String readNonEmptyString(String message) {
        String input;
        while (true) {
            System.out.print(message);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Lỗi: Nội dung này không được để trống!");
        }
    }
}