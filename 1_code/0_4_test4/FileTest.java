import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileTest {
    public static void main(String[] args) {
        System.out.println("--------------------------------");
        try {
            File file = new File("test.txt");
            file.createNewFile();
            System.out.println(file.exists());
            System.out.println(file.isFile());
            System.out.println(file.isDirectory());
            System.out.println(file.isAbsolute());
            System.out.println(file.isHidden());
            System.out.println(file.canRead());
            System.out.println(file.canWrite());
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getCanonicalPath());
            System.out.println(file.getName());
            System.out.println(file.getPath());
            System.out.println(file.getParent());
            System.out.println(file.length());
            System.out.println(file.lastModified());
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------------------------");
    }
}