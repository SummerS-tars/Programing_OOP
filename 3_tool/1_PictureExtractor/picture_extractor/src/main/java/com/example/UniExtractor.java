package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class UniExtractor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入要爬取图片的网页URL:");
        String url = scanner.nextLine();

        System.out.println("请输入下载保存的目标文件夹完整路径:");
        String saveFolder = scanner.nextLine();

        System.out.println("请输入图片文件名前缀(例如 pic):");
        String prefix = scanner.nextLine();

        scanner.close();

        // 创建目标文件夹（如果不存在）
        try {
            Files.createDirectories(Paths.get(saveFolder));
        } catch (IOException e) {
            System.err.println("创建文件夹失败: " + e.getMessage());
            return;
        }

        try {
            // 使用 Jsoup 连接并获取文档
            Document doc = Jsoup.connect(url).get();

            // 获取所有 <img> 标签
            Elements imgElements = doc.select("img");

            int count = 1;
            for (Element img : imgElements) {
                // 获取 src 属性
                String src = img.attr("abs:src"); // abs:src 可获取绝对路径
                if (src == null || src.isEmpty()) {
                    continue;
                }

                // 下载图片
                String fileName = prefix + "_" + count + getFileExtension(src);
                downloadImage(src, Paths.get(saveFolder, fileName).toString());
                System.out.println("已下载: " + fileName);

                count++;
            }
            System.out.println("图片下载完成，共 " + (count - 1) + " 张。");
        } catch (IOException e) {
            System.err.println("下载图片失败: " + e.getMessage());
        }
    }

    // 下载图片并保存到指定路径
    private static void downloadImage(String imageUrl, String destFilePath) {
        try (InputStream in = new URL(imageUrl).openStream();
             OutputStream out = new FileOutputStream(destFilePath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("无法下载图片: " + e.getMessage());
        }
    }

    // 简单获取文件扩展名(如果无扩展名,默认返回 .jpg)
    private static String getFileExtension(String url) {
        int lastDot = url.lastIndexOf('.');
        if (lastDot == -1 || lastDot == url.length() - 1) {
            return ".jpg";
        }
        // 基于简单场景返回扩展名
        String ext = url.substring(lastDot);
        if (ext.contains("/") || ext.contains("?")) {
            return ".jpg";
        }
        return ext;
    }
}