package top.thesumst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Student {
    private String name ;
    private int grade ;

    public Student(String name, int grade ) {
        this.name = name ;
        this.grade = grade ;
    }
    public Student() {
        this.name = "" ;
        this.grade = 0;
    }

    public void importName( String str ) {
        name = str ;
    }
    public void importGrade( int i ) {
        grade = i ;
    }

    public String getName() {
        return name ;
    }
    public int getGrade() {
        return grade;
    }
}

public class GradeOperation {
    // *  存储成绩文本路径
    private String Path ;

    // * 缓存学生信息
    private ArrayList<Student> studentList = new ArrayList<>();

    // * 主入口
    public static void main( String[] args ) {
        GradeOperation gradeOperation = new GradeOperation() ;
        System.out.println("Welcome to Grade Operation Ver0.1 !");
        System.out.println("Please import your grade file : ");
        Scanner sc = new Scanner(System.in) ;

        // * 读取用户输入的文本文件
        gradeOperation.Path = sc.nextLine();
        System.out.println("File path set to : " + gradeOperation.Path);
        sc.close();

        // * 读入文件
        gradeOperation.readFile();

        // * 对学生缓存信息进行排序
        gradeOperation.gradeSort();

        // * 输出学生信息
        gradeOperation.gradePrint();
    }

    /**
     *  读取文件
     */ 
    public void readFile() {
        try (Scanner sc = new Scanner(new File(Path))) {
            while ( sc.hasNextLine()) {
                // * 行格式：名字 成绩
                String line = sc.nextLine() ;
                Scanner lineScanner = new Scanner(line) ;
                String name = lineScanner.next() ;
                int grade = lineScanner.nextInt() ;
                lineScanner.close();
                
                // * 导入成绩信息
                Student st = new Student() ;
                st.importName(name);
                st.importGrade(grade);
                studentList.add(st) ;
            }
            
            System.out.println("Import is finished");
        } catch ( FileNotFoundException e ) {
            System.out.println("File not found in " + "\"" + Path + "\"");
            e.printStackTrace();
        }
    }

    /**
     * 简介：排序学生信息
     * 顺序：按照成绩从高到低
     */
    private void gradeSort() {
        java.util.Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2 ) {
                return Integer.compare(s2.getGrade(), s1.getGrade()) ;
            }
        }) ;
    }
    
    /**
     * 输出学生信息
     */
    private void gradePrint() {
        for( Student st : studentList ) {
            System.out.printf("姓名:%-10s|成绩:%4d\n" , st.getName() , st.getGrade());
        }
    }
}
