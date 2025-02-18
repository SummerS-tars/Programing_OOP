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

public class PictureExtractor {

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
            // 使用 Jsoup 连接并获取文档，添加 User-Agent、Referer 和 Cookie 请求头
            Document doc = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                                .header("Referer", "https://www.zhihu.com/")
                                .header("Cookie", "_xsrf=L3CQGA7t9SpfkzQLgVREeiKzNWuyKOSS; _zap=bf3448d7-be0e-4e8e-945e-52127f66cacf; d_c0=ARBSNUpr2hmPTsu3d3CpZWo-KbpJ0CqQfug=|1736934587; gdxidpyhxdE=VR7sHUyyYYHJC2L0VM2A%2BNrlZZNtmx1QK9gfPWvv%5Cv3N4N76%5C%2FJMmwGd5hdGrfwCV%2FarGu8UDZIS%5CuYOGomUPhDvYP%2BGmXQQzz24Jtz4pOdrorUL3T24NghgC9em3S4Rf%2FaNK7YHBNMxWLtAbVMmrduaa%2BNXsZiJWpWgLd3p2yqKlO6C%3A1736935490976; captcha_session_v2=2|1:0|10:1736934591|18:captcha_session_v2|88:UTBjVkpUTDY1MjdCRFljQXl0ZDY1SzE5eFlha2ZSbk1yWkFNZzEyeFhIZDVuaTRuckpzLzlyelNNNDB6K2JLWA==|e193e6c6460a3500088ee7eeec97cf5f259c53dc4b03b5e188cbddf52d2a6c52; q_c1=3a441da772aa49a99234289963f3f0d8|1736934597000|1736934597000; _tea_utm_cache_20001731={%22utm_content%22:%22search_suggestion%22}; tst=r; __zse_ck=003_bnmVOP5uAbA0xPsOFPyjihNGk/7e1M76u1RpK2makJiZEphjop8PNbnSjc1kqJkpWhz67GB7DaXLuScSomP/xSdyB9evyteVJ4E+21/BED7h; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1737130408,1737168884,1737169112,1737295163; HMACCOUNT=8F563B7D13FDEEC1; z_c0=2|1:0|10:1737295160|4:z_c0|80:MS4xWW5UREVBQUFBQUFtQUFBQVlBSlZUVGRUZW1obUVyZ0tmRjlkQ0xJSVFZa0lnUzdpdVZGOUV3PT0=|0a80a6341cdf8ce7e97760c208f4a67b7befa7dc0646d357ee52b5f38967e322; BEC=6bca8f185b99e85d761c7a0d8d692864; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1737295227") // 添加实际的 Cookie 和 _xsrf 值
                                .get();

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