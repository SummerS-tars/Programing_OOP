import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * 本脚本功能：
 * 1. 将指定文件夹下所有 .java 文件移动到新文件夹
 * 2. 更新指定文件夹下所有 Markdown 文件中的引用路径(相对路径)
 * 
 * 调用命令参数说明:
 * java FileMover <sourceFolder> <newPath> <newFolderName> <markdownFolder>
 * 1. sourceFolder: 源文件夹路径
 * 2. newPath: 新文件夹路径
 * 3. newFolderName: 新文件夹名称
 * 4. markdownFolder: Markdown 文件夹路径
 */

public class FileMover {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: java FileMover <sourceFolder> <newPath> <newFolderName> <markdownFolder>");
            return;
        }

        String sourceFolder = args[0];
        String newPath = args[1];
        String newFolderName = args[2];
        String markdownFolder = args[3];

        try {
            // 创建新文件夹（如果不存在）
            Path newFolderPath = Paths.get(newPath, newFolderName);
            if (!Files.exists(newFolderPath)) {
                Files.createDirectories(newFolderPath);
            }

            // 移动文件夹下所有 .java 文件到新文件夹
            moveJavaFiles(sourceFolder, newFolderPath.toString());

            // 更新文件夹下所有 Markdown 文件中的引用路径
            updateMarkdownFiles(markdownFolder, sourceFolder, newFolderPath.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void moveJavaFiles(String sourceFolder, String newPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(sourceFolder))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".java"))
                 .forEach(source -> {
                     try {
                         Path destination = Paths.get(newPath, source.getFileName().toString());
                         Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

    private static void updateMarkdownFiles(String folderPath, String oldPath, String newPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".md"))
                 .forEach(path -> {
                     try {
                         updateMarkdownFile(path, oldPath, newPath);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
        }
    }

    private static void updateMarkdownFile(Path markdownFilePath, String oldPath, String newPath) throws IOException {
        String content = new String(Files.readAllBytes(markdownFilePath), "UTF-8");

        // 计算旧路径和新路径的相对路径
        Path markdownDir = markdownFilePath.getParent();
        Path oldRelativePath = markdownDir.relativize(Paths.get(oldPath));
        Path newRelativePath = markdownDir.relativize(Paths.get(newPath));

        // 将路径分隔符转换为 '/'
        String oldRelativePathStr = oldRelativePath.toString().replace("\\", "/");
        String newRelativePathStr = newRelativePath.toString().replace("\\", "/");

        // 使用正则表达式替换旧路径为新路径
        String updatedContent = content.replaceAll(Pattern.quote(oldRelativePathStr), Matcher.quoteReplacement(newRelativePathStr));

        Files.write(markdownFilePath, updatedContent.getBytes("UTF-8"));
    }
}