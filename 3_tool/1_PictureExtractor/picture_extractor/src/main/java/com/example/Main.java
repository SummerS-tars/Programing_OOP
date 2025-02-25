package com.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择要启动的功能:");
        System.out.println("1. 知乎");
        System.out.println("2. 学霸网");
        System.out.println("3. 通用图片提取器");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                PictureExtractor.main(args);
                break;
            case 2:
                BookDownloader.main(args);
                break;
            case 3:
                UniExtractor.main(args);
                break;
            default:
                System.out.println("无效选择");
        }

        scanner.close();
    }
}