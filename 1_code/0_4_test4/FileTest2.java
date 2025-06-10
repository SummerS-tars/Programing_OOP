import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileTest2 implements AutoCloseable {
    File file;
    PrintWriter printWriter;

    public FileTest2(String fileName) throws IOException {
        file = new File(fileName);
        printWriter = new PrintWriter(file);
    }

    public void open() {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String text) {
        printWriter.write(text);
    }

    public void close() {
        try {
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a name for the file: ");
        String fileName = sc.nextLine();
        System.out.println("Enter a line of text for the file: ");
        String text = sc.nextLine();
        sc.close();

        try (FileTest2 fileTest2 = new FileTest2(fileName);
        Scanner fileSc = new Scanner(fileTest2.file)) {
            fileTest2.open();
            fileTest2.write(text);
            fileTest2.close();
            if (fileTest2.file.exists()) {
                while (fileSc.hasNextLine()) {
                    System.out.println(fileSc.nextLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
